package com.example.spring.validation;

import java.util.ArrayList;
import java.util.List;

public class ValidationErrors {

    private List<ValidationError> errors;

    public ValidationErrors() {
        this.errors = new ArrayList<>(0);
    }

    public ValidationErrors(List<ValidationError> errors) {
        this.errors = errors;
    }

    public List<ValidationError> getErrors() {
        return List.copyOf(errors);
    }

    public void setErrors(List<ValidationError> errors) {
        this.errors = errors;
    }

    public ValidationErrors addError(ValidationError error) {
        this.errors.add(error);
        return this;
    }
}
