package com.bankati.wallet.service;

import com.bankati.wallet.proxy.CurrencyExchangeClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Value;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class CurrencyExchangeServiceTest {

    @Autowired
    private CurrencyExchangeService currencyExchangeService;

    @Value("${api.exchangerates.access_key}")
    private String accessKey;

    @Test
    public void testGetExchangeRate() {
        // Appel réel à l'API
        Double rate = currencyExchangeService.getExchangeRate("MAD", "EUR");

        // Vérification que le taux est retourné
        assertNotNull(rate);
        System.out.println("Exchange rate MAD to EUR: " + rate);
    }
}
