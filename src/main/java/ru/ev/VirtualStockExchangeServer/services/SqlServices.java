//package ru.ev.VirtualStockExchangeServer.services;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import ru.ev.VirtualStockExchangeServer.models.SharePrice.SharePrice;
//import ru.ev.VirtualStockExchangeServer.models.SharePriceForList.SharePriceForList;
//import ru.ev.VirtualStockExchangeServer.repositories.SharePriceForListRepository;
//import ru.ev.VirtualStockExchangeServer.repositories.SharePriceRepository;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//
//public class SqlServices {
//    @Autowired
//    private final SharePriceRepository sharePriceRepository;
//    private final SharePriceForListRepository sharePriceForListRepository;
//    public void adduserShares(SharePrice sharePrice) {
//        if(sharePriceRepository.findAll().isEmpty()){
//            sharePriceRepository.save(sharePrice);
//        }
//
//        else {
//            List<SharePrice> sharePrices1 = sharePriceRepository.findAll();
//            for (int i = 0; i < sharePrices1.size(); i++) {
//                if (sharePrices1.get(i).getShortName().equals(sharePrice.getShortName()) && (sharePrices1.get(i).getDate().equals(sharePrice.getDate()))) {
//                    int a = sharePrices1.get(i).getCount();
//                    int b = sharePrice.getCount();
//                    sharePrice.setCount(a + b);
//                    sharePrice.setTotalCost();
//                    sharePriceRepository.delete(sharePrices1.get(i));
//                    sharePriceRepository.save(sharePrice);
//
//                }
//                else sharePriceRepository.save(sharePrice);
//            }
//        }
//    }
//
//    public List<SharePrice> getSharesUser() {
//        if(sharePriceRepository.findAll().isEmpty()){
//            return sharePriceRepository.findAll();
//        }
//        else {
//            List<SharePriceForList> sharePriceNow = sharePriceForListRepository.findAll();
//            List<SharePrice> sharePriceNowToUser = new ArrayList<>();
//            for (int i = 0; i < sharePriceNow.size(); i++) {
//                sharePriceNowToUser.add(SharePrice.convertToSharePrice(sharePriceNow.get(i)));
//            }
//            List<SharePrice> userShareOld = sharePriceRepository.findAll();
//            if (!userShareOld.get(0).getDate().equals(sharePriceNowToUser.get(0).getDate())) {
//                for (int i = 0; i < userShareOld.size(); i++) {
//                    for (int j = 0; j < sharePriceNowToUser.size(); j++) {
//                        if (userShareOld.get(i).getSecid().equals(sharePriceNowToUser.get(j).getSecid())) {
//                            userShareOld.get(i).setOldPrice(userShareOld.get(i).getPrice());
//                            userShareOld.get(i).setPrice(sharePriceNowToUser.get(j).getPrice());
//                            userShareOld.get(i).setDifference();
//                            userShareOld.get(i).setTotalCost();
//                            sharePriceRepository.save(userShareOld.get(i));
//                        }
//                    }
//                }
//            }
//
//            return userShareOld;
//        }
//    }
//
//    public List<SharePriceForList> getListofSharesPrice(){
//        return sharePriceForListRepository.findAll();
//    }
//
//
//    public void deleteShare(SharePrice sharePrice,int x) {
//        sharePrice.setCount(x);
//        List<SharePrice> sharePrices=sharePriceRepository.findAll();
//        for (int i = 0; i < sharePrices.size(); i++) {
//            if (sharePrices.get(i).getShortName().equals(sharePrice.getShortName()) && (sharePrices.get(i).getDate().equals(sharePrice.getDate()))){
//                int a = sharePrices.get(i).getCount();
//                int b = sharePrice.getCount();
//                if(a-b<0){
//                    log.info("Erro");
//                }
//                else  sharePrice.setCount(a-b);
//                if(sharePrice.getCount()==0){
//                    sharePriceRepository.delete(sharePrices.get(i));
//                }
//                else {
//                sharePrice.setTotalCost();
//                sharePriceRepository.delete(sharePrices.get(i));
//                sharePriceRepository.save(sharePrice);
//                }
//            }
//
//        }
//    }
//
//
//    public void saveNewPrices(List<SharePrice> newPrices) {
//        List<SharePriceForList> oldPrices=sharePriceForListRepository.findAll();
//        List<SharePriceForList> Prices=new ArrayList<>();
//        for (int i = 0; i < newPrices.size(); i++) {
//            Prices.add(new SharePriceForList().getShareFoListOfPrice(newPrices.get(i)));
//        }
//
//        for (SharePriceForList x:oldPrices
//             ) {sharePriceForListRepository.delete(x);
//            System.out.println("delete");
//        }
//        for (SharePriceForList x:Prices
//        ) {sharePriceForListRepository.save(x);
//            System.out.println("save2");
//        }
//    }
//}
