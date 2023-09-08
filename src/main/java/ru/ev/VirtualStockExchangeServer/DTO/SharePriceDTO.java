package ru.ev.VirtualStockExchangeServer.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;

import java.time.LocalDate;

public class SharePriceDTO {

    private String shortName;

    private String secid;

    private Double price;

    public String getShortName() {
        return shortName;
    }
    private LocalDate date;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getSecid() {
        return secid;
    }

    public void setSecid(String secid) {
        this.secid = secid;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
