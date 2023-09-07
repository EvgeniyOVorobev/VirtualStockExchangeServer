//package ru.ev.VirtualStockExchangeServer.services;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.PropertySource;
//import org.springframework.core.env.Environment;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.client.RestTemplate;
//import ru.ev.VirtualStockExchangeServer.models.SharePrice.ListListSharePrice;
//import ru.ev.VirtualStockExchangeServer.models.SharePrice.ListSharePrice;
//import ru.ev.VirtualStockExchangeServer.models.SharePrice.SharePrice;
//import ru.ev.VirtualStockExchangeServer.models.SharePriceForList.SharePriceForList;
//import ru.ev.VirtualStockExchangeServer.models.SharesList.ListListShare;
//import ru.ev.VirtualStockExchangeServer.models.SharesList.ListShare;
//import ru.ev.VirtualStockExchangeServer.models.SharesList.Share;
//import ru.ev.VirtualStockExchangeServer.models.User.UserShares;
//import ru.ev.VirtualStockExchangeServer.repositories.ShareListRepository;
//import ru.ev.VirtualStockExchangeServer.repositories.SharePriceForListRepository;
//
//import java.math.BigDecimal;
//import java.math.RoundingMode;
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//@PropertySource("classpath:URL.properties")
//public class MoexService {
//    @Autowired
//    private final ShareListRepository shareListRepository;
//    private final SharePriceForListRepository sharePriceForList1;
//    private final Environment environment;
//
//    UserShares userShares=new UserShares();
//
//
//    ObjectMapper objectMapper = new ObjectMapper();
//
//    RestTemplate restTemplate = new RestTemplate();
//
//
//     String a = environment.getProperty("urlForListOfPrice1");
//
//    String c = environment.getProperty("urlForListOfPrice2");
//    String date1 = "2014-06-09";
//
//    String ca = environment.getProperty("urlForListOfPrice3");
//
//    String date2 = "2014-06-09";
//    String de = environment.getProperty("urlForListOfPrice4");
//
//    public String getDate1() {
//        return date1;
//    }
//
//    public void setDate1(LocalDate date) {
//        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        String date1=date.format(dateTimeFormatter);
//        this.date1 = date1;
//        this.date2 = date1;
//        System.out.println(this.date1+""+this.date2);
//    }
//
//    public String getDate2() {
//        return date2;
//    }
//
//
//    public LocalDate StartDate(){
//         userShares.setDate(LocalDate.parse(date1));
//         return userShares.getDate();
//    }
//    @Transactional
//    public List<SharePrice> showPrice(String secid) throws JsonProcessingException {
//        String json = restTemplate.getForObject(a + secid + c + date1 +ca+date2+  de, String.class);
//        objectMapper.registerModule(new JavaTimeModule());
//        objectMapper.findAndRegisterModules();
//        ListListSharePrice listShare = objectMapper.readValue(json, ListListSharePrice.class);
//        ListSharePrice listShare1 = listShare.getListSharePrice();
//
//
//        List<SharePrice> priceList = new ArrayList<>();
//        if (listShare1.getSharesPrice().isEmpty()) {
//            return priceList;
//        } else if (listShare1.getSharesPrice().get(0).getPrice() == null) {
////                listShare1.getSharesPrice().get(0).setPrice(0.0);
////                priceList.add((listShare1.getSharesPrice().get(0)));
//                return priceList;
//                }
//             else priceList.add((listShare1.getSharesPrice().get(0)));
//
//
//            for (int i = 0; i < priceList.size(); i++) {
//                SharePriceForList sharePriceForList = new SharePriceForList();
//                sharePriceForList = sharePriceForList.getShareFoListOfPrice(priceList.get(i));
//                sharePriceForList1.save(sharePriceForList);
//
//                System.out.println("Save1");
//            }
//
//
//        return priceList;
//
//
//    }
//
//
//    public List<Share> showListOfShares() throws JsonProcessingException {
//        List<Share> shareList = new ArrayList<>();
//        if (shareListRepository.findAll().isEmpty()) {
//            String jsonList = restTemplate.getForObject("https://iss.moex.com/iss/history/engines/stock/markets/shares/boards/TQBR/securities.json?from="+date1+"&till="+date2+"&iss.meta=off&iss.only=history&history.columns=SHORTNAME,SECID", String.class);
//            ListListShare listShare22 = objectMapper.readValue(jsonList, ListListShare.class);
//            ListShare listShar = listShare22.getListShare();
//            shareList = listShar.getShares();
//            shareListRepository.saveAll(shareList);
//            System.out.println("запрос на биржу");
//        } else {
//            shareList = shareListRepository.findAll();
//            System.out.println("V bd");
//        }
//
//        return shareList;
//    }
//
//    public double sumOfTotalCoast(List<SharePrice> sharePrices){
//        double sum=0;
//        for (int i = 0; i < sharePrices.size(); i++) {
//            sum += sharePrices.get(i).getTotalCost();
//        }
//        BigDecimal result=new BigDecimal(sum);
//        result=result.setScale(2, RoundingMode.HALF_UP);
//        sum=result.doubleValue();
//        return sum;
//    }
//}
