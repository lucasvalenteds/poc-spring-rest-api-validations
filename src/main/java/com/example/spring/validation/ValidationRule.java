package com.example.spring.validation;

import java.util.Optional;

@FunctionalInterface
public interface ValidationRule {

    Optional<ValidationError> validate();
}
