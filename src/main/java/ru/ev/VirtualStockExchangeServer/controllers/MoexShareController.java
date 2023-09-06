package ru.ev.VirtualStockExchangeServer.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.ev.VirtualStockExchangeServer.models.SharePrice.SharePrice;
import ru.ev.VirtualStockExchangeServer.models.SharePriceForList.SharePriceForList;
import ru.ev.VirtualStockExchangeServer.models.SharesList.Share;
import ru.ev.VirtualStockExchangeServer.services.MoexService;
import ru.ev.VirtualStockExchangeServer.services.SqlServices;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/")
public class MoexShareController {
    private final MoexService moexService;

    private final SqlServices sqlServices;


    @GetMapping("/")
    public String showShares(Model model1, Model model2, Model model3, Model model4) throws JsonProcessingException {
        List<Share> shareList = moexService.showListOfShares();
        List<SharePrice> sharePrices = new ArrayList<>();
        List<SharePriceForList> sharePriceForLists = new ArrayList<>();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if (sqlServices.getListofSharesPrice().isEmpty()) {
            for (int i = 0; i < shareList.size(); i++) {
                sharePrices.addAll(moexService.showPrice(shareList.get(i).getSecid()));
                System.out.println("v moex za listom of price");
            }
        } else if (
                !moexService.getDate1().equals(sqlServices.getListofSharesPrice().get(0).getDate().format(dateTimeFormatter))) {
            List<SharePrice> newPrices = new ArrayList<>();
            for (int i = 0; i < shareList.size(); i++) {
                newPrices.addAll(moexService.showPrice(shareList.get(i).getSecid()));
                System.out.println("Новый прайс");
            }


            sqlServices.saveNewPrices(newPrices);
            sharePrices = newPrices;

        } else {
            sharePriceForLists = sqlServices.getListofSharesPrice();
            for (int i = 0; i < sharePriceForLists.size(); i++) {
                sharePrices.add(new SharePrice().convertToSharePrice(sharePriceForLists.get(i)));
                System.out.println("za listom v bd");
            }
        }
        model1.addAttribute("people", sharePrices);
        List<SharePrice> sharePrices1 = sqlServices.getSharesUser();
        model2.addAttribute("user1", sharePrices1);
        model3.addAttribute("sum", moexService.sumOfTotalCoast(sharePrices1));
        System.out.println(model3);
        model4.addAttribute("date", moexService.StartDate());
        return "show";
    }

    @PostMapping("/buyShare")
    public String buyShare(@ModelAttribute() SharePrice sharePrice, @ModelAttribute("t") int a) {
        sharePrice.setCount(a);
        sharePrice.setTotalCost();
        sqlServices.adduserShares(sharePrice);
        return "redirect:/";
    }

    @PostMapping("/sellShare")
    public String sellShare(@ModelAttribute() SharePrice sharePrice, @ModelAttribute("s") int a) {
//        sharePrice.setCount(a);
        sqlServices.deleteShare(sharePrice, a);
        return "redirect:/";
    }

    @PostMapping("/setDate")
    public String setDate(@ModelAttribute("calendar") LocalDate date) {
        moexService.setDate1(date);
        return "redirect:/";
    }

}
