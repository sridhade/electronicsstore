package com.ecommerce.electronicsstore.repository;


import com.ecommerce.electronicsstore.entity.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Long> {
    List<Discount> findByProductId(Long productId);

}