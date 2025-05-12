package com.cloudkitchen.service;

import com.cloudkitchen.model.FoodItemMapping;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for FoodItemMapping
 */
public interface FoodItemMappingService {
    
    /**
     * Create a new food item mapping
     * 
     * @param foodItemId The ID of the food item
     * @param categoryId The ID of the category
     * @return The created food item mapping with ID
     */
    FoodItemMapping createMapping(int foodItemId, int categoryId);
    
    /**
     * Get a food item mapping by ID
     * 
     * @param id The ID of the food item mapping
     * @return An Optional containing the food item mapping if found, or empty if not found
     */
    Optional<FoodItemMapping> getMappingById(int id);
    
    /**
     * Get all food item mappings
     * 
     * @return A list of all food item mappings
     */
    List<FoodItemMapping> getAllMappings();
    
    /**
     * Get all food item mappings for a food item
     * 
     * @param foodItemId The ID of the food item
     * @return A list of food item mappings for the specified food item
     */
    List<FoodItemMapping> getMappingsByFoodItemId(int foodItemId);
    
    /**
     * Get all food item mappings for a category
     * 
     * @param categoryId The ID of the category
     * @return A list of food item mappings for the specified category
     */
    List<FoodItemMapping> getMappingsByCategoryId(int categoryId);
    
    /**
     * Update a food item mapping
     * 
     * @param mappingId The ID of the food item mapping
     * @param foodItemId The new food item ID
     * @param categoryId The new category ID
     * @return The updated food item mapping
     */
    FoodItemMapping updateMapping(int mappingId, int foodItemId, int categoryId);
    
    /**
     * Delete a food item mapping
     * 
     * @param mappingId The ID of the food item mapping to delete
     * @return true if the food item mapping was deleted, false otherwise
     */
    boolean deleteMapping(int mappingId);
    
    /**
     * Delete all food item mappings for a food item
     * 
     * @param foodItemId The ID of the food item
     * @return true if the food item mappings were deleted, false otherwise
     */
    boolean deleteMappingsByFoodItemId(int foodItemId);
    
    /**
     * Delete all food item mappings for a category
     * 
     * @param categoryId The ID of the category
     * @return true if the food item mappings were deleted, false otherwise
     */
    boolean deleteMappingsByCategoryId(int categoryId);
}
