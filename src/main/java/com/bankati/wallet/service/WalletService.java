package com.bankati.wallet.service;

import com.bankati.wallet.exception.WalletAlreadyExistsException;
import com.bankati.wallet.model.*;
import com.bankati.wallet.repository.TransactionRepository;
import com.bankati.wallet.repository.WalletRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;

@Service
public class WalletService {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CurrencyExchangeService currencyExchangeService;

    public Wallet getWalletByUserId(Long userId) {
        return walletRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Wallet not found for user: " + userId));
    }


    @Transactional
    public Wallet createWallet(Long userId, String defaultCurrency) throws WalletAlreadyExistsException {
        if (walletRepository.existsByUserId(userId)) {
            throw new WalletAlreadyExistsException("User already has a wallet");
        }

        Wallet wallet = new Wallet();
        wallet.setUserId(userId);
        wallet.setCurrencies(new HashSet<>());

        WalletCurrency defaultWalletCurrency = new WalletCurrency();
        defaultWalletCurrency.setWallet(wallet);
        defaultWalletCurrency.setCurrencyCode(defaultCurrency);
        defaultWalletCurrency.setBalance(0.0);
        wallet.getCurrencies().add(defaultWalletCurrency);  // Using the new helper method

        return walletRepository.save(wallet);
    }

    public void creditWallet(Long userId, String currency, Double amount) {
        Wallet wallet = getWalletByUserId(userId);
        WalletCurrency walletCurrency = getOrCreateWalletCurrency(wallet, currency);
        walletCurrency.setBalance(walletCurrency.getBalance() + amount);

        Transaction transaction = new Transaction();
        transaction.setWallet(wallet);
        transaction.setType("CREDIT");
        transaction.setAmount(amount);
        transaction.setCurrency(currency);
        transaction.setTimestamp(LocalDateTime.now());

        transactionRepository.save(transaction);
        walletRepository.save(wallet);
    }

    public void debitWallet(Long userId, String currency, Double amount) {
        Wallet wallet = getWalletByUserId(userId);
        WalletCurrency walletCurrency = wallet.getCurrencies().stream()
                .filter(c -> c.getCurrencyCode().equals(currency))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Currency " + currency + " not found in wallet"));

        if (walletCurrency.getBalance() < amount) {
            throw new RuntimeException("Insufficient funds in " + currency);
        }

        walletCurrency.setBalance(walletCurrency.getBalance() - amount);

        Transaction transaction = new Transaction();
        transaction.setWallet(wallet);
        transaction.setType("DEBIT");
        transaction.setAmount(amount);
        transaction.setCurrency(currency);
        transaction.setTimestamp(LocalDateTime.now());

        transactionRepository.save(transaction);
        walletRepository.save(wallet);
    }

    public void transfer(Long fromUserId, Long toUserId, String currency, Double amount) {
        debitWallet(fromUserId, currency, amount);
        creditWallet(toUserId, currency, amount);
    }

    @Transactional
    public void transferMultiCurrency(Long fromUserId, String fromCurrency, Long toUserId, String toCurrency, Double amount) {
        Double exchangeRate = currencyExchangeService.getExchangeRate(fromCurrency, toCurrency);
        Double convertedAmount = amount * exchangeRate;

        debitWalletWithAutoConversion(fromUserId, fromCurrency, amount);
        creditWallet(toUserId, toCurrency, convertedAmount);

    }

    public Double getBalance(Long userId, String currency) {
        Wallet wallet = getWalletByUserId(userId);
        return wallet.getCurrencies().stream()
                .filter(c -> c.getCurrencyCode().equals(currency))
                .findFirst()
                .map(WalletCurrency::getBalance)
                .orElse(0.0);
    }

    private WalletCurrency getOrCreateWalletCurrency(Wallet wallet, String currency) {
        return wallet.getCurrencies().stream()
                .filter(c -> c.getCurrencyCode().equals(currency))
                .findFirst()
                .orElseGet(() -> {
                    WalletCurrency newCurrency = new WalletCurrency();
                    newCurrency.setWallet(wallet);
                    newCurrency.setCurrencyCode(currency);
                    newCurrency.setBalance(0.0);
                    wallet.getCurrencies().add(newCurrency);
                    return newCurrency;
                });
    }

    public void addCurrenciesToWallet(Long userId, HashSet<String> currencies) {
        Wallet wallet = getWalletByUserId(userId);

        for (String currency : currencies) {
            // Add currency if it doesn't already exist
            getOrCreateWalletCurrency(wallet, currency);
        }

        walletRepository.save(wallet);
    }

    public void debitWalletWithAutoConversion(Long userId, String currency, Double amount) {
        Wallet wallet = getWalletByUserId(userId);

        // Check if the balance in the requested currency is sufficient
        WalletCurrency walletCurrency = wallet.getCurrencies().stream()
                .filter(c -> c.getCurrencyCode().equals(currency))
                .findFirst()
                .orElse(null);

        if (walletCurrency != null && walletCurrency.getBalance() >= amount) {
            // Sufficient balance in the requested currency
            walletCurrency.setBalance(walletCurrency.getBalance() - amount);
        } else {
            // Insufficient funds in the requested currency, try other currencies
            double remainingAmount = amount - (walletCurrency != null ? walletCurrency.getBalance() : 0);
            if (walletCurrency != null) walletCurrency.setBalance(0.0);

            for (WalletCurrency otherCurrency : wallet.getCurrencies()) {
                if (!otherCurrency.getCurrencyCode().equals(currency) && remainingAmount > 0) {
                    double exchangeRate = currencyExchangeService.getExchangeRate(otherCurrency.getCurrencyCode(), currency);
                    double equivalentAmount = remainingAmount / exchangeRate;

                    if (otherCurrency.getBalance() >= equivalentAmount) {
                        otherCurrency.setBalance(otherCurrency.getBalance() - equivalentAmount);
                        remainingAmount = 0;
                    } else {
                        remainingAmount -= otherCurrency.getBalance() * exchangeRate;
                        otherCurrency.setBalance(0.0);
                    }
                }
            }

            if (remainingAmount > 0) {
                throw new RuntimeException("Insufficient funds across all currencies to complete the transaction");
            }
        }

        // Save the updated wallet
        walletRepository.save(wallet);

        // Log the transaction
        Transaction transaction = new Transaction();
        transaction.setWallet(wallet);
        transaction.setType("DEBIT");
        transaction.setAmount(amount);
        transaction.setCurrency(currency);
        transaction.setTimestamp(LocalDateTime.now());
        transactionRepository.save(transaction);
    }


}