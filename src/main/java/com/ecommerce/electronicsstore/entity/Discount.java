package com.ecommerce.electronicsstore.entity;


import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "discounts")
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "discount_code")
    private String discountCode;

}