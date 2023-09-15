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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import ru.ev.VirtualStockExchangeServer.DTO.SharePriceDTO;
import ru.ev.VirtualStockExchangeServer.DTO.UserSharePriceDTO;
import ru.ev.VirtualStockExchangeServer.Exeption.LimitRequestsException;
import ru.ev.VirtualStockExchangeServer.Exeption.RestTemplateResponseErrorHandler;
import ru.ev.VirtualStockExchangeServer.enums.Role;
import ru.ev.VirtualStockExchangeServer.models.SharePrice.ListListSharePrice;
import ru.ev.VirtualStockExchangeServer.models.SharePrice.ListSharePrice;
import ru.ev.VirtualStockExchangeServer.models.SharePrice.SharePrice;
import ru.ev.VirtualStockExchangeServer.models.SharePriceForList.ListOfPrice;
import ru.ev.VirtualStockExchangeServer.models.SharesList.ListListSecidAndNameOfShare;
import ru.ev.VirtualStockExchangeServer.models.SharesList.ListSecidAndNameOfShare;
import ru.ev.VirtualStockExchangeServer.models.SharesList.SecidAndNameOfShare;
import ru.ev.VirtualStockExchangeServer.models.User.User;
import ru.ev.VirtualStockExchangeServer.repositories.ShareListRepository;
import ru.ev.VirtualStockExchangeServer.repositories.SharePriceForListRepository;
import ru.ev.VirtualStockExchangeServer.repositories.SharePriceRepository;
import ru.ev.VirtualStockExchangeServer.repositories.UserRepository;

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
    private final SharePriceRepository sharePriceRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    private final PasswordEncoder passwordEncoder;

     private final User userShares = new User();

    @Value("${urlForListOfPrice1}")
   private String a ;
    @Value("${urlForListOfPrice2}")
    private String c ;
    private String date1 = "2014-06-09";
    @Value("${urlForListOfPrice3}")
    private String ca ;
    private String date2 = "2014-06-09";
    @Value("${urlForListOfPrice4}")
    private String de ;

    public String getDate() {
        return date1;
    }

    public String getDate2() {
        return date2;
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
        RestTemplate restTemplate = new RestTemplate();
        log.info("Request for share {}", secid);
        restTemplate.setErrorHandler(new RestTemplateResponseErrorHandler());
        String json = restTemplate.getForObject(a + secid + c + date1 + ca + date2 + de, String.class);
        if(json.isEmpty()){
            log.error("Moex isn't answering for getting prices.");
            throw new LimitRequestsException("Moex isn't answering for getting prices.");
        }
        log.info("Getting share from Moex");
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
        RestTemplate restTemplate = new RestTemplate();
        List<SecidAndNameOfShare> shareList = new ArrayList<>();
        if (shareListRepository.findAll().isEmpty()) {
            restTemplate.setErrorHandler(new RestTemplateResponseErrorHandler());
            String jsonList = restTemplate.getForObject("https://iss.moex.com/iss/history/engines/stock/markets/shares/boards/TQBR/securities.json?from=" + date1 + "&till=" + date2 + "&iss.meta=off&iss.only=history&history.columns=SHORTNAME,SECID", String.class);
            log.error("Getting short name of share");
            if(jsonList.isEmpty()){
                log.error("Moex isn't answering for getting secid.");
                throw new LimitRequestsException("Moex isn't answering for getting secid.");
            }
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
    public void deleteShare(SharePrice sharePrice, int x) throws RuntimeException {
        sharePrice.setCount(x);
        List<SharePrice> sharePrices = sharePriceRepository.findAll();
        for (int i = 0; i < sharePrices.size(); i++) {
            if (sharePrices.get(i).getShortName().equals(sharePrice.getShortName()) && (sharePrices.get(i).getDate().equals(sharePrice.getDate()))) {
                int a = sharePrices.get(i).getCount();
                int b = sharePrice.getCount();
                 if (a - b < 0) {
                    log.info("Error");
                    throw  new RuntimeException("Errorsses");
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
        }
        for (ListOfPrice x:Prices
        ) {sharePriceForListRepository.save(x);
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

    public List<SharePriceDTO> getSharePrice(List<SecidAndNameOfShare> shareList) throws JsonProcessingException {
        List<SharePrice> sharePrices = new ArrayList<>();
        List<ListOfPrice> sharePriceForLists = new ArrayList<>();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if (this.getListOfSharesPrice().isEmpty()) {
            for (int i = 0; i < shareList.size(); i++) {
                sharePrices.addAll(this.showPrice(shareList.get(i).getSecid()));
            }
        } else if (
                !this.getDate().equals(this.getListOfSharesPrice().get(0).getDate().format(dateTimeFormatter))) {
            List<SharePrice> newPrices = new ArrayList<>();
            for (int i = 0; i < shareList.size(); i++) {
                newPrices.addAll(this.showPrice(shareList.get(i).getSecid()));
            }
            this.saveNewPrices(newPrices);
            sharePrices = newPrices;
        } else {
            sharePriceForLists = this.getListOfSharesPrice();
            for (int i = 0; i < sharePriceForLists.size(); i++) {
                sharePrices.add(SharePrice.convertToSharePrice(sharePriceForLists.get(i)));
            }
        }
        List<SharePriceDTO> sharePriceDTOList = new ArrayList<>();
        for (int i = 0; i < sharePrices.size(); i++) {
            sharePriceDTOList.add(this.convertToShareDTO(sharePrices.get(i)));
        }


        return sharePriceDTOList;
    }

    public boolean createUser(User user){
        if(userRepository.findByName(user.getName())!=null)
            return false;
        user.getRoleSet().add(Role.ROLE_USER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        log.info("Saving new user with name {}",user.getName());
        userRepository.save(user);
        return true;
    }
}
