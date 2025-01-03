package com.bankati.wallet.controller.crypto.models;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;


public class TransferCryptoRequest {
    @NotNull
    private Long fromUserId;

    @NotNull
    private Long toUserId;

    @NotNull
    private String cryptoSymbol;

    @Positive
    private Double cryptoAmount;

    public TransferCryptoRequest(Long fromUserId, Long toUserId, String cryptoSymbol, Double cryptoAmount) {
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.cryptoSymbol = cryptoSymbol;
        this.cryptoAmount = cryptoAmount;
    }

    public TransferCryptoRequest() {
    }

    public @NotNull Long getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(@NotNull Long fromUserId) {
        this.fromUserId = fromUserId;
    }

    public @NotNull Long getToUserId() {
        return toUserId;
    }

    public void setToUserId(@NotNull Long toUserId) {
        this.toUserId = toUserId;
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
}
