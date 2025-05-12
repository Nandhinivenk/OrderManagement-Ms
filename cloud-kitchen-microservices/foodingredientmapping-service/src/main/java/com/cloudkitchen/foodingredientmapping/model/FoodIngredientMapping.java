package com.cloudkitchen.foodingredientmapping.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "food_ingredient_mappings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FoodIngredientMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long foodItemId;
    
    @Column(nullable = false)
    private Long inventoryItemId;
    
    @Column(nullable = false)
    private Double quantityRequired; // how much stock needed per 1 food item
    
    // Additional fields for better tracking and management
    private String foodItemName;
    private String inventoryItemName;
    private String inventoryItemUnit;
}
