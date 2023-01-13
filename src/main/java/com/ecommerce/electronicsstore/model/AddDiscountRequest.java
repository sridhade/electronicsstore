package com.ecommerce.electronicsstore.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddDiscountRequest {
    private long productId;
    private String discountCode;
    private Double discountPercent;
}
