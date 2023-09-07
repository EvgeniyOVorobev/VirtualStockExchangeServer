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
import ru.ev.VirtualStockExchangeServer.services.MainService;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/")
public class MoexShareController {

    private final MainService mainService;

    @GetMapping("/")
    public String showShares(Model model1, Model model2, Model model3, Model model4) throws JsonProcessingException {
        List<Share> shareList = mainService.showListOfShares();
//        List<SharePrice> sharePrices = new ArrayList<>();
//        List<SharePriceForList> sharePriceForLists = new ArrayList<>();
//        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        if (mainService.getListofSharesPrice().isEmpty()) {
//            for (int i = 0; i < shareList.size(); i++) {
//                sharePrices.addAll(mainService.showPrice(shareList.get(i).getSecid()));
//            }
//        } else if (
//                !mainService.getDate().equals(mainService.getListofSharesPrice().get(0).getDate().format(dateTimeFormatter))) {
//            List<SharePrice> newPrices = new ArrayList<>();
//            for (int i = 0; i < shareList.size(); i++) {
//                newPrices.addAll(mainService.showPrice(shareList.get(i).getSecid()));
//            }
//            mainService.saveNewPrices(newPrices);
//            sharePrices = newPrices;
//        } else {
//            sharePriceForLists = mainService.getListofSharesPrice();
//            for (int i = 0; i < sharePriceForLists.size(); i++) {
//                sharePrices.add(new SharePrice().convertToSharePrice(sharePriceForLists.get(i)));
//            }
//        }
        model1.addAttribute("people", getSharePrice(shareList));
        List<SharePrice> sharePrices1 = mainService.getSharesUser();
        model2.addAttribute("user1", sharePrices1);
        model3.addAttribute("sum", mainService.sumOfTotalCoast(sharePrices1));
        model4.addAttribute("date", mainService.startDate());
        return "show";
    }

    @PostMapping("/buyShare")
    public String buyShare(@ModelAttribute() SharePrice sharePrice, @ModelAttribute("t") int a) {
        sharePrice.setCount(a);
        sharePrice.setTotalCost();
        mainService.adduserShares(sharePrice);
        return "redirect:/";
    }

    @PostMapping("/sellShare")
    public String sellShare(@ModelAttribute() SharePrice sharePrice, @ModelAttribute("s") int a) {
        mainService.deleteShare(sharePrice, a);
        return "redirect:/";
    }

    @PostMapping("/setDate")
    public String setDate(@ModelAttribute("calendar") LocalDate date) {
        mainService.setDate(date);
        return "redirect:/";
    }

    public List<SharePrice> getSharePrice(List<Share> shareList) throws JsonProcessingException {
        List<SharePrice> sharePrices = new ArrayList<>();
        List<SharePriceForList> sharePriceForLists = new ArrayList<>();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if (mainService.getListofSharesPrice().isEmpty()) {
            for (int i = 0; i < shareList.size(); i++) {
                sharePrices.addAll(mainService.showPrice(shareList.get(i).getSecid()));
            }
        } else if (
                !mainService.getDate().equals(mainService.getListofSharesPrice().get(0).getDate().format(dateTimeFormatter))) {
            List<SharePrice> newPrices = new ArrayList<>();
            for (int i = 0; i < shareList.size(); i++) {
                newPrices.addAll(mainService.showPrice(shareList.get(i).getSecid()));
            }
            mainService.saveNewPrices(newPrices);
            sharePrices = newPrices;
        } else {
            sharePriceForLists = mainService.getListofSharesPrice();
            for (int i = 0; i < sharePriceForLists.size(); i++) {
                sharePrices.add(new SharePrice().convertToSharePrice(sharePriceForLists.get(i)));
            }
        }
        return sharePrices;
    }

}
