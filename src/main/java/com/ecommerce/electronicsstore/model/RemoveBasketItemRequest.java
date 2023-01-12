package com.ecommerce.electronicsstore.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RemoveBasketItemRequest {

        private long productId;
        private long basketId;
}
