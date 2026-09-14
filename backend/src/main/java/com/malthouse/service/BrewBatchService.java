package com.malthouse.service;

import com.malthouse.dto.BrewBatchRequest;
import com.malthouse.entity.BrewBatch;
import com.malthouse.exception.BusinessException;
import com.malthouse.repository.BrewBatchRepository;
import com.malthouse.repository.FermenterRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class BrewBatchService {

    private static final Set<String> STATUSES = Set.of("planned", "active", "packaged", "dumped");

    private final BrewBatchRepository repository;
    private final FermenterRepository fermenterRepository;

    public BrewBatchService(BrewBatchRepository repository, FermenterRepository fermenterRepository) {
        this.repository = repository;
        this.fermenterRepository = fermenterRepository;
    }

    public List<BrewBatch> list() {
        return repository.findAll();
    }

    public BrewBatch get(Long id) {
        return repository.findById(id).orElseThrow(() -> new BusinessException("发酵批次不存在"));
    }

    @Transactional
    public BrewBatch create(BrewBatchRequest req) {
        validate(req);
        BrewBatch entity = new BrewBatch();
        apply(entity, req);
        return repository.save(entity);
    }

    @Transactional
    public BrewBatch update(Long id, BrewBatchRequest req) {
        BrewBatch entity = get(id);
        validate(req);
        apply(entity, req);
        return repository.save(entity);
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new BusinessException("发酵批次不存在");
        }
        repository.deleteById(id);
    }

    private void validate(BrewBatchRequest req) {
        if (!fermenterRepository.existsById(req.getFermenterId())) {
            throw new BusinessException("发酵罐不存在");
        }
        if (!STATUSES.contains(req.getStatus())) {
            throw new BusinessException("批次状态必须是 planned / active / packaged / dumped");
        }
    }

    private void apply(BrewBatch entity, BrewBatchRequest req) {
        entity.setFermenterId(req.getFermenterId());
        entity.setRecipeName(req.getRecipeName().trim());
        entity.setBrewDate(req.getBrewDate());
        entity.setOriginalGravity(req.getOriginalGravity());
        entity.setTargetFg(req.getTargetFg());
        entity.setStatus(req.getStatus());
    }
}
