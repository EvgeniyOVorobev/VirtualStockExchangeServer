package ru.ev.VirtualStockExchangeServer.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ev.VirtualStockExchangeServer.models.SharePriceForList.ListOfPrice;

public interface SharePriceForListRepository extends JpaRepository<ListOfPrice,Long> {
}
