package com.ecommerce.electronicsstore.service;

import com.ecommerce.electronicsstore.entity.Basket;
import com.ecommerce.electronicsstore.entity.BasketItem;
import com.ecommerce.electronicsstore.model.Receipt;
import com.ecommerce.electronicsstore.model.ReceiptItem;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class ReceiptService {

    public static final double ZERO = 0.0;


    public ReceiptService(DiscountService discountService) {
        this.discountService = discountService;
    }

    private final DiscountService discountService;

    public Receipt calculateReceipt(Basket basket) {
        double total = ZERO;
        double totalDiscount = ZERO;
        double itemDiscount;
        List<ReceiptItem> receiptItems = new ArrayList<>();
        for (BasketItem item : basket.getItems()) {
            double itemTotal = item.getProduct().getPrice() * item.getQuantity();
            total += itemTotal;
            itemDiscount = discountService.calculateDiscount(item);
            totalDiscount += itemDiscount;
            receiptItems.add(
                        ReceiptItem.builder()
                                .product(item.getProduct())
                                .itemQuantity(item.getQuantity())
                                .itemTotal(itemTotal)
                                .itemDiscount(itemDiscount)
                                .itemTotalAfterDiscount((itemTotal-itemDiscount))
                                .build()
                            );

        }
        return Receipt.builder()
                .items(receiptItems)
                .total(total)
                .totalDiscount(totalDiscount)
                .totalAfterDiscount((total - totalDiscount))
                .build();


    }

}
