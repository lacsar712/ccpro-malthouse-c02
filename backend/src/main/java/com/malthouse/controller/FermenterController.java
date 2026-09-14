package com.malthouse.controller;

import com.malthouse.dto.FermenterRequest;
import com.malthouse.entity.Fermenter;
import com.malthouse.service.FermenterService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fermenters")
public class FermenterController {

    private final FermenterService service;

    public FermenterController(FermenterService service) {
        this.service = service;
    }

    @GetMapping
    public List<Fermenter> list() {
        return service.list();
    }

    @GetMapping("/{id}")
    public Fermenter get(@PathVariable Long id) {
        return service.get(id);
    }

    @PostMapping
    public Fermenter create(@Valid @RequestBody FermenterRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public Fermenter update(@PathVariable Long id, @Valid @RequestBody FermenterRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
