package com.cloudkitchen.repository;

import com.cloudkitchen.model.InventoryItem;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for InventoryItem
 */
public interface InventoryRepository {
    
    /**
     * Save an inventory item
     * 
     * @param item The inventory item to save
     * @return The saved inventory item with ID
     */
    InventoryItem save(InventoryItem item);
    
    /**
     * Find an inventory item by ID
     * 
     * @param id The ID of the inventory item
     * @return An Optional containing the inventory item if found, or empty if not found
     */
    Optional<InventoryItem> findById(int id);
    
    /**
     * Find an inventory item by name
     * 
     * @param name The name of the inventory item
     * @return An Optional containing the inventory item if found, or empty if not found
     */
    Optional<InventoryItem> findByName(String name);
    
    /**
     * Find all inventory items
     * 
     * @return A list of all inventory items
     */
    List<InventoryItem> findAll();
    
    /**
     * Find all inventory items that need to be reordered
     * 
     * @return A list of inventory items that need to be reordered
     */
    List<InventoryItem> findItemsToReorder();
    
    /**
     * Update an inventory item
     * 
     * @param item The inventory item to update
     * @return The updated inventory item
     */
    InventoryItem update(InventoryItem item);
    
    /**
     * Delete an inventory item by ID
     * 
     * @param id The ID of the inventory item to delete
     * @return true if the inventory item was deleted, false otherwise
     */
    boolean deleteById(int id);
}
