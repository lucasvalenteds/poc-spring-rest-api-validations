package com.example.spring.validations;

import com.example.spring.account.Account;
import com.example.spring.validation.ValidationError;
import com.example.spring.validation.ValidationRule;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AccountIsNotLockedTest {

    @Test
    void testLockedEqualsTrueReturnsError() {
        Account account = new Account();
        account.setLocked(true);

        ValidationRule rule = new AccountIsNotLocked(account);
        Optional<ValidationError> error = rule.validate();

        assertTrue(error.isPresent());
        assertEquals("locked", error.get().getField());
        assertEquals("Account.State.isLocked", error.get().getMessage());
        assertEquals(0, error.get().getMetadata().size());
    }

    @Test
    void testLockedEqualsFalseDoesNotReturnsError() {
        Account account = new Account();
        account.setLocked(false);

        ValidationRule rule = new AccountIsNotLocked(account);
        Optional<ValidationError> error = rule.validate();

        assertTrue(error.isEmpty());
    }
}