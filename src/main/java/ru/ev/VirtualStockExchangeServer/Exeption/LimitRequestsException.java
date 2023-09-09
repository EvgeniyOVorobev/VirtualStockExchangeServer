package ru.ev.VirtualStockExchangeServer.Exeption;

public class LimitRequestsException extends RuntimeException {
    public LimitRequestsException(String message) {
        super(message);
    }
}

