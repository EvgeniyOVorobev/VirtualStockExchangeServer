package ru.ev.VirtualStockExchangeServer.models.SharePrice;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import ru.ev.VirtualStockExchangeServer.models.SharePriceForList.SharePriceForList;


import javax.annotation.processing.Generated;
import java.time.LocalDate;

import static java.lang.Math.round;

@Entity
@Table(name="shareprice")
//@Data
//@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("jsonschema2pojo")
//@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIgnoreProperties(value = { "id","count","totalCost","oldPrice" })
public class SharePrice {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.AUTO)

    private Long id;
//    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
//    private ListSharePrice listSharePrice;
    @Column(name="shortname")
    private String shortName;
    @Column(name="secid")
    private String secid;
    @Column(name="Price")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Double price;

    @Column(name="oldPrice")
    private Double oldPrice;
    @Column(name="tradedate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date;
    @Column(name="count")
    private int count;
    @Column(name="totalCost")
    private double totalCost;

    @Column(name="difference")
    private double difference;

    public Double getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(Double oldPrice) {
        this.oldPrice = oldPrice;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public double getDifference() {
        return difference;
    }

    public void setDifference() {
        if(this.oldPrice<=0){
            this.difference=0;
        }
        else
            this.difference=Math.round(((price-oldPrice)/oldPrice*100)*100.0)/100.0;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost() {
        this.totalCost = count*price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int a) {
        this.count = a;
    }

    public String getShortName() {
        return shortName;
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "SharePrice{" +
                "shortName='" + shortName + '\'' +
                ", secid='" + secid + '\'' +
                ", price=" + price +
                ", date=" + date +
                '}';
    }
    public static SharePrice convertToSharePrice(SharePriceForList list){
        SharePrice sharePrice=new SharePrice();
        sharePrice.setPrice(list.getPrice());
        sharePrice.setDate(list.getDate());
        sharePrice.setSecid(list.getSecid());
        sharePrice.setShortName(list.getShortName());
        return sharePrice;
    }
}
