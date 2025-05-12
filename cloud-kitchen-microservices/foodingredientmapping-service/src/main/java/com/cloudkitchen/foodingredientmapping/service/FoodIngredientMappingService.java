package com.cloudkitchen.foodingredientmapping.service;

import com.cloudkitchen.foodingredientmapping.dto.FoodIngredientMappingDTO;

import java.util.List;
import java.util.Map;

public interface FoodIngredientMappingService {
    
    FoodIngredientMappingDTO addMapping(FoodIngredientMappingDTO mappingDTO);
    
    FoodIngredientMappingDTO getMappingById(Long id);
    
    List<FoodIngredientMappingDTO> getMappingsForFoodItem(Long foodItemId);
    
    List<FoodIngredientMappingDTO> getMappingsForInventoryItem(Long inventoryItemId);
    
    List<FoodIngredientMappingDTO> getAllMappings();
    
    FoodIngredientMappingDTO updateMapping(Long id, FoodIngredientMappingDTO mappingDTO);
    
    void deleteMapping(Long id);
    
    // Additional methods for business logic
    Map<Long, Double> calculateRequiredInventoryForOrder(Long foodItemId, int quantity);
    
    boolean checkInventoryAvailabilityForFoodItem(Long foodItemId, int quantity);
    
    void updateInventoryAfterOrderPlaced(Long foodItemId, int quantity);
}
