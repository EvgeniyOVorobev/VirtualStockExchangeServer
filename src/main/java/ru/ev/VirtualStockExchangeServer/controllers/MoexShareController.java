package ru.ev.VirtualStockExchangeServer.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.ev.VirtualStockExchangeServer.DTO.SharePriceDTO;
import ru.ev.VirtualStockExchangeServer.Exeption.ExceededTheNumberOfShares;
import ru.ev.VirtualStockExchangeServer.Exeption.RanOutOfMoney;
import ru.ev.VirtualStockExchangeServer.models.SharePrice.SharePrice;
import ru.ev.VirtualStockExchangeServer.models.SharePriceForList.ListOfPrice;
import ru.ev.VirtualStockExchangeServer.models.SharesList.SecidAndNameOfShare;
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
    public String showShares(Model model1, Model model2, Model model3, Model model4, Model model5) throws JsonProcessingException {
        List<SecidAndNameOfShare> listOfPrice = mainService.showListOfShortNameShares();
        model1.addAttribute("listOfPrice", getSharePrice(listOfPrice));
        List<SharePrice> userShares = mainService.getUserShares();
        model2.addAttribute("userShares", userShares);
        model3.addAttribute("sum", mainService.sumOfTotalCoast(userShares));
        model4.addAttribute("date", mainService.startDate());
        model5.addAttribute("bankAccount", mainService.getBankAccount());
        return "show";
    }

    @PostMapping("/reset")
    public String resetAll() {
        mainService.reset();
        return "redirect:/";
    }

    @PostMapping("/buyShare")
    public String buyShare(@ModelAttribute() SharePriceDTO sharePriceDTO, @ModelAttribute("t") int count) {
        SharePrice sharePrice = mainService.convertToSharePrice(sharePriceDTO);
        sharePrice.setCount(count);
        sharePrice.setTotalCost();
        try {
            mainService.addUserShares(sharePrice, count);
        } catch (RanOutOfMoney e) {
            System.out.println(e.getMessage());
            return "OutOfMoney";
        }
        return "redirect:/";
    }

    @PostMapping("/sellShare")
    public String sellShare(@ModelAttribute() SharePriceDTO sharePriceDTO, @ModelAttribute("s") int count) {
        SharePrice sharePrice = mainService.convertToSharePrice(sharePriceDTO);
        try {
            mainService.deleteShare(sharePrice, count);
        } catch (ExceededTheNumberOfShares e) {
            System.out.println(e.getMessage());
            return "ErrorCount";
        }
        return "redirect:/";
    }

    @PostMapping("/setDate")
    public String setDate(@ModelAttribute("calendar") LocalDate date) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate oldDate = LocalDate.parse(mainService.getDate(), dateTimeFormatter);
        if (date.isBefore(oldDate)) {
            return "ErrorDate";
        }
        mainService.setDate(date);
        return "redirect:/";
    }

    public List<SharePriceDTO> getSharePrice(List<SecidAndNameOfShare> shareList) throws JsonProcessingException {
        List<SharePrice> sharePrices = new ArrayList<>();
        List<ListOfPrice> sharePriceForLists = new ArrayList<>();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if (mainService.getListOfSharesPrice().isEmpty()) {
            for (int i = 0; i < shareList.size(); i++) {
                sharePrices.addAll(mainService.showPrice(shareList.get(i).getSecid()));
            }
        } else if (
                !mainService.getDate().equals(mainService.getListOfSharesPrice().get(0).getDate().format(dateTimeFormatter))) {
            List<SharePrice> newPrices = new ArrayList<>();
            for (int i = 0; i < shareList.size(); i++) {
                newPrices.addAll(mainService.showPrice(shareList.get(i).getSecid()));
            }
            mainService.saveNewPrices(newPrices);
            sharePrices = newPrices;
        } else {
            sharePriceForLists = mainService.getListOfSharesPrice();
            for (int i = 0; i < sharePriceForLists.size(); i++) {
                sharePrices.add(new SharePrice().convertToSharePrice(sharePriceForLists.get(i)));
            }
        }
        List<SharePriceDTO> sharePriceDTOList = new ArrayList<>();
        for (int i = 0; i < sharePrices.size(); i++) {
            sharePriceDTOList.add(mainService.convertToShareDTO(sharePrices.get(i)));
        }


        return sharePriceDTOList;
    }


}
