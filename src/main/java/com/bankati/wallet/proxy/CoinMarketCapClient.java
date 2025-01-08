package com.bankati.wallet.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "coinMarketCapClient", url = "https://pro-api.coinmarketcap.com/v1")
@Service
public interface CoinMarketCapClient {

    @GetMapping("/cryptocurrency/quotes/latest")
    Map<String, Object> getCryptoPrice(@RequestParam("symbol") String cryptoSymbol, @RequestParam("convert") String fiatCurrency, @RequestHeader("X-CMC_PRO_API_KEY") String apiKey);
}
