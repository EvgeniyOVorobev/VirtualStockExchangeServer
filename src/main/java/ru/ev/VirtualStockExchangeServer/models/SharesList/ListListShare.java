package ru.ev.VirtualStockExchangeServer.models.SharesList;

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
public class ListListShare {
    @JsonProperty("history")
    private ListShare listShare;

    @JsonProperty("history")
    public ListShare getListShare() {
        return listShare;
    }

    @JsonProperty("history")
    public void setListShare(ListShare history) {
        this.listShare = history;
    }


}
