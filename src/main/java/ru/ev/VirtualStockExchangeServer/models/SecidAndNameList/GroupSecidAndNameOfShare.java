
package ru.ev.VirtualStockExchangeServer.models.SecidAndNameList;

import com.fasterxml.jackson.annotation.*;


import javax.annotation.processing.Generated;
import java.util.List;

//@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "columns",
        "data"
})
@Generated("jsonschema2pojo")
@JsonIgnoreProperties(ignoreUnknown = true)
public class GroupSecidAndNameOfShare {


    @JsonProperty("data")
    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    private List<SecidAndNameOfShare> shares;


    @JsonProperty("data")
    public List<SecidAndNameOfShare> getShares() {
        return shares;
    }

    @JsonProperty("data")
    public void setShares(List<SecidAndNameOfShare> data) {
        this.shares = data;
    }

//

}
