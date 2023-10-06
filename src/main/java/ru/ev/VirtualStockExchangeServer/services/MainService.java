package ru.ev.VirtualStockExchangeServer.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import ru.ev.VirtualStockExchangeServer.DTO.SharePriceDTO;
import ru.ev.VirtualStockExchangeServer.DTO.UserSharePriceDTO;
import ru.ev.VirtualStockExchangeServer.Exeption.ExceededTheNumberOfShares;
import ru.ev.VirtualStockExchangeServer.Exeption.LimitRequestsException;
import ru.ev.VirtualStockExchangeServer.Exeption.RanOutOfMoney;
import ru.ev.VirtualStockExchangeServer.Exeption.RestTemplateResponseErrorHandler;
import ru.ev.VirtualStockExchangeServer.models.SharePrice.GroupGroupSharePrice;
import ru.ev.VirtualStockExchangeServer.models.SharePrice.GroupOfSharePrice;
import ru.ev.VirtualStockExchangeServer.models.SharePrice.SharePrice;
import ru.ev.VirtualStockExchangeServer.models.ListOfPrices.ListOfPrice;
import ru.ev.VirtualStockExchangeServer.models.SecidAndNameList.GroupGroupSecidAndNameOfShare;
import ru.ev.VirtualStockExchangeServer.models.SecidAndNameList.GroupSecidAndNameOfShare;
import ru.ev.VirtualStockExchangeServer.models.SecidAndNameList.SecidAndNameOfShare;
import ru.ev.VirtualStockExchangeServer.models.User.User;
import ru.ev.VirtualStockExchangeServer.repositories.SecidAndNameOfShareRepository;
import ru.ev.VirtualStockExchangeServer.repositories.ListOfPriceRepository;
import ru.ev.VirtualStockExchangeServer.repositories.SharePriceRepository;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@PropertySource("classpath:URL.properties")
public class MainService {

    private final SecidAndNameOfShareRepository secidAndNameOfShareRepository;
    private final ListOfPriceRepository listOfPriceRepository;
    private final SharePriceRepository sharePriceRepository;
    private final ObjectMapper objectMapper;
    private final User user;

    public String getDate() {
        return user.getDate();
    }


