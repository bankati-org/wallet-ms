package com.bankati.wallet.service;

import com.bankati.wallet.exception.CurrencyNotFoundException;
import com.bankati.wallet.exception.InsufficientFundsException;
import com.bankati.wallet.exception.WalletAlreadyExistsException;
import com.bankati.wallet.exception.WalletNotFoundException;
import com.bankati.wallet.model.CurrencyType;
import com.bankati.wallet.model.Transaction;
import com.bankati.wallet.model.Wallet;
import com.bankati.wallet.model.WalletCurrency;
import com.bankati.wallet.repository.TransactionRepository;
import com.bankati.wallet.repository.WalletRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WalletService {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CurrencyExchangeService currencyExchangeService;

    @Autowired
    private CryptoExchangeService cryptoExchangeService;

    public Wallet getWalletByUserId(Long userId) {
        return walletRepository.findByUserId(userId)
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found for user: " + userId));
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

    public List<WalletCurrency> getCryptoWalletsByUserId(Long userId) {
        Wallet wallet = getWalletByUserId(userId);
        return wallet.getCurrencies().stream()
                .filter(currency -> currency.getCurrencyType() == CurrencyType.CRYPTO)
                .collect(Collectors.toList());
    }

    public List<WalletCurrency> getFiatWalletsByUserId(Long userId) {
        Wallet wallet = getWalletByUserId(userId);
        return wallet.getCurrencies().stream()
                .filter(currency -> currency.getCurrencyType() == CurrencyType.FIAT)
                .collect(Collectors.toList());
    }



    public void debitWallet(Long userId, String currency, Double amount, CurrencyType currencyType) {
        Wallet wallet = getWalletByUserId(userId);
        WalletCurrency walletCurrency = getOrCreateWalletCurrency(wallet, currency, currencyType);
        walletCurrency.setBalance(walletCurrency.getBalance() + amount);

        Transaction transaction = new Transaction();
        transaction.setWallet(wallet);
        transaction.setType("DEBIT");
        transaction.setAmount(amount);
        transaction.setCurrency(currency);
        transaction.setCurrencyType(isCryptoCurrency(currency) ? CurrencyType.CRYPTO : CurrencyType.FIAT);
        transaction.setTimestamp(LocalDateTime.now());

        transactionRepository.save(transaction);
        walletRepository.save(wallet);
    }

    public void creditWallet(Long userId, String currency, Double amount) {
        Wallet wallet = getWalletByUserId(userId);
        WalletCurrency walletCurrency = wallet.getCurrencies().stream()
                .filter(c -> c.getCurrencyCode().equals(currency))
                .findFirst()
                .orElseThrow(() -> new CurrencyNotFoundException("Currency " + currency + " not found in wallet"));


        if (walletCurrency.getBalance() < amount) {
            throw new InsufficientFundsException("Insufficient funds in " + currency);
        }

        walletCurrency.setBalance(walletCurrency.getBalance() - amount);

        Transaction transaction = new Transaction();
        transaction.setWallet(wallet);
        transaction.setType("CREDIT");
        transaction.setAmount(amount);
        transaction.setCurrency(currency);
        transaction.setCurrencyType(isCryptoCurrency(currency) ? CurrencyType.CRYPTO : CurrencyType.FIAT);
        transaction.setTimestamp(LocalDateTime.now());

        transactionRepository.save(transaction);
        walletRepository.save(wallet);
    }

    public void transfer(Long fromUserId, Long toUserId, String currency, Double amount, CurrencyType currencyType) {
        creditWallet(fromUserId, currency, amount);
        debitWallet(toUserId, currency, amount, currencyType);
    }

    @Transactional
    public void transferMultiCurrency(Long fromUserId, String fromCurrency, Long toUserId, String toCurrency, Double amount, CurrencyType currencyType) {
        Double exchangeRate = currencyExchangeService.getExchangeRate(fromCurrency, toCurrency);
        Double convertedAmount = amount * exchangeRate;

        creditWallet(fromUserId, fromCurrency, amount);
        debitWallet(toUserId, toCurrency, convertedAmount, currencyType);

    }

    public Double getBalance(Long userId, String currency) {
        Wallet wallet = getWalletByUserId(userId);
        return wallet.getCurrencies().stream()
                .filter(c -> c.getCurrencyCode().equals(currency))
                .findFirst()
                .map(WalletCurrency::getBalance)
                .orElse(0.0);
    }

    private WalletCurrency getOrCreateWalletCurrency(Wallet wallet, String currency, CurrencyType currencyType) {
        return wallet.getCurrencies().stream()
                .filter(c -> c.getCurrencyCode().equals(currency))
                .findFirst()
                .orElseGet(() -> {
                    WalletCurrency newCurrency = new WalletCurrency();
                    newCurrency.setWallet(wallet);
                    newCurrency.setCurrencyCode(currency);
                    newCurrency.setBalance(0.0);
                    newCurrency.setCurrencyType(currencyType);
                    wallet.getCurrencies().add(newCurrency);
                    return newCurrency;
                });
    }

//    public void addCurrenciesToWallet(Long userId, HashSet<String> currencies, CurrencyType currencyType) {
//        Wallet wallet = getWalletByUserId(userId);
//
//        for (String currency : currencies) {
//            // Add currency if it doesn't already exist
//            getOrCreateWalletCurrency(wallet, currency);
//        }
//
//        walletRepository.save(wallet);
//    }

    public void creditWalletWithAutoConversion(Long userId, String currency, Double amount) {
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
                throw new InsufficientFundsException("Insufficient funds across all currencies to complete the transaction");
            }
        }

        // Save the updated wallet
        walletRepository.save(wallet);

        // Log the transaction
        Transaction transaction = new Transaction();
        transaction.setWallet(wallet);
        transaction.setType("CREDIT");
        transaction.setAmount(amount);
        transaction.setCurrency(currency);
        transaction.setTimestamp(LocalDateTime.now());
        transactionRepository.save(transaction);
    }

    public void buyCrypto(Long userId, String cryptoSymbol, Double fiatAmount, String fiatCurrency) {
        Double cryptoPrice = cryptoExchangeService.getCryptoPrice(cryptoSymbol, fiatCurrency);
        Double cryptoAmount = fiatAmount / cryptoPrice;

        debitWallet(userId, cryptoSymbol, cryptoAmount, CurrencyType.CRYPTO);

        Transaction transaction = new Transaction();
        transaction.setWallet(getWalletByUserId(userId));
        transaction.setType("BUY_CRYPTO");
        transaction.setAmount(cryptoAmount);
        transaction.setCurrency(cryptoSymbol);
        transaction.setCurrencyType(CurrencyType.CRYPTO);
        transaction.setTimestamp(LocalDateTime.now());
        transactionRepository.save(transaction);
    }

    public void sellCrypto(Long userId, String cryptoSymbol, Double cryptoAmount, String fiatCurrency) {
        Double cryptoPrice = cryptoExchangeService.getCryptoPrice(cryptoSymbol, fiatCurrency);
        Double fiatAmount = cryptoAmount * cryptoPrice;

        creditWallet(userId, cryptoSymbol, cryptoAmount);
        debitWallet(userId, fiatCurrency, fiatAmount, CurrencyType.CRYPTO);

        Transaction transaction = new Transaction();
        transaction.setWallet(getWalletByUserId(userId));
        transaction.setType("SELL_CRYPTO");
        transaction.setAmount(fiatAmount);
        transaction.setCurrency(fiatCurrency);
        transaction.setCurrencyType(CurrencyType.CRYPTO);
        transaction.setTimestamp(LocalDateTime.now());
        transactionRepository.save(transaction);
    }

    public void transferCrypto(Long fromUserId, Long toUserId, String cryptoSymbol, Double cryptoAmount) {
        creditWallet(fromUserId, cryptoSymbol, cryptoAmount);
        debitWallet(toUserId, cryptoSymbol, cryptoAmount, CurrencyType.CRYPTO);

        Transaction transaction = new Transaction();
        transaction.setWallet(getWalletByUserId(fromUserId));
        transaction.setType("TRANSFER_CRYPTO");
        transaction.setAmount(cryptoAmount);
        transaction.setCurrency(cryptoSymbol);
        transaction.setCurrencyType(CurrencyType.CRYPTO);
        transaction.setTimestamp(LocalDateTime.now());
        transactionRepository.save(transaction);
    }


    public List<Transaction> getCryptoTransactionsByUserId(Long userId) {
        Wallet wallet = getWalletByUserId(userId);
        return wallet.getTransactions().stream()
                .filter(transaction -> transaction.getCurrencyType() == CurrencyType.CRYPTO)
                .collect(Collectors.toList());
    }

    public List<Transaction> getFiatTransactionsByUserId(Long userId) {
        Wallet wallet = getWalletByUserId(userId);
        return wallet.getTransactions().stream()
                .filter(transaction -> transaction.getCurrencyType() == CurrencyType.FIAT)
                .collect(Collectors.toList());
    }


    public void transferCryptoToFiat(Long userId, String cryptoSymbol, Double cryptoAmount, String fiatCurrency) {
        // Obtenir le prix actuel de la cryptomonnaie
        Double cryptoPriceInFiat = cryptoExchangeService.getCryptoPrice(cryptoSymbol, fiatCurrency);

        // Calculer le montant en devise fiat
        Double fiatAmount = cryptoAmount * cryptoPriceInFiat;

        // Débiter le portefeuille en cryptomonnaie
        creditWallet(userId, cryptoSymbol, cryptoAmount);

        // Crédite le portefeuille en devise fiat
        debitWallet(userId, fiatCurrency, fiatAmount, CurrencyType.FIAT);

        // Enregistrer la transaction
        Transaction transaction = new Transaction();
        transaction.setWallet(getWalletByUserId(userId));
        transaction.setType("TRANSFER_CRYPTO_TO_FIAT");
        transaction.setAmount(fiatAmount);
        transaction.setCurrency(fiatCurrency);
        transaction.setCurrencyType(CurrencyType.CRYPTO);
        transaction.setTimestamp(LocalDateTime.now());
        transactionRepository.save(transaction);
    }

    private boolean isCryptoCurrency(String currency) {
        // Liste des cryptomonnaies connues, vous pouvez l'étendre
        List<String> cryptoCurrencies = List.of("BTC", "ETH", "USDT", "BNB", "XRP", "ADA", "DOGE");
        return cryptoCurrencies.contains(currency.toUpperCase());
    }


}