package com.malthouse.controller;

import com.malthouse.dto.BrewerySiteRequest;
import com.malthouse.entity.BrewerySite;
import com.malthouse.service.BrewerySiteService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sites")
public class BrewerySiteController {

    private final BrewerySiteService service;

    public BrewerySiteController(BrewerySiteService service) {
        this.service = service;
    }

    @GetMapping
    public List<BrewerySite> list() {
        return service.list();
    }

    @GetMapping("/{id}")
    public BrewerySite get(@PathVariable Long id) {
        return service.get(id);
    }

    @PostMapping
    public BrewerySite create(@Valid @RequestBody BrewerySiteRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public BrewerySite update(@PathVariable Long id, @Valid @RequestBody BrewerySiteRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
