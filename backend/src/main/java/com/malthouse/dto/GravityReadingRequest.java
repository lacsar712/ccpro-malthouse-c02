package com.malthouse.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class GravityReadingRequest {
    @NotNull
    private Long batchId;
    @NotNull
    private LocalDateTime measuredAt;
    @NotNull
    private Double specificGravity;
    @NotNull
    private Double temperatureC;
    private String notes;

    public Long getBatchId() {
        return batchId;
    }

    public void setBatchId(Long batchId) {
        this.batchId = batchId;
    }

    public LocalDateTime getMeasuredAt() {
        return measuredAt;
    }

    public void setMeasuredAt(LocalDateTime measuredAt) {
        this.measuredAt = measuredAt;
    }

    public Double getSpecificGravity() {
        return specificGravity;
    }

    public void setSpecificGravity(Double specificGravity) {
        this.specificGravity = specificGravity;
    }

    public Double getTemperatureC() {
        return temperatureC;
    }

    public void setTemperatureC(Double temperatureC) {
        this.temperatureC = temperatureC;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
