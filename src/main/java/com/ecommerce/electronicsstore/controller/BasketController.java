package com.ecommerce.electronicsstore.controller;

import com.ecommerce.electronicsstore.entity.Basket;
import com.ecommerce.electronicsstore.model.AddBasketItemRequest;
import com.ecommerce.electronicsstore.model.RemoveBasketItemRequest;
import com.ecommerce.electronicsstore.service.BasketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/basket")
public class BasketController {

    private final BasketService basketService;

    public BasketController(BasketService basketService) {
        this.basketService = basketService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Basket> getBasketById(@PathVariable long id) {
        Basket basket = basketService.getBasketById(id);
        if (basket == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(basket, HttpStatus.OK);
    }

    @PostMapping("/add-item")
    public ResponseEntity<Basket> addProductToBasket(@RequestBody AddBasketItemRequest request) {
        Basket basket = basketService.addItemToBasket(
                request.getProductId(),
                request.getQuantity(), request.getBasketId());
        return new ResponseEntity<>(basket, HttpStatus.OK);

    }

    @DeleteMapping("/remove-item")
    public ResponseEntity<Basket> removeItemFromBasket(@RequestBody RemoveBasketItemRequest request) {
        Basket basket = basketService.removeItemFromBasket(request.getProductId(), request.getBasketId());
        return new ResponseEntity<>(basket, HttpStatus.OK);
    }

}
