package com.ecommerce.electronicsstore.service;

import com.ecommerce.electronicsstore.config.Constants;
import com.ecommerce.electronicsstore.entity.BasketItem;
import com.ecommerce.electronicsstore.entity.Discount;
import com.ecommerce.electronicsstore.repository.DiscountRepository;
import org.springframework.stereotype.Service;
@Service
public class DiscountService {
    private DiscountRepository discountRepository;


    public DiscountService(DiscountRepository discountRepository) {
        this.discountRepository = discountRepository;
    }

    public Discount addDiscount(Discount discount) {
        return discountRepository.save(discount);
    }

    public Discount getDiscountByProductId(Long productId) {
        return discountRepository.findByProductId(productId);
    }


    public Double calculateDiscount(BasketItem item) {
                double itemDiscount = 0.0;

                Discount discount = getDiscountByProductId(item.getProduct().getId());
                if (discount != null) {
                    int quantity = item.getQuantity();

                    if (discount.getDiscountCode().equalsIgnoreCase(Constants.BUY_1_GET_50_OFF_2_ND)) {
                        if (quantity > 1) {
                            itemDiscount = (quantity / 2) * (item.getProduct().getPrice() * (discount.getDiscountPercent() / 100));
                        }
                    } else if (discount.getDiscountCode().equalsIgnoreCase(Constants.BUY_3_GET_25_OFF)) {
                        if (quantity >= 3) {
                            itemDiscount = (item.getProduct().getPrice() * (discount.getDiscountPercent() / 100)) * quantity;
                        }
                    } else if  (discount.getDiscountCode().equalsIgnoreCase(Constants.BUY_1_GET_1)) {
                        if (quantity >= 1) {
                            itemDiscount = (quantity / 2) * item.getProduct().getPrice();
                        }
                    }
                }
                return  itemDiscount;


    }


}
