package ru.ev.VirtualStockExchangeServer.Exeption;

public class RanOutOfMoney extends RuntimeException{
    public RanOutOfMoney(String message) {
        super(message);
    }
}
