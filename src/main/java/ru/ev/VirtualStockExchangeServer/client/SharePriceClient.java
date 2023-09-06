package ru.ev.VirtualStockExchangeServer.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "shareprice", url = "${moex.shares.price2020.url}",configuration = FeignConfig.class)
public interface SharePriceClient {
    @GetMapping
    String getSharePrice();
}
