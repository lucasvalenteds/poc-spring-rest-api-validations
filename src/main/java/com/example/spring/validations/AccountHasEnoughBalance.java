package com.example.spring.validations;

import com.example.spring.account.Account;
import com.example.spring.validation.ValidationError;
import com.example.spring.validation.ValidationRule;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public final class AccountHasEnoughBalance implements ValidationRule {

    private final Account account;

    public AccountHasEnoughBalance(Account account) {
        this.account = account;
    }

    @Override
    public Optional<ValidationError> validate() {
        if (account.getBalance().compareTo(BigDecimal.ZERO) <= 0) {
            return Optional.of(new ValidationError(
                "balance",
                "Account.Operation.Draw.hasEnoughBalance",
                List.of()
            ));
        }

        return Optional.empty();
    }
}
