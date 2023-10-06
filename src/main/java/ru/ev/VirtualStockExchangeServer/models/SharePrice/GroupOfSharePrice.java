
package ru.ev.VirtualStockExchangeServer.models.SharePrice;
import com.fasterxml.jackson.annotation.*;
import javax.annotation.processing.Generated;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "columns",
        "data"
})
@Generated("jsonschema2pojo")
@JsonIgnoreProperties(ignoreUnknown = true)
public class GroupOfSharePrice {
    @JsonProperty("data")
    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    private List<SharePrice> shares;
    @JsonProperty("data")
    public List<SharePrice> getSharesPrice() {
        return shares;
    }

    @JsonProperty("data")
    public void setSharesPrice(List<SharePrice> data) {
        this.shares = data;
    }
    @Override
    public String toString() {
        return "ListSharePrice{" +
                "shares=" + shares +
                '}';
    }


}
