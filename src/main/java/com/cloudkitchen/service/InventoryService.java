package com.cloudkitchen.service;

import com.cloudkitchen.model.InventoryItem;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for InventoryItem
 */
public interface InventoryService {
    
    /**
     * Add a new inventory item
     * 
     * @param item The inventory item to add
     * @return The added inventory item with ID and QR code
     */
    InventoryItem addInventoryItem(InventoryItem item);
    
    /**
     * Get an inventory item by ID
     * 
     * @param id The ID of the inventory item
     * @return An Optional containing the inventory item if found, or empty if not found
     */
    Optional<InventoryItem> getInventoryItemById(int id);
    
    /**
     * Get an inventory item by name
     * 
     * @param name The name of the inventory item
     * @return An Optional containing the inventory item if found, or empty if not found
     */
    Optional<InventoryItem> getInventoryItemByName(String name);
    
    /**
     * Get all inventory items
     * 
     * @return A list of all inventory items
     */
    List<InventoryItem> getAllInventoryItems();
    
    /**
     * Get all inventory items that need to be reordered
     * 
     * @return A list of inventory items that need to be reordered
     */
    List<InventoryItem> getItemsToReorder();
    
    /**
     * Update an inventory item
     * 
     * @param item The inventory item to update
     * @return The updated inventory item
     */
    InventoryItem updateInventoryItem(InventoryItem item);
    
    /**
     * Update the quantity of an inventory item
     * 
     * @param id The ID of the inventory item
     * @param quantity The new quantity
     * @return The updated inventory item
     */
    InventoryItem updateInventoryItemQuantity(int id, int quantity);
    
    /**
     * Delete an inventory item by ID
     * 
     * @param id The ID of the inventory item to delete
     * @return true if the inventory item was deleted, false otherwise
     */
    boolean deleteInventoryItem(int id);
    
    /**
     * Generate a QR code for an inventory item
     * 
     * @param item The inventory item to generate a QR code for
     * @return The path to the generated QR code
     */
    String generateQRCode(InventoryItem item);
}
