package ru.ev.VirtualStockExchangeServer.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ev.VirtualStockExchangeServer.models.ListOfPrices.ListOfPrice;

public interface ListOfPriceRepository extends JpaRepository<ListOfPrice,Long> {
}
