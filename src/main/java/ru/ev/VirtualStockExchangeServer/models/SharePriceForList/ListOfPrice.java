package ru.ev.VirtualStockExchangeServer.models.SharePriceForList;

import jakarta.persistence.*;
import lombok.Data;
import ru.ev.VirtualStockExchangeServer.models.SharePrice.SharePrice;

import java.time.LocalDate;

@Entity
@Table(name="listofshareprice")
@Data
public class ListOfPrice {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name="shortname")
    private String shortName;
    @Column(name="secid")
    private String secid;
    @Column(name="Price")
    private Double price;
    @Column(name="tradedate")
    private LocalDate date;


    public ListOfPrice getShareFoListOfPrice(SharePrice sharePrices) {
        ListOfPrice sharePriceForList = new ListOfPrice();
        sharePriceForList.setPrice(sharePrices.getPrice());
        sharePriceForList.setDate(sharePrices.getDate());
        sharePriceForList.setSecid(sharePrices.getSecid());
        sharePriceForList.setShortName(sharePrices.getShortName());
        return sharePriceForList;
    }

}
