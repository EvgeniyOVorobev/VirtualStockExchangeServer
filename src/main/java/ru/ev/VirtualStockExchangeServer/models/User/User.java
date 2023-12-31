package ru.ev.VirtualStockExchangeServer.models.User;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.ev.VirtualStockExchangeServer.Exeption.RanOutOfMoney;

@Component
@Data
public class User {
    private String date;
    private Double bankAccount;
    @Value("${urlForListOfPrice1}")
    private String firstUrl;
    @Value("${urlForListOfPrice2}")
    private String secondUrl;
    @Value("${urlForListOfPrice3}")
    private String thirdUrl;
    @Value("${urlForListOfPrice4}")
    private String fourthUrl;
    {
        date = "2014-06-09";
        bankAccount = 100000.00;
    }
    //
    public void debitingFromAccount(Double sharePrice)  {
        double debet = bankAccount - sharePrice;
        if (debet < 0) {
            throw new RanOutOfMoney("Ran Out Of Money");
        } else {
            double a = Math.pow(10, 2);
            bankAccount = Math.ceil(debet * a) / a;
        }
    }
    public void plusAccount(Double sharePrice) {
        double a = bankAccount + sharePrice;
        double b = Math.pow(10, 2);
        bankAccount = Math.ceil(a * b) / b;
    }


}
