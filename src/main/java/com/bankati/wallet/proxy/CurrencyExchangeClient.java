package com.bankati.wallet.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient(name = "currency-exchange", url = "https://v6.exchangerate-api.com/v6")
public interface CurrencyExchangeClient {

    @GetMapping("/${api.exchangerates.access_key}/latest/{base_code}")
    Map<String, Object> getExchangeRates(@PathVariable("base_code") String baseCurrency);
}


