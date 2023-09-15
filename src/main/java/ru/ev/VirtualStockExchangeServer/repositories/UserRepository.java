package ru.ev.VirtualStockExchangeServer.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.ev.VirtualStockExchangeServer.models.User.User;

public interface UserRepository extends JpaRepository<User,Long> {
        User findByName(String name);
}
