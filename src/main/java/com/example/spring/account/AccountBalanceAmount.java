package com.example.spring.account;

import java.math.BigDecimal;

public final class AccountBalanceAmount {

    private BigDecimal amount;

    public AccountBalanceAmount() {
    }

    public AccountBalanceAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
