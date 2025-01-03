package com.bankati.wallet.controller.crypto.models;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class SellCryptoRequest {
    @NotNull
    private Long userId;

    @NotNull
    private String cryptoSymbol;

    @Positive
    private Double cryptoAmount;

    @NotNull
    private String fiatCurrency;

    public SellCryptoRequest(Long userId, String fiatCurrency, Double cryptoAmount, String cryptoSymbol) {
        this.userId = userId;
        this.fiatCurrency = fiatCurrency;
        this.cryptoAmount = cryptoAmount;
        this.cryptoSymbol = cryptoSymbol;
    }

    public SellCryptoRequest() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getCryptoSymbol() {
        return cryptoSymbol;
    }

    public void setCryptoSymbol(String cryptoSymbol) {
        this.cryptoSymbol = cryptoSymbol;
    }

    public @Positive Double getCryptoAmount() {
        return cryptoAmount;
    }

    public void setCryptoAmount(@Positive Double cryptoAmount) {
        this.cryptoAmount = cryptoAmount;
    }

    public String getFiatCurrency() {
        return fiatCurrency;
    }

    public void setFiatCurrency(String fiatCurrency) {
        this.fiatCurrency = fiatCurrency;
    }
}
