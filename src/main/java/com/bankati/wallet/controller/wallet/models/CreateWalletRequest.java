package com.bankati.wallet.controller.wallet.models;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


public class CreateWalletRequest {

    @NotNull
    private Long userId;

    @NotNull
    @Size(min = 3, max = 3)
    private String defaultCurrency;

    public CreateWalletRequest(Long userId, String defaultCurrency) {
        this.userId = userId;
        this.defaultCurrency = defaultCurrency;
    }

    public CreateWalletRequest() {
    }

    public @NotNull Long getUserId() {
        return userId;
    }

    public void setUserId(@NotNull Long userId) {
        this.userId = userId;
    }

    public @NotNull @Size(min = 3, max = 3) String getDefaultCurrency() {
        return defaultCurrency;
    }

    public void setDefaultCurrency(@NotNull @Size(min = 3, max = 3) String defaultCurrency) {
        this.defaultCurrency = defaultCurrency;
    }
}
