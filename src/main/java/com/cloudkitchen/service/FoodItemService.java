package com.cloudkitchen.service;

import com.cloudkitchen.model.FoodItem;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for FoodItem
 */
public interface FoodItemService {
    
    /**
     * Add a new food item
     * 
     * @param foodItem The food item to add
     * @return The added food item with ID
     */
    FoodItem addFoodItem(FoodItem foodItem);
    
    /**
     * Get a food item by ID
     * 
     * @param id The ID of the food item
     * @return An Optional containing the food item if found, or empty if not found
     */
    Optional<FoodItem> getFoodItemById(int id);
    
    /**
     * Get a food item by name
     * 
     * @param name The name of the food item
     * @return An Optional containing the food item if found, or empty if not found
     */
    Optional<FoodItem> getFoodItemByName(String name);
    
    /**
     * Get all food items
     * 
     * @return A list of all food items
     */
    List<FoodItem> getAllFoodItems();
    
    /**
     * Get all food items by category
     * 
     * @param category The category of the food items
     * @return A list of food items in the specified category
     */
    List<FoodItem> getFoodItemsByCategory(String category);
    
    /**
     * Get all available food items
     * 
     * @return A list of all available food items
     */
    List<FoodItem> getAvailableFoodItems();
    
    /**
     * Update a food item
     * 
     * @param foodItem The food item to update
     * @return The updated food item
     */
    FoodItem updateFoodItem(FoodItem foodItem);
    
    /**
     * Update the availability of a food item
     * 
     * @param id The ID of the food item
     * @param isAvailable The new availability status
     * @return The updated food item
     */
    FoodItem updateFoodItemAvailability(int id, boolean isAvailable);
    
    /**
     * Delete a food item by ID
     * 
     * @param id The ID of the food item to delete
     * @return true if the food item was deleted, false otherwise
     */
    boolean deleteFoodItem(int id);
}
