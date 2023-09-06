package ru.ev.VirtualStockExchangeServer.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "listofshares", url = "${moex.shares.list.url}",configuration = FeignConfig.class)
public interface ListOfSharesClient {
    @GetMapping
    String getListOfShares();
}
