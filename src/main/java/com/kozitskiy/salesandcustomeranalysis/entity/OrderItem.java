package com.kozitskiy.salesandcustomeranalysis.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItem {
    private String productName;
    private int quantity;
    private double price;
    private Category category;
}
