package com.cloudkitchen.foodingredientmapping.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FoodIngredientMappingDTO {
    
    private Long id;
    
    @NotNull(message = "Food item ID is required")
    private Long foodItemId;
    
    @NotNull(message = "Inventory item ID is required")
    private Long inventoryItemId;
    
    @NotNull(message = "Quantity required is required")
    @Positive(message = "Quantity required must be positive")
    private Double quantityRequired;
    
    private String foodItemName;
    private String inventoryItemName;
    private String inventoryItemUnit;
}
