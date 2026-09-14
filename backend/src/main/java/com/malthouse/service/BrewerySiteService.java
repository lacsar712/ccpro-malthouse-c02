package com.malthouse.service;

import com.malthouse.dto.BrewerySiteRequest;
import com.malthouse.entity.BrewerySite;
import com.malthouse.exception.BusinessException;
import com.malthouse.repository.BrewerySiteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BrewerySiteService {

    private final BrewerySiteRepository repository;

    public BrewerySiteService(BrewerySiteRepository repository) {
        this.repository = repository;
    }

    public List<BrewerySite> list() {
        return repository.findAll();
    }

    public BrewerySite get(Long id) {
        return repository.findById(id).orElseThrow(() -> new BusinessException("厂区不存在"));
    }

    @Transactional
    public BrewerySite create(BrewerySiteRequest req) {
        BrewerySite site = new BrewerySite();
        apply(site, req);
        return repository.save(site);
    }

    @Transactional
    public BrewerySite update(Long id, BrewerySiteRequest req) {
        BrewerySite site = get(id);
        apply(site, req);
        return repository.save(site);
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new BusinessException("厂区不存在");
        }
        repository.deleteById(id);
    }

    private void apply(BrewerySite site, BrewerySiteRequest req) {
        site.setName(req.getName().trim());
        site.setLocation(req.getLocation());
        site.setNotes(req.getNotes());
    }
}
