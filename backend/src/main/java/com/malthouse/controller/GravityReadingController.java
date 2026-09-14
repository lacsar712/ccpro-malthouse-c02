package com.malthouse.controller;

import com.malthouse.dto.GravityReadingRequest;
import com.malthouse.entity.GravityReading;
import com.malthouse.service.GravityReadingService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/readings")
public class GravityReadingController {

    private final GravityReadingService service;

    public GravityReadingController(GravityReadingService service) {
        this.service = service;
    }

    @GetMapping
    public List<GravityReading> list(@RequestParam(required = false) Long batchId) {
        if (batchId != null) {
            return service.listByBatch(batchId);
        }
        return service.list();
    }

    @GetMapping("/{id}")
    public GravityReading get(@PathVariable Long id) {
        return service.get(id);
    }

    @PostMapping
    public GravityReading create(@Valid @RequestBody GravityReadingRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public GravityReading update(@PathVariable Long id, @Valid @RequestBody GravityReadingRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
