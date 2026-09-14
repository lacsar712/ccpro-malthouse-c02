package com.malthouse.service;

import com.malthouse.dto.FermenterRequest;
import com.malthouse.entity.Fermenter;
import com.malthouse.exception.BusinessException;
import com.malthouse.repository.BrewerySiteRepository;
import com.malthouse.repository.FermenterRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class FermenterService {

    private static final Set<String> STATUSES = Set.of("idle", "fermenting", "cip");

    private final FermenterRepository repository;
    private final BrewerySiteRepository siteRepository;

    public FermenterService(FermenterRepository repository, BrewerySiteRepository siteRepository) {
        this.repository = repository;
        this.siteRepository = siteRepository;
    }

    public List<Fermenter> list() {
        return repository.findAll();
    }

    public Fermenter get(Long id) {
        return repository.findById(id).orElseThrow(() -> new BusinessException("发酵罐不存在"));
    }

    @Transactional
    public Fermenter create(FermenterRequest req) {
        validate(req, null);
        Fermenter entity = new Fermenter();
        apply(entity, req);
        return repository.save(entity);
    }

    @Transactional
    public Fermenter update(Long id, FermenterRequest req) {
        Fermenter entity = get(id);
        validate(req, id);
        apply(entity, req);
        return repository.save(entity);
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new BusinessException("发酵罐不存在");
        }
        repository.deleteById(id);
    }

    private void validate(FermenterRequest req, Long id) {
        if (!siteRepository.existsById(req.getSiteId())) {
            throw new BusinessException("所属厂区不存在");
        }
        if (!STATUSES.contains(req.getStatus())) {
            throw new BusinessException("罐状态必须是 idle / fermenting / cip");
        }
        String code = req.getTankCode().trim();
        boolean dup = id == null
                ? repository.existsBySiteIdAndTankCode(req.getSiteId(), code)
                : repository.existsBySiteIdAndTankCodeAndIdNot(req.getSiteId(), code, id);
        if (dup) {
            throw new BusinessException("同一厂区内罐号 tankCode 必须唯一");
        }
    }

    private void apply(Fermenter entity, FermenterRequest req) {
        entity.setSiteId(req.getSiteId());
        entity.setTankCode(req.getTankCode().trim());
        entity.setCapacityLiters(req.getCapacityLiters());
        entity.setStatus(req.getStatus());
    }
}
