package com.ecommerce.electronicsstore.service;

import com.ecommerce.electronicsstore.entity.Discount;
import com.ecommerce.electronicsstore.repository.DiscountRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class DiscountService {
    private DiscountRepository discountRepository;


    public DiscountService(DiscountRepository discountRepository) {
        this.discountRepository = discountRepository;
    }

    public Discount addDiscount(Discount discount) {
        return discountRepository.save(discount);
    }

    public List<Discount> getDiscountByProductId(Long productId) {
        return discountRepository.findByProductId(productId);
    }


}
