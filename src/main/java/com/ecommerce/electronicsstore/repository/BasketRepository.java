package com.ecommerce.electronicsstore.repository;

import com.ecommerce.electronicsstore.entity.Basket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BasketRepository extends JpaRepository<Basket, Long> {
}