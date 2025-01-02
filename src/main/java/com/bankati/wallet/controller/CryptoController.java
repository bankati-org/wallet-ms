package com.bankati.wallet.controller;

import com.bankati.wallet.service.CryptoExchangeService;
import com.bankati.wallet.service.WalletService;
import com.bankati.wallet.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
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

    // Acheter de la crypto
    @PostMapping("/buy")
    public String buyCrypto(@RequestParam Long userId,
                            @RequestParam String cryptoSymbol,
                            @RequestParam Double fiatAmount,
                            @RequestParam String fiatCurrency) {
        try {
            walletService.buyCrypto(userId, cryptoSymbol, fiatAmount, fiatCurrency);
            return "Crypto acheté avec succès!";
        } catch (Exception e) {
            return "Erreur lors de l'achat de crypto: " + e.getMessage();
        }
    }

    // Vendre de la crypto
    @PostMapping("/sell")
    public String sellCrypto(@RequestParam Long userId,
                             @RequestParam String cryptoSymbol,
                             @RequestParam Double cryptoAmount,
                             @RequestParam String fiatCurrency) {
        try {
            walletService.sellCrypto(userId, cryptoSymbol, cryptoAmount, fiatCurrency);
            return "Crypto vendue avec succès!";
        } catch (Exception e) {
            return "Erreur lors de la vente de crypto: " + e.getMessage();
        }
    }

    // Transférer des cryptos entre utilisateurs
    @PostMapping("/transfer")
    public String transferCrypto(@RequestParam Long fromUserId,
                                 @RequestParam Long toUserId,
                                 @RequestParam String cryptoSymbol,
                                 @RequestParam Double cryptoAmount) {
        try {
            walletService.transferCrypto(fromUserId, toUserId, cryptoSymbol, cryptoAmount);
            return "Crypto transférée avec succès!";
        } catch (Exception e) {
            return "Erreur lors du transfert de crypto: " + e.getMessage();
        }
    }

    // Récupérer l'historique des transactions d'un utilisateur
    @GetMapping("/transactions/{userId}")
    public List<Transaction> getTransactions(@PathVariable Long userId) {
        return walletService.getCryptoTransactionsByUserId(userId);
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
    public String transferCryptoToFiat(@RequestParam Long userId,
                                       @RequestParam String cryptoSymbol,
                                       @RequestParam Double cryptoAmount,
                                       @RequestParam String fiatCurrency) {
        try {
            walletService.transferCryptoToFiat(userId, cryptoSymbol, cryptoAmount, fiatCurrency);
            return "Transfert de crypto en argent classique effectué avec succès!";
        } catch (Exception e) {
            return "Erreur lors du transfert de crypto en argent classique: " + e.getMessage();
        }
    }


    @GetMapping("/cryptoPrices")
    public List<Map<String, Object>> getCryptoPrices(
            @RequestParam List<String> symbols,
            @RequestParam String fiatCurrency) {
        return cryptoExchangeService.getCryptoPrices(symbols, fiatCurrency);
    }

}
