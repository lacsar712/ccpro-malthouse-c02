package com.malthouse.dto;

import java.util.Map;

public class ApiError {
    private String message;
    private Map<String, String> details;

    public ApiError() {
    }

    public ApiError(String message) {
        this.message = message;
    }

    public ApiError(String message, Map<String, String> details) {
        this.message = message;
        this.details = details;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, String> getDetails() {
        return details;
    }

    public void setDetails(Map<String, String> details) {
        this.details = details;
    }
}
