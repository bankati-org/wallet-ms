package com.bankati.wallet.controller.wallet.models;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class CreditDebitRequest {

    @NotNull
    private String currency;

    @Positive
    private Double amount;

    public CreditDebitRequest(String currency, Double amount) {
        this.currency = currency;
        this.amount = amount;
    }

    public CreditDebitRequest() {
    }

    public @NotNull String getCurrency() {
        return currency;
    }

    public void setCurrency(@NotNull String currency) {
        this.currency = currency;
    }

    public @Positive Double getAmount() {
        return amount;
    }

    public void setAmount(@Positive Double amount) {
        this.amount = amount;
    }
}