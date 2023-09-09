package ru.ev.VirtualStockExchangeServer.Exeption;

public class ShareRequestException extends RuntimeException {
    public ShareRequestException(Exception ex) {
        super(ex);
    }
}