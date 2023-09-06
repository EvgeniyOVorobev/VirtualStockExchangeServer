package ru.ev.VirtualStockExchangeServer.models.SharePrice;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import javax.annotation.processing.Generated;


//@Entity
//@Table(name="listofshares")
//@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "history"
})
@Generated("jsonschema2pojo")
public class ListListSharePrice {
    @JsonProperty("history")
    private ListSharePrice listShare;

    @JsonProperty("history")
    public ListSharePrice getListSharePrice() {
        return listShare;
    }

    @JsonProperty("history")
    public void setListSharePrice(ListSharePrice history) {
        this.listShare = history;
    }


}
