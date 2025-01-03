package com.bankati.wallet.controller.wallet;

import com.bankati.wallet.controller.wallet.models.*;
import com.bankati.wallet.model.Wallet;
import com.bankati.wallet.service.WalletService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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
    public ResponseEntity<?> createWallet(@Valid @RequestBody CreateWalletRequest request) {
        // Si l'exception WalletAlreadyExistsException est lancée, elle sera capturée par le GlobalExceptionHandler
        Wallet wallet = walletService.createWallet(request.getUserId(), request.getDefaultCurrency());
        return ResponseEntity.ok(wallet);
    }


    @GetMapping("/{userId}/balance/{currency}")
    public ResponseEntity<Double> getBalance(
            @PathVariable Long userId,
            @PathVariable String currency) {
        Double balance = walletService.getBalance(userId, currency);
        return ResponseEntity.ok(balance);
    }

    @PostMapping("/{userId}/credit")
    public ResponseEntity<Void> creditWallet(@PathVariable Long userId, @Valid @RequestBody CreditDebitRequest request) {
        walletService.creditWallet(userId, request.getCurrency(), request.getAmount());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{userId}/debit")
    public ResponseEntity<Void> debitWallet(@PathVariable Long userId, @Valid @RequestBody CreditDebitRequest request) {
        walletService.debitWallet(userId, request.getCurrency(), request.getAmount());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/transfer")
    public ResponseEntity<Void> transfer(@Valid @RequestBody TransferRequest request) {
        walletService.transfer(request.getFromUserId(), request.getToUserId(), request.getCurrency(), request.getAmount());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/transfer/multi-currency")
    public ResponseEntity<Void> transferMultiCurrency(@Valid @RequestBody TransferMultiCurrencyRequest request) {
        walletService.transferMultiCurrency(request.getFromUserId(), request.getFromCurrency(), request.getToUserId(), request.getToCurrency(), request.getAmount());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{userId}/add-currencies")
    public ResponseEntity<?> addCurrenciesToWallet(@PathVariable Long userId, @Valid @RequestBody AddCurrenciesRequest request) {
        walletService.addCurrenciesToWallet(userId, request.getCurrencies());
        return ResponseEntity.ok("Currencies added successfully");

    }
}
