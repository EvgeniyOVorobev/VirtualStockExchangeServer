package ru.ev.VirtualStockExchangeServer.models.User;

import ru.ev.VirtualStockExchangeServer.models.SharePrice.SharePrice;

import java.time.LocalDate;
import java.util.List;

public class UserShares {
    private List<SharePrice> sharePriceList;

    private LocalDate date;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<SharePrice> getSharePriceList() {
        return sharePriceList;
    }

    public void setSharePriceList(List<SharePrice> sharePriceList) {
        this.sharePriceList = sharePriceList;
    }
}
