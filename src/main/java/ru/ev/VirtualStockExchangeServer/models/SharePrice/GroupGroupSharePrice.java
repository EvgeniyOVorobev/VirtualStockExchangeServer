package ru.ev.VirtualStockExchangeServer.models.SharePrice;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.processing.Generated;


//@Entity
//@Table(name="listofshares")
//@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "history"
})
@Generated("jsonschema2pojo")
public class GroupGroupSharePrice {
    @JsonProperty("history")
    private GroupOfSharePrice listShare;

    @JsonProperty("history")
    public GroupOfSharePrice getListSharePrice() {
        return listShare;
    }

    @JsonProperty("history")
    public void setListSharePrice(GroupOfSharePrice history) {
        this.listShare = history;
    }


}
