package com.malthouse.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class BrewBatchRequest {
    @NotNull
    private Long fermenterId;
    @NotBlank
    private String recipeName;
    @NotNull
    private LocalDate brewDate;
    @NotNull
    private Double originalGravity;
    @NotNull
    private Double targetFg;
    @NotBlank
    private String status;

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
