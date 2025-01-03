package com.bankati.wallet.controller.crypto.models;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class BuyCryptoRequest {
    @NotNull
    private Long userId;

    @NotNull
    private String cryptoSymbol;

    @Positive
    private Double fiatAmount;

    @NotNull
    private String fiatCurrency;

    public BuyCryptoRequest(Long userId, String fiatCurrency, Double fiatAmount, String cryptoSymbol) {
        this.userId = userId;
        this.fiatCurrency = fiatCurrency;
        this.fiatAmount = fiatAmount;
        this.cryptoSymbol = cryptoSymbol;
    }

    public BuyCryptoRequest() {
    }

    public @NotNull Long getUserId() {
        return userId;
    }

    public void setUserId(@NotNull Long userId) {
        this.userId = userId;
    }

    public @NotNull String getCryptoSymbol() {
        return cryptoSymbol;
    }

    public void setCryptoSymbol(@NotNull String cryptoSymbol) {
        this.cryptoSymbol = cryptoSymbol;
    }

    public @Positive Double getFiatAmount() {
        return fiatAmount;
    }

    public void setFiatAmount(@Positive Double fiatAmount) {
        this.fiatAmount = fiatAmount;
    }

    public @NotNull String getFiatCurrency() {
        return fiatCurrency;
    }

    public void setFiatCurrency(@NotNull String fiatCurrency) {
        this.fiatCurrency = fiatCurrency;
    }
}
