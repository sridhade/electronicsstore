package com.ecommerce.electronicsstore.controller;

import com.ecommerce.electronicsstore.entity.Discount;
import com.ecommerce.electronicsstore.model.AddDiscountRequest;
import com.ecommerce.electronicsstore.service.DiscountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/discounts")
public class DiscountController {
    private final DiscountService discountService;

    public DiscountController(DiscountService discountService) {
        this.discountService = discountService;
    }

    @PostMapping
    public ResponseEntity<Discount> addDiscount(@RequestBody AddDiscountRequest discountRequest) {

            Discount discount = discountService.addDiscount(
                    Discount.builder()
                            .discountCode(discountRequest.getDiscountCode())
                            .productId(discountRequest.getProductId())
                            .discountPercent(discountRequest.getDiscountPercent())
                            .build()
                );
            return new ResponseEntity<>(discount, HttpStatus.CREATED);
        }
  }
