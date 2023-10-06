package ru.ev.VirtualStockExchangeServer.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.ev.VirtualStockExchangeServer.models.SharePrice.GroupOfSharePrice;
import ru.ev.VirtualStockExchangeServer.models.User.User;
import ru.ev.VirtualStockExchangeServer.repositories.ListOfPriceRepository;
import ru.ev.VirtualStockExchangeServer.repositories.SecidAndNameOfShareRepository;
import ru.ev.VirtualStockExchangeServer.repositories.SharePriceRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MainServiceTest {
    @Mock
    private  User user;
    @Mock
    private  ObjectMapper objectMapper;
    @Mock
    private  SharePriceRepository sharePriceRepository;
    @Mock
    private ListOfPriceRepository listOfPriceRepository;
    @Mock
    private SecidAndNameOfShareRepository secidAndNameOfShareRepository;
    @InjectMocks
    private MainService mainService;


    @Test
    void getDate() {
        String date="2014-06-09";
        when(user.getDate()).thenReturn(date);
        assertEquals(mainService.getDate(),date);
    }


    @Test
    void getNowDate() {
        String date="2014-06-09";
        when(user.getDate()).thenReturn(date);
        assertEquals(LocalDate.parse(user.getDate()),mainService.getNowDate());
    }

    @Test
    void showPrice_ifGroupOfPriceIsEmpty_returnNull()  {

    }

    @Test
    void getGroupSharePriceFromMoex() {
    }

    @Test
    void showListOfShortNameShares() {
    }

    @Test
    void sumOfTotalCoast() {
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

    @Test
    void reset() {
    }

    @Test
    void getBankAccount() {
    }
}