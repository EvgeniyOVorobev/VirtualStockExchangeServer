package ru.ev.VirtualStockExchangeServer.models.SecidAndNameList;

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
public class GroupGroupSecidAndNameOfShare {
    @JsonProperty("history")
    private GroupSecidAndNameOfShare listShare;

    @JsonProperty("history")
    public GroupSecidAndNameOfShare getListShare() {
        return listShare;
    }

    @JsonProperty("history")
    public void setListShare(GroupSecidAndNameOfShare history) {
        this.listShare = history;
    }


}
