package com.malthouse.service;

import com.malthouse.repository.BrewBatchRepository;
import com.malthouse.repository.FermenterRepository;
import com.malthouse.repository.GravityReadingRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class DashboardService {

    private final FermenterRepository fermenterRepository;
    private final BrewBatchRepository brewBatchRepository;
    private final GravityReadingRepository gravityReadingRepository;

    public DashboardService(
            FermenterRepository fermenterRepository,
            BrewBatchRepository brewBatchRepository,
            GravityReadingRepository gravityReadingRepository) {
        this.fermenterRepository = fermenterRepository;
        this.brewBatchRepository = brewBatchRepository;
        this.gravityReadingRepository = gravityReadingRepository;
    }

    public Map<String, Object> summary() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("fermenterCount", fermenterRepository.count());
        result.put("activeBatchCount", brewBatchRepository.countByStatus("active"));
        result.put("readingsLast7Days", gravityReadingRepository.countByMeasuredAtAfter(LocalDateTime.now().minusDays(7)));

        Map<String, Long> byStatus = new LinkedHashMap<>();
        byStatus.put("planned", 0L);
        byStatus.put("active", 0L);
        byStatus.put("packaged", 0L);
        byStatus.put("dumped", 0L);
        for (Object[] row : brewBatchRepository.countGroupByStatus()) {
            byStatus.put(String.valueOf(row[0]), (Long) row[1]);
        }
        result.put("batchesByStatus", byStatus);
        return result;
    }
}
