package ru.ev.VirtualStockExchangeServer.models.SharesList;

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
public class ListListSecidAndNameOfShare {
    @JsonProperty("history")
    private ListSecidAndNameOfShare listShare;

    @JsonProperty("history")
    public ListSecidAndNameOfShare getListShare() {
        return listShare;
    }

    @JsonProperty("history")
    public void setListShare(ListSecidAndNameOfShare history) {
        this.listShare = history;
    }


}
