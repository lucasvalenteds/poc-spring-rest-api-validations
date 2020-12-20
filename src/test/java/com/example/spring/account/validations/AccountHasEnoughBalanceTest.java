package com.example.spring.account.validations;

import com.example.spring.account.Account;
import com.example.spring.validation.ValidationError;
import com.example.spring.validation.ValidationRule;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class AccountHasEnoughBalanceTest {

    @Test
    void testAbsenceOfMoneyReturnsError() {
        Account account = new Account();
        account.setBalance(BigDecimal.ZERO);

        ValidationRule rule = new AccountHasEnoughBalance(account);
        Optional<ValidationError> error = rule.validate();

        assertTrue(error.isPresent());
        assertEquals("balance", error.get().getField());
        assertEquals("Account.Operation.Draw.hasEnoughBalance", error.get().getMessage());
        assertEquals(0, error.get().getMetadata().size());
    }

    @Test
    void testPositiveBalanceDoesNotReturnsError() {
        Account account = new Account();
        account.setBalance(BigDecimal.ONE);

        ValidationRule rule = new AccountHasEnoughBalance(account);
        Optional<ValidationError> error = rule.validate();

        assertTrue(error.isEmpty());
    }
}