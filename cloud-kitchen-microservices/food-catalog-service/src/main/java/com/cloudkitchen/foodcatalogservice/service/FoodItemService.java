package com.cloudkitchen.foodcatalogservice.service;

import com.cloudkitchen.foodcatalogservice.dto.FoodItemDTO;

import java.util.List;

public interface FoodItemService {
    
    FoodItemDTO createFoodItem(FoodItemDTO foodItemDTO);
    
    FoodItemDTO getFoodItemById(Long id);
    
    List<FoodItemDTO> getAllFoodItems();
    
    List<FoodItemDTO> getFoodItemsByCategory(Long categoryId);
    
    List<FoodItemDTO> getAvailableFoodItems();
    
    List<FoodItemDTO> searchFoodItems(String keyword);
    
    FoodItemDTO updateFoodItem(Long id, FoodItemDTO foodItemDTO);
    
    FoodItemDTO updateFoodItemAvailability(Long id, boolean available);
    
    void deleteFoodItem(Long id);
}
