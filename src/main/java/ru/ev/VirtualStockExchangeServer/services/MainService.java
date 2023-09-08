package ru.ev.VirtualStockExchangeServer.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import ru.ev.VirtualStockExchangeServer.DTO.SharePriceDTO;
import ru.ev.VirtualStockExchangeServer.DTO.UserSharePriceDTO;
import ru.ev.VirtualStockExchangeServer.models.SharePrice.ListListSharePrice;
import ru.ev.VirtualStockExchangeServer.models.SharePrice.ListSharePrice;
import ru.ev.VirtualStockExchangeServer.models.SharePrice.SharePrice;
import ru.ev.VirtualStockExchangeServer.models.SharePriceForList.ListOfPrice;
import ru.ev.VirtualStockExchangeServer.models.SharesList.ListListSecidAndNameOfShare;
import ru.ev.VirtualStockExchangeServer.models.SharesList.ListSecidAndNameOfShare;
import ru.ev.VirtualStockExchangeServer.models.SharesList.SecidAndNameOfShare;
import ru.ev.VirtualStockExchangeServer.models.User.UserShares;
import ru.ev.VirtualStockExchangeServer.repositories.ShareListRepository;
import ru.ev.VirtualStockExchangeServer.repositories.SharePriceForListRepository;
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
    @Autowired
    private final ShareListRepository shareListRepository;
    private final SharePriceForListRepository sharePriceForListRepository;
    private final Environment environment;
    private final SharePriceRepository sharePriceRepository;
    UserShares userShares = new UserShares();
    ObjectMapper objectMapper = new ObjectMapper();
    RestTemplate restTemplate = new RestTemplate();
    @Value("${urlForListOfPrice1}")
    String a ;
    @Value("${urlForListOfPrice2}")
    String c ;
    String date1 = "2014-06-09";
    @Value("${urlForListOfPrice3}")
    String ca ;
    String date2 = "2014-06-09";
    @Value("${urlForListOfPrice4}")
    String de ;

    public String getDate() {
        return date1;
    }

    public void setDate(LocalDate date) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date1 = date.format(dateTimeFormatter);
        this.date1 = date1;
        this.date2 = date1;
    }

    public LocalDate startDate() {
        userShares.setDate(LocalDate.parse(date1));
        return userShares.getDate();
    }

    @Transactional
    public List<SharePrice> showPrice(String secid) throws JsonProcessingException {
        String json = restTemplate.getForObject(a + secid + c + date1 + ca + date2 + de, String.class);
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.findAndRegisterModules();
        ListListSharePrice listShare = objectMapper.readValue(json, ListListSharePrice.class);
        ListSharePrice listShare1 = listShare.getListSharePrice();
        List<SharePrice> priceList = new ArrayList<>();
        if (listShare1.getSharesPrice().isEmpty()) {
            return priceList;
        } else if (listShare1.getSharesPrice().get(0).getPrice() == null) {
            return priceList;
        } else priceList.add((listShare1.getSharesPrice().get(0)));
        for (int i = 0; i < priceList.size(); i++) {
            ListOfPrice sharePriceForList = new ListOfPrice();
            sharePriceForList = sharePriceForList.getShareFoListOfPrice(priceList.get(i));
            sharePriceForListRepository.save(sharePriceForList);
        }
        return priceList;
    }
    @Transactional
    public List<SecidAndNameOfShare> showListOfShortNameShares() throws JsonProcessingException {
        List<SecidAndNameOfShare> shareList = new ArrayList<>();
        if (shareListRepository.findAll().isEmpty()) {
            String jsonList = restTemplate.getForObject("https://iss.moex.com/iss/history/engines/stock/markets/shares/boards/TQBR/securities.json?from=" + date1 + "&till=" + date2 + "&iss.meta=off&iss.only=history&history.columns=SHORTNAME,SECID", String.class);
            ListListSecidAndNameOfShare listShare22 = objectMapper.readValue(jsonList, ListListSecidAndNameOfShare.class);
            ListSecidAndNameOfShare listShar = listShare22.getListShare();
            shareList = listShar.getShares();
            shareListRepository.saveAll(shareList);
        } else {
            shareList = shareListRepository.findAll();
        }

        return shareList;
    }

    public double sumOfTotalCoast(List<SharePrice> sharePrices) {
        double sum = 0;
        for (int i = 0; i < sharePrices.size(); i++) {
            sum += sharePrices.get(i).getTotalCost();
        }
        BigDecimal result = new BigDecimal(sum);
        result = result.setScale(2, RoundingMode.HALF_UP);
        sum = result.doubleValue();
        return sum;
    }
    @Transactional
    public void addUserShares(SharePrice sharePrice) {
        if (sharePriceRepository.findAll().isEmpty()) {
            sharePriceRepository.save(sharePrice);
        } else {
            List<SharePrice> sharePrices1 = sharePriceRepository.findAll();
            for (int i = 0; i < sharePrices1.size(); i++) {
                if (sharePrices1.get(i).getShortName().equals(sharePrice.getShortName()) && (sharePrices1.get(i).getDate().equals(sharePrice.getDate()))) {
                    int a = sharePrices1.get(i).getCount();
                    int b = sharePrice.getCount();
                    sharePrice.setCount(a + b);
                    sharePrice.setTotalCost();
                    sharePriceRepository.delete(sharePrices1.get(i));
                    sharePriceRepository.save(sharePrice);
                } else sharePriceRepository.save(sharePrice);
            }
        }
    }
    @Transactional
    public List<SharePrice> getUserShares() {
        if (sharePriceRepository.findAll().isEmpty()) {
            return sharePriceRepository.findAll();
        } else {
            List<ListOfPrice> sharePriceNow = sharePriceForListRepository.findAll();
            List<SharePrice> sharePriceNowToUser = new ArrayList<>();
            for (int i = 0; i < sharePriceNow.size(); i++) {
                sharePriceNowToUser.add(SharePrice.convertToSharePrice(sharePriceNow.get(i)));
            }
            List<SharePrice> userShareOld = sharePriceRepository.findAll();
            if (!userShareOld.get(0).getDate().equals(sharePriceNowToUser.get(0).getDate())) {
                for (int i = 0; i < userShareOld.size(); i++) {
                    for (int j = 0; j < sharePriceNowToUser.size(); j++) {
                        if (userShareOld.get(i).getSecid().equals(sharePriceNowToUser.get(j).getSecid())) {
                            userShareOld.get(i).setOldPrice(userShareOld.get(i).getPrice());
                            userShareOld.get(i).setPrice(sharePriceNowToUser.get(j).getPrice());
                            userShareOld.get(i).setDifference();
                            userShareOld.get(i).setTotalCost();
                            sharePriceRepository.save(userShareOld.get(i));
                        }
                    }
                }
            }

            return userShareOld;
        }
    }
    @Transactional
    public List<ListOfPrice> getListOfSharesPrice() {
        return sharePriceForListRepository.findAll();
    }
    @Transactional
    public void deleteShare(SharePrice sharePrice, int x) {
        sharePrice.setCount(x);
        List<SharePrice> sharePrices = sharePriceRepository.findAll();
        for (int i = 0; i < sharePrices.size(); i++) {
            if (sharePrices.get(i).getShortName().equals(sharePrice.getShortName()) && (sharePrices.get(i).getDate().equals(sharePrice.getDate()))) {
                int a = sharePrices.get(i).getCount();
                int b = sharePrice.getCount();
                if (a - b < 0) {
                    log.info("Erro");
                } else sharePrice.setCount(a - b);
                if (sharePrice.getCount() == 0) {
                    sharePriceRepository.delete(sharePrices.get(i));
                } else {
                    sharePrice.setTotalCost();
                    sharePriceRepository.delete(sharePrices.get(i));
                    sharePriceRepository.save(sharePrice);
                }
            }
        }
    }
    @Transactional
    public void saveNewPrices(List<SharePrice> newPrices) {
        List<ListOfPrice> oldPrices=sharePriceForListRepository.findAll();
        List<ListOfPrice> Prices=new ArrayList<>();
        for (int i = 0; i < newPrices.size(); i++) {
            Prices.add(new ListOfPrice().getShareFoListOfPrice(newPrices.get(i)));
        }

        for (ListOfPrice x:oldPrices
        ) {sharePriceForListRepository.delete(x);
            System.out.println("delete");
        }
        for (ListOfPrice x:Prices
        ) {sharePriceForListRepository.save(x);
            System.out.println("save2");
        }
    }
    public SharePriceDTO convertToShareDTO(SharePrice sharePrice){
        ModelMapper mapper=new ModelMapper();

        return mapper.map(sharePrice,SharePriceDTO.class);
    }

    public SharePrice convertToSharePrice(SharePriceDTO sharePriceDTO){
        ModelMapper mapper=new ModelMapper();

        return mapper.map(sharePriceDTO,SharePrice.class);
    }

    public UserSharePriceDTO convertToUserSharePriceDTO(SharePrice sharePrice){
        ModelMapper mapper=new ModelMapper();

        return mapper.map(sharePrice,UserSharePriceDTO.class);
    }
}
