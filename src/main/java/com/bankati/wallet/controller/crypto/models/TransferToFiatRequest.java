package com.bankati.wallet.controller.crypto.models;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;


public class TransferToFiatRequest {
    @NotNull
    private Long userId;

    @NotNull
    private String cryptoSymbol;

    @Positive
    private Double cryptoAmount;

    @NotNull
    private String fiatCurrency;

    public TransferToFiatRequest(Long userId, String cryptoSymbol, Double cryptoAmount, String fiatCurrency) {
        this.userId = userId;
        this.cryptoSymbol = cryptoSymbol;
        this.cryptoAmount = cryptoAmount;
        this.fiatCurrency = fiatCurrency;
    }

    public TransferToFiatRequest() {
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

    public @Positive Double getCryptoAmount() {
        return cryptoAmount;
    }

    public void setCryptoAmount(@Positive Double cryptoAmount) {
        this.cryptoAmount = cryptoAmount;
    }

    public @NotNull String getFiatCurrency() {
        return fiatCurrency;
    }

    public void setFiatCurrency(@NotNull String fiatCurrency) {
        this.fiatCurrency = fiatCurrency;
    }
}
