package com.bankati.wallet.controller.wallet.models;

import com.bankati.wallet.model.CurrencyType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class TransferRequest {

    @NotNull(message = "From User ID is required")
    private Long fromUserId;

    @NotNull(message = "To User ID is required")
    private Long toUserId;

    @NotNull(message = "Currency is required")
    private String currency;

    @Positive(message = "Amount must be positive")
    private Double amount;



    @NotNull
    private CurrencyType currencyType;

    public TransferRequest(Long fromUserId, Long toUserId, String currency, Double amount, CurrencyType currencyType) {
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.currency = currency;
        this.amount = amount;
        this.currencyType = currencyType;
    }

    public TransferRequest() {
    }

    public @NotNull(message = "From User ID is required") Long getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(@NotNull(message = "From User ID is required") Long fromUserId) {
        this.fromUserId = fromUserId;
    }

    public @NotNull(message = "To User ID is required") Long getToUserId() {
        return toUserId;
    }

    public void setToUserId(@NotNull(message = "To User ID is required") Long toUserId) {
        this.toUserId = toUserId;
    }

    public @NotNull(message = "Currency is required") String getCurrency() {
        return currency;
    }

    public void setCurrency(@NotNull(message = "Currency is required") String currency) {
        this.currency = currency;
    }

    public @Positive(message = "Amount must be positive") Double getAmount() {
        return amount;
    }

    public void setAmount(@Positive(message = "Amount must be positive") Double amount) {
        this.amount = amount;
    }

    public @NotNull CurrencyType getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(@NotNull CurrencyType currencyType) {
        this.currencyType = currencyType;
    }
}
