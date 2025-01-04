package com.bankati.wallet.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "currency-exchange", url = "https://v6.exchangerate-api.com/v6")
public interface CurrencyExchangeClient {

    @GetMapping("/${api.exchangerates.access_key}/latest/{base_code}")
    Map<String, Object> getExchangeRates(@PathVariable("base_code") String baseCurrency);

    // Ajout de l'endpoint pour récupérer l'historique des taux de change
    @GetMapping("/${api.exchangerates.access_key}/history/{base_code}")
    Map<String, Object> getExchangeRateHistory(@PathVariable("base_code") String baseCurrency,
                                               @RequestParam("start_date") String startDate,
                                               @RequestParam("end_date") String endDate);
}


