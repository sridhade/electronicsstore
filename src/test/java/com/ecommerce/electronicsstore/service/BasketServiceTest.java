package com.ecommerce.electronicsstore.service;

import com.ecommerce.electronicsstore.entity.Basket;
import com.ecommerce.electronicsstore.entity.BasketItem;
import com.ecommerce.electronicsstore.entity.Product;
import com.ecommerce.electronicsstore.exception.BasketNotFoundException;
import com.ecommerce.electronicsstore.exception.ProductNotFoundException;
import com.ecommerce.electronicsstore.repository.BasketRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;

@DisplayName("Basket Service Unit Tests")
class BasketServiceTest {
    @Mock
    ProductService productService;
    @Mock
    BasketRepository basketRepository;
    @InjectMocks
    BasketService basketService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void TestAddItemToBasket() throws ProductNotFoundException, BasketNotFoundException {
        when(productService.getProductById(anyLong()))
                .thenReturn(new
                        Product(Long.valueOf(1),
                        "name", Double.valueOf(200), "description"));



        Basket result = basketService.addItemToBasket(1L, 2, 0L);
        Assertions.assertEquals(400, result.getTotalPrice());
        Assertions.assertTrue(result.getItems().stream().filter(item -> item.getProduct().getId() == 1).count() > 0);

    }

    @Test
    void TestAddItemToBasket_ProductAlreadyExistsInBasket() throws ProductNotFoundException, BasketNotFoundException {
        when(productService.getProductById(anyLong()))
                .thenReturn(new
                        Product(Long.valueOf(1),
                        "name", Double.valueOf(200), "description"));

        when(basketRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(Basket.builder()
                        .id(1L)
                        .items(new ArrayList<>(Arrays.asList(
                                BasketItem.builder()
                                        .price(400)
                                        .quantity(2)
                                        .product(
                                                Product.builder().id(1L)
                                                        .price(200.0).build()
                                        )
                                        .build(),
                                BasketItem.builder()
                                        .price(400)
                                        .quantity(2)
                                        .product(
                                                Product.builder().id(2L)
                                                        .price(200.0)
                                                        .build()
                                        )
                                        .build()
                        )
                        ))
                        .totalPrice(800).build()));

        Basket result = basketService.addItemToBasket(1L, 2, 1L);
        result = basketService.addItemToBasket(1L, 1, result.getId());

        Assertions.assertEquals(1400, result.getTotalPrice());
        Assertions.assertTrue(result.getItems().stream().filter(item -> item.getProduct().getId() == 1).count() ==1);

    }

    @Test
    void TestGetBasketById() throws BasketNotFoundException {
        when(basketRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(Basket.builder()
                        .id(1L)
                        .items(new ArrayList<>(Arrays.asList(
                                BasketItem.builder()
                                        .price(400)
                                        .quantity(2)
                                        .product(
                                                Product.builder().id(1L)
                                                        .price(200.0).build()
                                        )
                                        .build(),
                                BasketItem.builder()
                                        .price(400)
                                        .quantity(2)
                                        .product(
                                                Product.builder().id(2L)
                                                        .price(200.0)
                                                        .build()
                                        )
                                        .build()
                        )
                        ))
                        .totalPrice(800).build()));
        Basket result = basketService.getBasketById(1L);
        Assertions.assertEquals(1L
                , result.getId());

        Assertions.assertEquals(800
                , result.getTotalPrice());
    }

    @Test
    void TestRemoveItemFromBasket() throws BasketNotFoundException, ProductNotFoundException {

        when(basketRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(Basket.builder()
                        .id(1L)
                        .items(new ArrayList<>(Arrays.asList(
                                        BasketItem.builder()
                                                .price(400)
                                                .quantity(2)
                                                .product(
                                                        Product.builder().id(1L)
                                                                .price(200.0).build()
                                                )
                                                .build(),
                                        BasketItem.builder()
                                                .price(400)
                                                .quantity(2)
                                                .product(
                                                        Product.builder().id(2L)
                                                                .price(200.0)
                                                                .build()
                                                )
                                                .build()
                                )
                        ))
                        .totalPrice(800).build()));


        Basket result = basketService.removeItemFromBasket(1L, 1L);
        Assertions.assertEquals(result.getTotalPrice(),400);

    }

    @Test
    void TestUpdateBasketTotalPrice() {
        Basket result = basketService.updateBasketTotalPrice(
                Basket.builder()
                        .id(1L)
                        .items(new ArrayList<>(Arrays.asList(
                                BasketItem.builder()
                                        .price(400)
                                        .quantity(2)
                                        .product(
                                                Product.builder().id(1L)
                                                        .price(200.0).build()
                                        )
                                        .build(),
                                BasketItem.builder()
                                        .price(400)
                                        .quantity(2)
                                        .product(
                                                Product.builder().id(2L)
                                                        .price(200.0)
                                                        .build()
                                        )
                                        .build()
                        )
                        ))
                        .totalPrice(800).build());

        Assertions.assertEquals(800,
                result.getTotalPrice());
    }
}
