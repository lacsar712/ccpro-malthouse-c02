package com.malthouse.seed;

import com.malthouse.entity.*;
import com.malthouse.repository.*;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class DataSeeder implements ApplicationRunner {

    private final UserRepository userRepository;
    private final BrewerySiteRepository siteRepository;
    private final FermenterRepository fermenterRepository;
    private final BrewBatchRepository batchRepository;
    private final GravityReadingRepository readingRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(
            UserRepository userRepository,
            BrewerySiteRepository siteRepository,
            FermenterRepository fermenterRepository,
            BrewBatchRepository batchRepository,
            GravityReadingRepository readingRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.siteRepository = siteRepository;
        this.fermenterRepository = fermenterRepository;
        this.batchRepository = batchRepository;
        this.readingRepository = readingRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (userRepository.count() > 0) {
            return;
        }

        User admin = new User();
        admin.setUsername("admin");
        admin.setPasswordHash(passwordEncoder.encode("123456"));
        admin.setRole("admin");
        admin.setDisplayName("系统管理员");
        userRepository.save(admin);

        User brewer = new User();
        brewer.setUsername("brewer");
        brewer.setPasswordHash(passwordEncoder.encode("123456"));
        brewer.setRole("brewer");
        brewer.setDisplayName("酿造员");
        userRepository.save(brewer);

        BrewerySite main = new BrewerySite();
        main.setName("东湖精酿主厂");
        main.setLocation("武汉市东湖高新区");
        main.setNotes("主发酵与包装区");
        siteRepository.save(main);

        BrewerySite pilot = new BrewerySite();
        pilot.setName("试点小试车间");
        pilot.setLocation("厂区北侧二楼");
        pilot.setNotes("试验批次与新配方验证");
        siteRepository.save(pilot);

        Fermenter f1 = tank(main.getId(), "FV-01", 2000.0, "fermenting");
        Fermenter f2 = tank(main.getId(), "FV-02", 2000.0, "idle");
        Fermenter f3 = tank(main.getId(), "FV-03", 1000.0, "cip");
        Fermenter f4 = tank(pilot.getId(), "PV-01", 300.0, "fermenting");
        fermenterRepository.save(f1);
        fermenterRepository.save(f2);
        fermenterRepository.save(f3);
        fermenterRepository.save(f4);

        BrewBatch b1 = batch(f1.getId(), "德式小麦", LocalDate.now().minusDays(5), 1.048, 1.010, "active");
        BrewBatch b2 = batch(f2.getId(), "美式IPA", LocalDate.now().minusDays(20), 1.062, 1.012, "packaged");
        BrewBatch b3 = batch(f4.getId(), "新英格兰IPA试点", LocalDate.now().minusDays(3), 1.055, 1.012, "active");
        BrewBatch b4 = batch(f3.getId(), "清爽拉格", LocalDate.now().plusDays(2), 1.045, 1.008, "planned");
        batchRepository.save(b1);
        batchRepository.save(b2);
        batchRepository.save(b3);
        batchRepository.save(b4);

        readingRepository.save(reading(b1.getId(), LocalDateTime.now().minusDays(4), 1.048, 20.0, "入罐初始比重"));
        readingRepository.save(reading(b1.getId(), LocalDateTime.now().minusDays(2), 1.028, 19.5, "主酵中期"));
        readingRepository.save(reading(b1.getId(), LocalDateTime.now().minusHours(8), 1.014, 18.0, "接近目标 FG"));
        readingRepository.save(reading(b3.getId(), LocalDateTime.now().minusDays(2), 1.055, 19.0, "小试入罐"));
        readingRepository.save(reading(b3.getId(), LocalDateTime.now().minusDays(1), 1.030, 18.5, null));
        readingRepository.save(reading(b2.getId(), LocalDateTime.now().minusDays(15), 1.012, 4.0, "冷储前终比重"));
    }

    private Fermenter tank(Long siteId, String code, double liters, String status) {
        Fermenter f = new Fermenter();
        f.setSiteId(siteId);
        f.setTankCode(code);
        f.setCapacityLiters(liters);
        f.setStatus(status);
        return f;
    }

    private BrewBatch batch(Long fermenterId, String recipe, LocalDate date, double og, double fg, String status) {
        BrewBatch b = new BrewBatch();
        b.setFermenterId(fermenterId);
        b.setRecipeName(recipe);
        b.setBrewDate(date);
        b.setOriginalGravity(og);
        b.setTargetFg(fg);
        b.setStatus(status);
        return b;
    }

    private GravityReading reading(Long batchId, LocalDateTime at, double sg, double temp, String notes) {
        GravityReading r = new GravityReading();
        r.setBatchId(batchId);
        r.setMeasuredAt(at);
        r.setSpecificGravity(sg);
        r.setTemperatureC(temp);
        r.setNotes(notes);
        return r;
    }
}
