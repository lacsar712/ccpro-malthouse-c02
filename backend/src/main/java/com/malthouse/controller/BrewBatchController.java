package com.malthouse.controller;

import com.malthouse.dto.BrewBatchRequest;
import com.malthouse.entity.BrewBatch;
import com.malthouse.service.BrewBatchService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/batches")
public class BrewBatchController {

    private final BrewBatchService service;

    public BrewBatchController(BrewBatchService service) {
        this.service = service;
    }

    @GetMapping
    public List<BrewBatch> list() {
        return service.list();
    }

    @GetMapping("/{id}")
    public BrewBatch get(@PathVariable Long id) {
        return service.get(id);
    }

    @PostMapping
    public BrewBatch create(@Valid @RequestBody BrewBatchRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public BrewBatch update(@PathVariable Long id, @Valid @RequestBody BrewBatchRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
