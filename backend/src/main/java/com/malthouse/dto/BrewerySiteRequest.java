package com.malthouse.dto;

import jakarta.validation.constraints.NotBlank;

public class BrewerySiteRequest {
    @NotBlank
    private String name;
    private String location;
    private String notes;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
