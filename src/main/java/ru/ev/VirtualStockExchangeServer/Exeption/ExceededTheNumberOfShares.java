package ru.ev.VirtualStockExchangeServer.Exeption;

public class ExceededTheNumberOfShares extends RuntimeException{
    public ExceededTheNumberOfShares(String message) {
        super(message);
    }
}
