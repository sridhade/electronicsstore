package com.ecommerce.electronicsstore.controller;

import com.ecommerce.electronicsstore.entity.Basket;
import com.ecommerce.electronicsstore.service.BasketService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(BasketController.class)
@ExtendWith(SpringExtension.class)
public class BasketControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private BasketService basketService;

    @Test
    public void TestAddItemToBasket_ExistingBasket() throws Exception {
        // given
        Basket basket = new Basket();
        basket.setId(1L);
        basket.setTotalPrice(1000);
        when(basketService.addItemToBasket(1L, 2, 1L)).thenReturn(basket);

        // when
        mvc.perform(post("/basket/add-item")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productId\":1,\"quantity\":2,\"basketId\":1}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{'id':1,'totalPrice':1000}"));

        verify(basketService, times(1)).addItemToBasket(1L,2,1L);
    }

    @Test
    public void TestAddItemToBasket_NewBasket() throws Exception {
        // given
        Basket basket = new Basket();
        basket.setId(1L);
        basket.setTotalPrice(1000);
        when(basketService.addItemToBasket(1L, 2, 0L)).thenReturn(basket);

        // when
        mvc.perform(post("/basket/add-item")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productId\":1,\"quantity\":2,\"basketId\":0}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{'id':1,'totalPrice':1000}"));

        verify(basketService, times(1)).addItemToBasket(1L,2,0L);
    }


    @Test
    public void TestRemoveItemFromBasket() throws Exception {
        // given
        Basket basket = new Basket();
        basket.setId(1L);
        basket.setTotalPrice(800);
        when(basketService.removeItemFromBasket(1L, 1L)).thenReturn(basket);

        // when
        mvc.perform(delete("/basket/remove-item")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productId\":1,\"basketId\":1}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{'id':1,'totalPrice':800}"));
        verify(basketService, times(1)).removeItemFromBasket(1L,1L);
    }
}
