package com.example.spring.validation;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

public final class Validations {

    private Validations() {
    }

    public static Mono<ValidationErrors> validate(List<ValidationRule> rules) {
        return Flux.fromIterable(rules)
            .map(ValidationRule::validate)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .reduce(new ValidationErrors(), ValidationErrors::addError);
    }
}
