package com.malthouse.service;

import com.malthouse.dto.GravityReadingRequest;
import com.malthouse.entity.GravityReading;
import com.malthouse.exception.BusinessException;
import com.malthouse.repository.BrewBatchRepository;
import com.malthouse.repository.GravityReadingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GravityReadingService {

    private final GravityReadingRepository repository;
    private final BrewBatchRepository batchRepository;

    public GravityReadingService(GravityReadingRepository repository, BrewBatchRepository batchRepository) {
        this.repository = repository;
        this.batchRepository = batchRepository;
    }

    public List<GravityReading> list() {
        return repository.findAll();
    }

    public List<GravityReading> listByBatch(Long batchId) {
        return repository.findByBatchIdOrderByMeasuredAtDesc(batchId);
    }

    public GravityReading get(Long id) {
        return repository.findById(id).orElseThrow(() -> new BusinessException("比重读数不存在"));
    }

    @Transactional
    public GravityReading create(GravityReadingRequest req) {
        validate(req);
        GravityReading entity = new GravityReading();
        apply(entity, req);
        return repository.save(entity);
    }

    @Transactional
    public GravityReading update(Long id, GravityReadingRequest req) {
        GravityReading entity = get(id);
        validate(req);
        apply(entity, req);
        return repository.save(entity);
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new BusinessException("比重读数不存在");
        }
        repository.deleteById(id);
    }

    private void validate(GravityReadingRequest req) {
        if (!batchRepository.existsById(req.getBatchId())) {
            throw new BusinessException("发酵批次不存在");
        }
    }

    private void apply(GravityReading entity, GravityReadingRequest req) {
        entity.setBatchId(req.getBatchId());
        entity.setMeasuredAt(req.getMeasuredAt());
        entity.setSpecificGravity(req.getSpecificGravity());
        entity.setTemperatureC(req.getTemperatureC());
        entity.setNotes(req.getNotes());
    }
}
