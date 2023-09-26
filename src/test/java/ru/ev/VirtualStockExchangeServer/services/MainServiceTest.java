package ru.ev.VirtualStockExchangeServer.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;
import ru.ev.VirtualStockExchangeServer.models.SharePrice.SharePrice;
import ru.ev.VirtualStockExchangeServer.models.User.User;
import ru.ev.VirtualStockExchangeServer.repositories.ShareListRepository;
import ru.ev.VirtualStockExchangeServer.repositories.SharePriceForListRepository;
import ru.ev.VirtualStockExchangeServer.repositories.SharePriceRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
class MainServiceTest {
    @Mock
    ShareListRepository shareListRepository;
    @Mock
    SharePriceForListRepository sharePriceForListRepository;
    @Mock
    SharePriceRepository sharePriceRepository;
    @Mock
    ObjectMapper objectMapper;
    @Mock
    MockMvc mockMvc;
    @Mock
    RestTemplate template;
    @InjectMocks
    MainService mainService;

    @Test
    void getDate() {


    }

    @Test
    void setDate() {

    }

    @Test
    void startDate() {
        User userShares=new User();
        String date="2020-06-09";
        userShares.setDate("2020-06-09");
        assertEquals(date,userShares.getDate());
    }

    @Test
    void showPriceTest_ifJsonIsEmpty_ReturnException() {

    }

    @Test
    void showListOfShortNameShares() {
    }

    @Test
    void sumOfTotalCoast() {
        SharePrice one=Mockito.mock(SharePrice.class);
        when(one.getTotalCost()).thenReturn(10.25);
        SharePrice two=Mockito.mock(SharePrice.class);
        when(two.getTotalCost()).thenReturn(20.25);
        List<SharePrice> list=Arrays.asList(one,two);
        assertEquals(30.50,mainService.sumOfTotalCoast(list));


    }

    @Test
    void addUserShares() {
    }

    @Test
    void getUserShares() {
    }

    @Test
    void getListOfSharesPrice() {
    }

    @Test
    void deleteShare() {
    }

    @Test
    void saveNewPrices() {
    }

    @Test
    void convertToShareDTO() {
    }

    @Test
    void convertToSharePrice() {
    }

    @Test
    void convertToUserSharePriceDTO() {
    }
}