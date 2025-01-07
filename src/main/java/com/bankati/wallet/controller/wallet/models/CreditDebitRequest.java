package com.bankati.wallet.controller.wallet.models;

import com.bankati.wallet.model.CurrencyType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class CreditDebitRequest {

    @NotNull
    private String currency;

    @Positive
    private Double amount;



    @NotNull
    private CurrencyType currencyType; // CRYPTO ou FIAT

    public CreditDebitRequest(String currency, Double amount, CurrencyType currencyType) {
        this.currency = currency;
        this.amount = amount;
        this.currencyType = currencyType;
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

    public @NotNull CurrencyType getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(@NotNull CurrencyType currencyType) {
        this.currencyType = currencyType;
    }

}