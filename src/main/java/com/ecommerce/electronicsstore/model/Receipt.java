package com.ecommerce.electronicsstore.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Receipt {
    private List<ReceiptItem> items;
    private Double total;
    private Double totalDiscount;
    private Double totalAfterDiscount;
}
