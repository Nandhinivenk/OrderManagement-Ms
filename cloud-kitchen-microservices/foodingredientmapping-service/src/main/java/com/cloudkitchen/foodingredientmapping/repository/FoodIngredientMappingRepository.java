package com.cloudkitchen.foodingredientmapping.repository;

import com.cloudkitchen.foodingredientmapping.model.FoodIngredientMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FoodIngredientMappingRepository extends JpaRepository<FoodIngredientMapping, Long> {
    
    List<FoodIngredientMapping> findByFoodItemId(Long foodItemId);
    
    List<FoodIngredientMapping> findByInventoryItemId(Long inventoryItemId);
    
    Optional<FoodIngredientMapping> findByFoodItemIdAndInventoryItemId(Long foodItemId, Long inventoryItemId);
}
