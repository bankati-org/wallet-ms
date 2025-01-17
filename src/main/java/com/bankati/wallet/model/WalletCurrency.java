package com.bankati.wallet.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
public class WalletCurrency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "wallet_id")
    @JsonIgnore
    private Wallet wallet;
    private String currencyCode; // EUR, USD, BTC, ETH, etc.
    private Double balance;



    @Enumerated(EnumType.STRING)
    private CurrencyType currencyType=CurrencyType.FIAT; // CRYPTO ou FIAT


    public WalletCurrency(Long id, Wallet wallet, String currencyCode, Double balance, CurrencyType currencyType) {
        this.id = id;
        this.wallet = wallet;
        this.currencyCode = currencyCode;
        this.balance = balance;
        this.currencyType = currencyType;
    }

    public WalletCurrency() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public CurrencyType getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(CurrencyType currencyType) {
        this.currencyType = currencyType;
    }
}

