package com.bankati.wallet.service;

import com.bankati.wallet.proxy.CurrencyExchangeClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CurrencyExchangeService {

    private final CurrencyExchangeClient currencyExchangeClient;

    @Autowired
    public CurrencyExchangeService(CurrencyExchangeClient currencyExchangeClient) {
        this.currencyExchangeClient = currencyExchangeClient;
    }



    public Double getExchangeRate(String fromCurrency, String toCurrency) {
        try {
            Map<String, Object> response = currencyExchangeClient.getExchangeRates(fromCurrency);
            Map<String, Double> rates = (Map<String, Double>) response.get("conversion_rates");
            return rates.get(toCurrency);
        } catch (Exception e) {
            throw new RuntimeException("Unable to fetch exchange rate: " + e.getMessage());
        }
    }
}
