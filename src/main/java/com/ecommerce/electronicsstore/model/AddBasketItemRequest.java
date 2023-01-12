package com.ecommerce.electronicsstore.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddBasketItemRequest {
        private long productId;
        private int quantity;
        private long basketId;
}
