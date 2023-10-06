package ru.ev.VirtualStockExchangeServer.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ev.VirtualStockExchangeServer.models.SecidAndNameList.SecidAndNameOfShare;

public interface SecidAndNameOfShareRepository extends JpaRepository<SecidAndNameOfShare,Long> {

}
