package com.bankati.wallet.controller.wallet;

import com.bankati.wallet.controller.wallet.models.*;
import com.bankati.wallet.model.Transaction;
import com.bankati.wallet.model.Wallet;
import com.bankati.wallet.service.CurrencyExchangeService;
import com.bankati.wallet.service.WalletService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/v1/wallets")
public class WalletController {

    @Autowired
    private WalletService walletService;
    @Autowired
    private CurrencyExchangeService currencyExchangeService;

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

    @GetMapping("/transactions/{userId}")
    public ResponseEntity<List<Transaction>> getTransactions(@PathVariable Long userId) {
        List<Transaction> transactions = walletService.getFiatTransactionsByUserId(userId);
        return ResponseEntity.ok(transactions);
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

    @GetMapping("/exchangeRates/{currency}")
    public ResponseEntity<?> getExchangeRates(@NotNull @PathVariable String currency) throws Exception {
        return ResponseEntity.ok(currencyExchangeService.getExchangeRates(currency));

    }

//
//    // Endpoint pour récupérer l'historique des taux de change
//    @GetMapping("/currency-history")
//    public Map<String, Object> getCurrencyHistory(@RequestParam("currency") String baseCurrency,
//                                                  @RequestParam("start_date") String startDate,
//                                                  @RequestParam("end_date") String endDate) {
//        return currencyExchangeService.getExchangeRateHistory(baseCurrency, startDate, endDate);
//    }
}
