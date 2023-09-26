package ru.ev.VirtualStockExchangeServer.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ev.VirtualStockExchangeServer.models.SharePrice.SharePrice;

import java.util.List;

@Repository
public interface SharePriceRepository extends JpaRepository<SharePrice, Long> {

}
