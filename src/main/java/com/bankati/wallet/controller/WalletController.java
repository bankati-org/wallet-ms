package com.bankati.wallet.controller;

import com.bankati.wallet.exception.WalletAlreadyExistsException;
import com.bankati.wallet.model.Wallet;
import com.bankati.wallet.service.WalletService;

import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.constraints.Positive;

import java.util.HashSet;


@RestController
@RequestMapping("/api/v1/wallets")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @GetMapping("/{userId}")
    public ResponseEntity<Wallet> getWallet(@PathVariable Long userId) {
        Wallet wallet = walletService.getWalletByUserId(userId);
        return ResponseEntity.ok(wallet);
    }

    @PostMapping
    public ResponseEntity<?> createWallet(
            @RequestParam @NotNull Long userId,
            @RequestParam @NotNull String defaultCurrency) {
        try {
            Wallet wallet = walletService.createWallet(userId, defaultCurrency);
            return ResponseEntity.ok(wallet);
        }catch (WalletAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping("/{userId}/balance/{currency}")
    public ResponseEntity<Double> getBalance(
            @PathVariable Long userId,
            @PathVariable String currency) {
        Double balance = walletService.getBalance(userId, currency);
        return ResponseEntity.ok(balance);
    }

    @PostMapping("/{userId}/credit")
    public ResponseEntity<Void> creditWallet(
            @PathVariable Long userId,
            @RequestParam @NotNull String currency,
            @RequestParam @Positive Double amount) {
        walletService.creditWallet(userId, currency, amount);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{userId}/debit")
    public ResponseEntity<Void> debitWallet(
            @PathVariable Long userId,
            @RequestParam @NotNull String currency,
            @RequestParam @Positive Double amount) {
        walletService.debitWalletWithAutoConversion(userId, currency, amount);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/transfer")
    public ResponseEntity<Void> transfer(
            @RequestParam @NotNull Long fromUserId,
            @RequestParam @NotNull Long toUserId,
            @RequestParam @NotNull String currency,
            @RequestParam @Positive Double amount) {
        walletService.transfer(fromUserId, toUserId, currency, amount);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/transfer/multi-currency")
    public ResponseEntity<Void> transferMultiCurrency(
            @RequestParam @NotNull Long fromUserId,
            @RequestParam @NotNull String fromCurrency,
            @RequestParam @NotNull Long toUserId,
            @RequestParam @NotNull String toCurrency,
            @RequestParam  Double amount) {
        walletService.transferMultiCurrency(fromUserId, fromCurrency, toUserId, toCurrency, amount);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{userId}/add-currencies")
    public ResponseEntity<?> addCurrenciesToWallet(
            @PathVariable Long userId,
            @RequestBody HashSet<String> currencies) {
        try {
            walletService.addCurrenciesToWallet(userId, currencies);
            return ResponseEntity.ok("Currencies added successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to add currencies: " + e.getMessage());
        }
    }
}
