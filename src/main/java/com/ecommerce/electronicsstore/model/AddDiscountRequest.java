package com.ecommerce.electronicsstore.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddDiscountRequest {
    private long productId;
    private String discountCode;
}
