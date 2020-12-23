package com.example.spring.validation;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ValidationTest {

    private final List<Object> metadata = List.of("text", 123, LocalDate.now(), UUID.randomUUID());

    private final ValidationError validationError = new ValidationError("field", "message", metadata);

    private final ValidationRule validationRuleWithError = () -> Optional.of(validationError);

    private final ValidationRule validationRuleWithoutError = Optional::empty;

    @Test
    void testApplyingRules() {
        List<ValidationRule> rules = List.of(validationRuleWithError, validationRuleWithoutError);

        StepVerifier.create(Validation.validateAll(rules))
            .assertNext(validationErrors -> {
                List<ValidationError> errors = validationErrors.getErrors();
                assertEquals(1, errors.size());

                ValidationError error = errors.get(0);
                assertEquals(validationError.getField(), error.getField());
                assertEquals(validationError.getMessage(), error.getMessage());
                assertEquals(validationError.getMetadata(), error.getMetadata());
            })
            .verifyComplete();
    }
}