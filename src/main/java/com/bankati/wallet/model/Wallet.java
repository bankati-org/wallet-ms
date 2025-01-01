package com.bankati.wallet.model;

import jakarta.persistence.*;

import java.util.List;
import java.util.Set;

@Table(name = "wallets")
@Entity
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @OneToMany(mappedBy = "wallet", cascade = CascadeType.ALL)
    private Set<WalletCurrency> currencies;

    @OneToMany(mappedBy = "wallet", cascade = CascadeType.ALL)
    private List<Transaction> transactions;

    public Wallet(List<Transaction> transactions, Set<WalletCurrency> currencies, Long userId, Long id) {
        this.transactions = transactions;
        this.currencies = currencies;
        this.userId = userId;
        this.id = id;
    }
    public Wallet() {}

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public Set<WalletCurrency> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(Set<WalletCurrency> currencies) {
        this.currencies = currencies;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

