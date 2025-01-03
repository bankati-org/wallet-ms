package com.bankati.wallet.controller.crypto;

import com.bankati.wallet.controller.crypto.models.BuyCryptoRequest;
import com.bankati.wallet.controller.crypto.models.SellCryptoRequest;
import com.bankati.wallet.controller.crypto.models.TransferCryptoRequest;
import com.bankati.wallet.controller.crypto.models.TransferToFiatRequest;
import com.bankati.wallet.model.Transaction;
import com.bankati.wallet.service.CryptoExchangeService;
import com.bankati.wallet.service.WalletService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/crypto")
public class CryptoController {

    @Autowired
    private WalletService walletService;
    @Autowired
    private CryptoExchangeService cryptoExchangeService;

    @PostMapping("/buy")
    public ResponseEntity<String> buyCrypto(@RequestBody @Valid BuyCryptoRequest request) {

        walletService.buyCrypto(
                request.getUserId(),
                request.getCryptoSymbol(),
                request.getFiatAmount(),
                request.getFiatCurrency()
        );
        return ResponseEntity.ok("Crypto acheté avec succès!");

    }

    @PostMapping("/sell")
    public ResponseEntity<String> sellCrypto(@RequestBody @Valid SellCryptoRequest request) {

        walletService.sellCrypto(
                request.getUserId(),
                request.getCryptoSymbol(),
                request.getCryptoAmount(),
                request.getFiatCurrency()
        );
        return ResponseEntity.ok("Crypto vendue avec succès!");

    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transferCrypto(@RequestBody @Valid TransferCryptoRequest request) {

        walletService.transferCrypto(
                request.getFromUserId(),
                request.getToUserId(),
                request.getCryptoSymbol(),
                request.getCryptoAmount()
        );
        return ResponseEntity.ok("Crypto transférée avec succès!");

    }


    @GetMapping("/transactions/{userId}")
    public ResponseEntity<List<Transaction>> getTransactions(@PathVariable Long userId) {
        List<Transaction> transactions = walletService.getCryptoTransactionsByUserId(userId);
        return ResponseEntity.ok(transactions);
    }

    // Récupérer le solde d'une crypto dans un portefeuille
    @GetMapping("/balance")
    public Double getBalance(@RequestParam Long userId,
                             @RequestParam String currency) {
        return walletService.getBalance(userId, currency);
    }

    // Récupérer le taux de change actuel pour une crypto
    @GetMapping("/price")
    public Double getCryptoPrice(@RequestParam String cryptoSymbol,
                                 @RequestParam String fiatCurrency) {
        return cryptoExchangeService.getCryptoPrice(cryptoSymbol, fiatCurrency);
    }

    @PostMapping("/transfer-to-fiat")
    public ResponseEntity<String> transferCryptoToFiat(@RequestBody @Valid TransferToFiatRequest request) {

        walletService.transferCryptoToFiat(
                request.getUserId(),
                request.getCryptoSymbol(),
                request.getCryptoAmount(),
                request.getFiatCurrency()
        );
        return ResponseEntity.ok("Transfert de crypto en argent classique effectué avec succès!");

    }


    @GetMapping("/cryptoPrices")
    public List<Map<String, Object>> getCryptoPrices(
            @RequestParam List<String> symbols,
            @RequestParam String fiatCurrency) {
        return cryptoExchangeService.getCryptoPrices(symbols, fiatCurrency);
    }

}
