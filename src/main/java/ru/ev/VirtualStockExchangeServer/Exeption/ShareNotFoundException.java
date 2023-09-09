package ru.ev.VirtualStockExchangeServer.Exeption;

public class ShareNotFoundException extends RuntimeException {
    public ShareNotFoundException(String m) {
        super(m);
    }
}