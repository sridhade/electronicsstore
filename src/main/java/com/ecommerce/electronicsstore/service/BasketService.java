package com.ecommerce.electronicsstore.service;

import com.ecommerce.electronicsstore.entity.Basket;
import com.ecommerce.electronicsstore.entity.BasketItem;
import com.ecommerce.electronicsstore.entity.Product;
import com.ecommerce.electronicsstore.exception.BasketNotFoundException;
import com.ecommerce.electronicsstore.exception.ProductNotFoundException;
import com.ecommerce.electronicsstore.repository.BasketRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Service
public class BasketService {

    private final ProductService productService;
    private final BasketRepository basketRepository;

    public BasketService(ProductService productService, BasketRepository basketRepository) {
        this.productService = productService;
        this.basketRepository=basketRepository;

    }

    public Basket addItemToBasket(long productId, int quantity, long basketId) throws ProductNotFoundException, BasketNotFoundException {
        Product product = productService.getProductById(productId);

        Basket basket;
        if (basketId == 0) {
            basket = new Basket();
            basket.setItems(new ArrayList<>());
            basket.setTotalPrice(0);
        }
        else {
            basket = getBasketById(basketId);
        }

        Optional<BasketItem> existingItem = basket.getItems().stream().filter(item -> item.getProduct().getId() == productId).findFirst();
        if (existingItem.isPresent()) {
            BasketItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
            item.setPrice(item.getProduct().getPrice() * item.getQuantity());
        } else {
            BasketItem item = new BasketItem();
            item.setProduct(product);
            item.setQuantity(quantity);
            item.setPrice(product.getPrice() * quantity);
            basket.getItems().add(item);
        }

        updateBasketTotalPrice(basket);
        basketRepository.save(basket);
        return basket;
    }

    public Basket getBasketById(long id)  throws BasketNotFoundException {
       return basketRepository.findById(id).orElseThrow(() -> new BasketNotFoundException("Basket not found with id " + id));
    }
    public Basket removeItemFromBasket(long productId, long basketId)
       throws BasketNotFoundException, ProductNotFoundException {

        Product product = productService.getProductById(productId);
        Basket basket = getBasketById(basketId);

        List<BasketItem> items = basket.getItems();
        Predicate<BasketItem> basketItemPredicate = item -> item.getProduct().getId() == product.getId();
        items.removeIf(basketItemPredicate);

        updateBasketTotalPrice(basket);
        basketRepository.save(basket);
        return basket;
    }

    public Basket updateBasketTotalPrice(Basket basket) {
        double totalPrice = 0;
        for (BasketItem item : basket.getItems()) {
            totalPrice += item.getPrice();
        }
        basket.setTotalPrice(totalPrice);
        return basket;
    }
}