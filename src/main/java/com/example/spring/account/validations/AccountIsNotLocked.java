package com.example.spring.account.validations;

import com.example.spring.account.Account;
import com.example.spring.validation.ValidationError;
import com.example.spring.validation.ValidationRule;

import java.util.List;
import java.util.Optional;

public final class AccountIsNotLocked implements ValidationRule {

    private final Account account;

    public AccountIsNotLocked(Account account) {
        this.account = account;
    }

    @Override
    public Optional<ValidationError> validate() {
        if (account.getLocked()) {
            return Optional.of(new ValidationError(
                "locked",
                "Account.Operation.Money.isLocked",
                List.of()
            ));
        }

        return Optional.empty();
    }
}
