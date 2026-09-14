package com.malthouse.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "brew_batches")
public class BrewBatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fermenter_id", nullable = false)
    private Long fermenterId;

    @Column(nullable = false, length = 128)
    private String recipeName;

    @Column(nullable = false)
    private LocalDate brewDate;

    @Column(nullable = false)
    private Double originalGravity;

    @Column(nullable = false)
    private Double targetFg;

    /** planned | active | packaged | dumped */
    @Column(nullable = false, length = 32)
    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFermenterId() {
        return fermenterId;
    }

    public void setFermenterId(Long fermenterId) {
        this.fermenterId = fermenterId;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public LocalDate getBrewDate() {
        return brewDate;
    }

    public void setBrewDate(LocalDate brewDate) {
        this.brewDate = brewDate;
    }

    public Double getOriginalGravity() {
        return originalGravity;
    }

    public void setOriginalGravity(Double originalGravity) {
        this.originalGravity = originalGravity;
    }

    public Double getTargetFg() {
        return targetFg;
    }

    public void setTargetFg(Double targetFg) {
        this.targetFg = targetFg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
