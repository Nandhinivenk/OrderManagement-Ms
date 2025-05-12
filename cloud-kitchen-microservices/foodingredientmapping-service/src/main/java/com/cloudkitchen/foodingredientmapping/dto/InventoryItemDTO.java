package com.cloudkitchen.foodingredientmapping.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryItemDTO {
    
    private Long id;
    private String name;
    private int quantity;
    private String unit;
    private int reorderLevel;
    private String qrCodePath;
    private LocalDateTime lastUpdated;
}
