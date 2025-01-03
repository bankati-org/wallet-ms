package com.bankati.wallet.controller.wallet.models;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class TransferMultiCurrencyRequest {

    @NotNull
    private Long fromUserId;

    @NotNull
    private String fromCurrency;

    @NotNull
    private Long toUserId;

    @NotNull
    private String toCurrency;

    @Positive
    private Double amount;

    public TransferMultiCurrencyRequest(Long fromUserId, String fromCurrency, Long toUserId, String toCurrency, Double amount) {
        this.fromUserId = fromUserId;
        this.fromCurrency = fromCurrency;
        this.toUserId = toUserId;
        this.toCurrency = toCurrency;
        this.amount = amount;
    }

    public TransferMultiCurrencyRequest() {
    }

    public @NotNull Long getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(@NotNull Long fromUserId) {
        this.fromUserId = fromUserId;
    }

    public @NotNull String getFromCurrency() {
        return fromCurrency;
    }

    public void setFromCurrency(@NotNull String fromCurrency) {
        this.fromCurrency = fromCurrency;
    }

    public @NotNull Long getToUserId() {
        return toUserId;
    }

    public void setToUserId(@NotNull Long toUserId) {
        this.toUserId = toUserId;
    }

    public @NotNull String getToCurrency() {
        return toCurrency;
    }

    public void setToCurrency(@NotNull String toCurrency) {
        this.toCurrency = toCurrency;
    }

    public @Positive Double getAmount() {
        return amount;
    }

    public void setAmount(@Positive Double amount) {
        this.amount = amount;
    }
}
