package ru.ev.VirtualStockExchangeServer.models.User;

import lombok.Data;

import java.time.LocalDate;

@Data
public class User {
    private LocalDate date;
    private Double bankAccount;
    {
        date=LocalDate.parse("2014-06-09");
        bankAccount=100000.00;
    }

    public void debitingFromAccount(Double sharePrice){
        double debet=bankAccount-sharePrice;
        if(debet<0){
            throw new RuntimeException("Не хвататет бабла");
        }
        else {double a=Math.pow(10,2);
            bankAccount=Math.ceil(debet*a)/a;
        }
    }

    public void plusAccount(Double sharePrice){
        double a=bankAccount+sharePrice;
        double b=Math.pow(10,2);
        bankAccount= Math.ceil(a*b)/b;
    }




}
