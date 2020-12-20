package com.example.spring.validation;

import java.util.List;

public final class ValidationError {

    private String field;
    private String message;
    private List<Object> metadata;

    public ValidationError() {
    }

    public ValidationError(String field, String message, List<Object> metadata) {
        this.field = field;
        this.message = message;
        this.metadata = metadata;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(List<Object> metadata) {
        this.metadata = metadata;
    }
}
