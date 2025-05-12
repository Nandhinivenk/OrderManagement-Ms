package com.cloudkitchen.kitchenflowservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDTO {
    
    private Long id;
    private Long foodItemId;
    private String foodItemName;
    private int quantity;
    private BigDecimal price;
    private BigDecimal subtotal;
}
