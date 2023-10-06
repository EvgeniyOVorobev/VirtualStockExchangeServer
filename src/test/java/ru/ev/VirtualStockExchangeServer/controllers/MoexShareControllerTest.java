package ru.ev.VirtualStockExchangeServer.controllers;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import ru.ev.VirtualStockExchangeServer.services.MainService;


import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@WebMvcTest
class MoexShareControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MoexShareController moexShareController;
    @MockBean
    private MainService mainService;
    @Test
    void showShares() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name(("show")));

    }

    @Test
    void resetAll() {
    }

    @Test
    void buyShare() {
    }

    @Test
    void sellShare() {
    }

    @Test
    void setDate() {
    }

    @Test
    void getSharePrices() {
    }
}