
package ru.ev.VirtualStockExchangeServer.models.SharesList;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;

import ru.ev.VirtualStockExchangeServer.models.SharePrice.SharePrice;


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
public class ListShare {


    @JsonProperty("data")
    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    private List<Share> shares;


    @JsonProperty("data")
    public List<Share> getShares() {
        return shares;
    }

    @JsonProperty("data")
    public void setShares(List<Share> data) {
        this.shares = data;
    }

//

}
