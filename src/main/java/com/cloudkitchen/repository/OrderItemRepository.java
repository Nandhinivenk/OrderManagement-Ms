package com.cloudkitchen.repository;

import com.cloudkitchen.model.OrderItem;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for OrderItem
 */
public interface OrderItemRepository {
    
    /**
     * Save an order item
     * 
     * @param orderItem The order item to save
     * @return The saved order item with ID
     */
    OrderItem save(OrderItem orderItem);
    
    /**
     * Find an order item by ID
     * 
     * @param id The ID of the order item
     * @return An Optional containing the order item if found, or empty if not found
     */
    Optional<OrderItem> findById(int id);
    
    /**
     * Find all order items for an order
     * 
     * @param orderId The ID of the order
     * @return A list of order items for the specified order
     */
    List<OrderItem> findByOrderId(int orderId);
    
    /**
     * Update an order item
     * 
     * @param orderItem The order item to update
     * @return The updated order item
     */
    OrderItem update(OrderItem orderItem);
    
    /**
     * Delete an order item by ID
     * 
     * @param id The ID of the order item to delete
     * @return true if the order item was deleted, false otherwise
     */
    boolean deleteById(int id);
    
    /**
     * Delete all order items for an order
     * 
     * @param orderId The ID of the order
     * @return true if the order items were deleted, false otherwise
     */
    boolean deleteByOrderId(int orderId);
}
