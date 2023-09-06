package ru.ev.VirtualStockExchangeServer.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ev.VirtualStockExchangeServer.models.SharesList.Share;

public interface ShareListRepository extends JpaRepository<Share,Long> {
}
