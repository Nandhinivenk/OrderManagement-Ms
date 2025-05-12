package com.cloudkitchen.repository;

import com.cloudkitchen.model.FoodItem;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for FoodItem
 */
public interface FoodItemRepository {
    
    /**
     * Save a food item
     * 
     * @param foodItem The food item to save
     * @return The saved food item with ID
     */
    FoodItem save(FoodItem foodItem);
    
    /**
     * Find a food item by ID
     * 
     * @param id The ID of the food item
     * @return An Optional containing the food item if found, or empty if not found
     */
    Optional<FoodItem> findById(int id);
    
    /**
     * Find a food item by name
     * 
     * @param name The name of the food item
     * @return An Optional containing the food item if found, or empty if not found
     */
    Optional<FoodItem> findByName(String name);
    
    /**
     * Find all food items
     * 
     * @return A list of all food items
     */
    List<FoodItem> findAll();
    
    /**
     * Find all food items by category
     * 
     * @param category The category of the food items
     * @return A list of food items in the specified category
     */
    List<FoodItem> findByCategory(String category);
    
    /**
     * Find all available food items
     * 
     * @return A list of all available food items
     */
    List<FoodItem> findAvailable();
    
    /**
     * Update a food item
     * 
     * @param foodItem The food item to update
     * @return The updated food item
     */
    FoodItem update(FoodItem foodItem);
    
    /**
     * Delete a food item by ID
     * 
     * @param id The ID of the food item to delete
     * @return true if the food item was deleted, false otherwise
     */
    boolean deleteById(int id);
}
