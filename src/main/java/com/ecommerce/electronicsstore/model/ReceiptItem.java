package com.ecommerce.electronicsstore.model;

import com.ecommerce.electronicsstore.entity.Product;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptItem {
    private Product product;
    private Integer itemQuantity;
    private Double itemTotal;
    private Double itemDiscount;
    private Double itemTotalAfterDiscount;
}
