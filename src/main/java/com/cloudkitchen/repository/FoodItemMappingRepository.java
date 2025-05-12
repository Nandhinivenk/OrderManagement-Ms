package com.cloudkitchen.repository;

import com.cloudkitchen.model.FoodItemMapping;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for FoodItemMapping
 */
public interface FoodItemMappingRepository {
    
    /**
     * Save a food item mapping
     * 
     * @param mapping The food item mapping to save
     * @return The saved food item mapping with ID
     */
    FoodItemMapping save(FoodItemMapping mapping);
    
    /**
     * Find a food item mapping by ID
     * 
     * @param id The ID of the food item mapping
     * @return An Optional containing the food item mapping if found, or empty if not found
     */
    Optional<FoodItemMapping> findById(int id);
    
    /**
     * Find all food item mappings
     * 
     * @return A list of all food item mappings
     */
    List<FoodItemMapping> findAll();
    
    /**
     * Find all food item mappings by food item ID
     * 
     * @param foodItemId The ID of the food item
     * @return A list of food item mappings for the specified food item
     */
    List<FoodItemMapping> findByFoodItemId(int foodItemId);
    
    /**
     * Find all food item mappings by category ID
     * 
     * @param categoryId The ID of the category
     * @return A list of food item mappings for the specified category
     */
    List<FoodItemMapping> findByCategoryId(int categoryId);
    
    /**
     * Update a food item mapping
     * 
     * @param mapping The food item mapping to update
     * @return The updated food item mapping
     */
    FoodItemMapping update(FoodItemMapping mapping);
    
    /**
     * Delete a food item mapping by ID
     * 
     * @param id The ID of the food item mapping to delete
     * @return true if the food item mapping was deleted, false otherwise
     */
    boolean deleteById(int id);
    
    /**
     * Delete all food item mappings for a food item
     * 
     * @param foodItemId The ID of the food item
     * @return true if the food item mappings were deleted, false otherwise
     */
    boolean deleteByFoodItemId(int foodItemId);
    
    /**
     * Delete all food item mappings for a category
     * 
     * @param categoryId The ID of the category
     * @return true if the food item mappings were deleted, false otherwise
     */
    boolean deleteByCategoryId(int categoryId);
}
