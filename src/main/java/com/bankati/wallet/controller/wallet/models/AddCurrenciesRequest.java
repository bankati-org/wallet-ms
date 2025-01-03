package com.bankati.wallet.controller.wallet.models;

import jakarta.validation.constraints.NotNull;

import java.util.HashSet;

public class AddCurrenciesRequest {
    @NotNull
    private HashSet<String> currencies;

    public AddCurrenciesRequest(HashSet<String> currencies) {
        this.currencies = currencies;
    }

    public AddCurrenciesRequest() {
    }

    public @NotNull HashSet<String> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(@NotNull HashSet<String> currencies) {
        this.currencies = currencies;
    }
}
