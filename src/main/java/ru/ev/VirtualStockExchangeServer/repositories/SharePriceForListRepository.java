package ru.ev.VirtualStockExchangeServer.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ev.VirtualStockExchangeServer.models.SharePriceForList.SharePriceForList;

public interface SharePriceForListRepository extends JpaRepository<SharePriceForList,Long> {
}