    public void setDate(LocalDate date) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String nextDate = date.format(dateTimeFormatter);
        user.setDate(nextDate);
    }

    public LocalDate getNowDate() {
        return LocalDate.parse(user.getDate());
    }
    //getting the price of one share
    @Transactional
    public List<SharePrice> showPrice(String secid) throws JsonProcessingException {
        GroupOfSharePrice groupOfSharePrice = getGroupSharePriceFromMoex(secid);
        List<SharePrice> priceList = new ArrayList<>();
        if (groupOfSharePrice.getSharesPrice().isEmpty()) {
            return priceList;
        } else if (groupOfSharePrice.getSharesPrice().get(0).getPrice() == null) {
            return priceList;
        } else priceList.add((groupOfSharePrice.getSharesPrice().get(0)));
        for (SharePrice sharePrice : priceList) {
            ListOfPrice sharePriceForList = new ListOfPrice();
            sharePriceForList = sharePriceForList.getShareFoListOfPrice(sharePrice);
            listOfPriceRepository.save(sharePriceForList);
        }
        return priceList;
    }
    //getting the price for one share from moex
    public GroupOfSharePrice getGroupSharePriceFromMoex(String secid) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        log.info("Request for share {}", secid);
        restTemplate.setErrorHandler(new RestTemplateResponseErrorHandler());
        String json = restTemplate.getForObject(user.getFirstUrl() + secid + user.getSecondUrl() + user.getDate() + user.getThirdUrl() + user.getDate() + user.getFourthUrl(), String.class);
        if (json != null && json.isEmpty()) {
            log.error("Moex isn't answering for getting prices.");
            throw new LimitRequestsException("Moex isn't answering for getting prices.");
        }
        log.info("Getting share from Moex");
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.findAndRegisterModules();
        GroupGroupSharePrice groupGroupSharePrice = objectMapper.readValue(json, GroupGroupSharePrice.class);
        return groupGroupSharePrice.getListSharePrice();
    }
    //getting a list of names of all stocks on the selected date
    @Transactional
    public List<SecidAndNameOfShare> showListOfShortNameShares() throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        List<SecidAndNameOfShare> listOfSecidAndNameOfShares = new ArrayList<>();
        if (secidAndNameOfShareRepository.findAll().isEmpty()) {
            restTemplate.setErrorHandler(new RestTemplateResponseErrorHandler());
            String jsonList = restTemplate.getForObject("https://iss.moex.com/iss/history/engines/stock/markets/shares/boards/TQBR/securities.json?from=" + user.getDate() + "&till=" + user.getDate() + "&iss.meta=off&iss.only=history&history.columns=SHORTNAME,SECID", String.class);
            log.error("Getting short name of share");
            if (jsonList.isEmpty()) {
                log.error("Moex isn't answering for getting secid.");
                throw new LimitRequestsException("Moex isn't answering for getting secid.");
            }
            GroupGroupSecidAndNameOfShare listShare22 = objectMapper.readValue(jsonList, GroupGroupSecidAndNameOfShare.class);
            GroupSecidAndNameOfShare listShar = listShare22.getListShare();
            listOfSecidAndNameOfShares = listShar.getShares();
            secidAndNameOfShareRepository.saveAll(listOfSecidAndNameOfShares);
        } else {
            listOfSecidAndNameOfShares = secidAndNameOfShareRepository.findAll();
        }
        return listOfSecidAndNameOfShares;
    }
    //calculate the total amount
    public double sumOfTotalCoast(List<SharePrice> sharePrices) {
        double sum = 0;
        for (SharePrice sharePrice : sharePrices) {
            sum += sharePrice.getTotalCost();
        }
        BigDecimal result = new BigDecimal(sum);
        result = result.setScale(2, RoundingMode.HALF_UP);
        sum = result.doubleValue();
        return sum;
    }
    //add a share to the portfolio, if there is already such a share, increase the number, recalculate the stability of the shares
    @Transactional
    public void addUserShares(SharePrice comingSharePrice, int count)  {
        if (sharePriceRepository.findAll().isEmpty()) {
            sharePriceRepository.save(comingSharePrice);
            user.debitingFromAccount(comingSharePrice.getPrice() * count);
        } else {
            List<SharePrice> sharePricesOld = sharePriceRepository.findAll();
            for (SharePrice oldPrice : sharePricesOld) {
                if (oldPrice.getShortName().equals(comingSharePrice.getShortName())) {
                    int oldCount = oldPrice.getCountOfShares();
                    int newCount = comingSharePrice.getCountOfShares();
                    comingSharePrice.setCountOfShares(oldCount + newCount);
                    comingSharePrice.setTotalCost();
                    sharePriceRepository.delete(oldPrice);
                    sharePriceRepository.save(comingSharePrice);
                    user.debitingFromAccount(comingSharePrice.getPrice() * count);
                } else {
                    sharePriceRepository.save(comingSharePrice);
                    user.debitingFromAccount(comingSharePrice.getPrice() * count);
                }
            }
        }
    }
    //getting list of user shares
    @Transactional
    public List<SharePrice> getUserShares() {
        if (sharePriceRepository.findAll().isEmpty()) {
            return sharePriceRepository.findAll();
        } else {
            List<ListOfPrice> listOfPrice = listOfPriceRepository.findAll();
            List<SharePrice> sharePriceNow = new ArrayList<>();
            for (int i = 0; i < listOfPrice.size(); i++) {
                sharePriceNow.add(SharePrice.convertToSharePrice(listOfPrice.get(i)));
            }
            List<SharePrice> userShares = sharePriceRepository.findAll();
            if (!userShares.get(0).getDate().equals(sharePriceNow.get(0).getDate())) {
                for (SharePrice sharePriceOld : userShares) {
                    for (SharePrice priceNew : sharePriceNow) {
                        if (sharePriceOld.getSecid().equals(priceNew.getSecid())) {
                            sharePriceOld.setOldPrice(sharePriceOld.getPrice());
                            sharePriceOld.setPrice(priceNew.getPrice());
                            sharePriceOld.setDifference();
                            sharePriceOld.setTotalCost();
                            sharePriceRepository.save(sharePriceOld);
                        }
                    }
                }
            }
            return userShares;
        }
    }
    //getting list of prices
    @Transactional
    public List<ListOfPrice> getListOfSharesPrice() {
        return listOfPriceRepository.findAll();
    }


    //delete share, if the number of shares is less than the specified number, we cause an exception,
    // if the difference is zero, we delete the share, the stability of the shares is added to the wallet
    //recalculation of the stability of shares, quantity
    @Transactional
    public void deleteShare(SharePrice comingSharePrice, int count)  {
        comingSharePrice.setCountOfShares(count);
        List<SharePrice> sharePrices = sharePriceRepository.findAll();
        for (SharePrice priceOld : sharePrices) {
            if (priceOld.getShortName().equals(comingSharePrice.getShortName()) && (priceOld.getDate().equals(comingSharePrice.getDate()))) {
                int oldCountOfShares = priceOld.getCountOfShares();
                int comingCountOfShares = comingSharePrice.getCountOfShares();
                if (oldCountOfShares - comingCountOfShares < 0) {
                    log.info("Error");
                    throw new ExceededTheNumberOfShares("Exceeded the number of shares");
                } else comingSharePrice.setCountOfShares(oldCountOfShares - comingCountOfShares);
                if (comingSharePrice.getCountOfShares() == 0) {
                    sharePriceRepository.delete(priceOld);
                    user.plusAccount(comingSharePrice.getPrice() * count);
                } else {
                    comingSharePrice.setTotalCost();
                    sharePriceRepository.delete(priceOld);
                    sharePriceRepository.save(comingSharePrice);
                    user.plusAccount(comingSharePrice.getPrice() * count);
                }
            }
        }
    }
    //getting a new list of stocks to buy, when the date changes
    @Transactional
    public void saveNewPrices(List<SharePrice> comingPrices) {
        List<ListOfPrice> oldPrices = listOfPriceRepository.findAll();
        List<ListOfPrice> PricesWithNewDate = new ArrayList<>();
        for (SharePrice comingPrice : comingPrices) {
            PricesWithNewDate.add(new ListOfPrice().getShareFoListOfPrice(comingPrice));
        }
        for (ListOfPrice old : oldPrices
        ) {
            listOfPriceRepository.delete(old);
        }
        for (ListOfPrice priceWithNewDate : PricesWithNewDate
        ) {
            listOfPriceRepository.save(priceWithNewDate);
        }
    }

    public SharePriceDTO convertToShareDTO(SharePrice sharePrice) {
        ModelMapper mapper = new ModelMapper();
        return mapper.map(sharePrice, SharePriceDTO.class);
    }

    public SharePrice convertToSharePrice(SharePriceDTO sharePriceDTO) {
        ModelMapper mapper = new ModelMapper();
        return mapper.map(sharePriceDTO, SharePrice.class);
    }

    public UserSharePriceDTO convertToUserSharePriceDTO(SharePrice sharePrice) {
        ModelMapper mapper = new ModelMapper();
        return mapper.map(sharePrice, UserSharePriceDTO.class);
    }

    //reset date, wallet, delete portfolio with shares
    public void reset() {
        user.setDate("2014-06-09");
        sharePriceRepository.deleteAll();
        user.setBankAccount(100000.00);
    }
    //getting amount of money
    public double getBankAccount() {
        return user.getBankAccount();
    }


}
