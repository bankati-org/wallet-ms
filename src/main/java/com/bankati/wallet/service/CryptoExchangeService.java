package com.bankati.wallet.service;

import com.bankati.wallet.proxy.CoinMarketCapClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CryptoExchangeService {

    private final CoinMarketCapClient coinMarketCapClient;

    @Value("${api.coinmarketcap.api.key}")
    private String apiKey;

    public CryptoExchangeService(CoinMarketCapClient coinMarketCapClient) {
        this.coinMarketCapClient = coinMarketCapClient;
    }

    public Double getCryptoPrice(String cryptoSymbol, String fiatCurrency) {
        try {
            Map<String, Object> response = coinMarketCapClient.getCryptoPrice(cryptoSymbol, fiatCurrency, apiKey);
            Map<String, Object> data = (Map<String, Object>) response.get("data");
            Map<String, Object> cryptoData = (Map<String, Object>) data.get(cryptoSymbol.toUpperCase());
            Map<String, Object> quote = (Map<String, Object>) cryptoData.get("quote");
            Map<String, Object> fiatData = (Map<String, Object>) quote.get(fiatCurrency.toUpperCase());
            return (Double) fiatData.get("price");
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch crypto price", e);
        }
    }

    public List<Map<String, Object>> getCryptoPrices(List<String> cryptoSymbols, String fiatCurrency) {
        List<Map<String, Object>> prices = new ArrayList<>();

        for (String cryptoSymbol : cryptoSymbols) {
            try {
                Double price = getCryptoPrice(cryptoSymbol, fiatCurrency);
                prices.add(Map.of(
                        "symbol", cryptoSymbol,
                        "price", price
                ));
            } catch (Exception e) {
                // Log an error and skip this symbol
                System.err.println("Error fetching price for: " + cryptoSymbol + " - " + e.getMessage());
            }
        }

        return prices;
    }

}
