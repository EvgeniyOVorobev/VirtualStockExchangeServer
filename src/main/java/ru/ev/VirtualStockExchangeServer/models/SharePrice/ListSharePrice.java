
package ru.ev.VirtualStockExchangeServer.models.SharePrice;

import com.fasterxml.jackson.annotation.*;

import lombok.Data;


import javax.annotation.processing.Generated;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
//@Data
//@Entity
//@Table(name="listOfPrice")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "columns",
        "data"
})
@Generated("jsonschema2pojo")
//@JsonIgnoreProperties(value = { "date"})
@JsonIgnoreProperties(ignoreUnknown = true)
public class ListSharePrice {
//    @Id
//    @Column(name="id")
//    @GeneratedValue(strategy = GenerationType.AUTO)
//
//    private Long id;


    @JsonProperty("data")
    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
//    @Column(name="list")
//    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<SharePrice> shares;
//    @Column(name="date")
//    private LocalDate date;


    @JsonProperty("data")
    public List<SharePrice> getSharesPrice() {
        return shares;
    }

    @JsonProperty("data")
    public void setSharesPrice(List<SharePrice> data) {
        this.shares = data;
    }

//    public LocalDate getDate() {
//        return date;
//    }
//
//    public void setDate(LocalDate date) {
//        this.date = date;
//    }

    @Override
    public String toString() {
        return "ListSharePrice{" +
                "shares=" + shares +
                '}';
    }

    //

}
