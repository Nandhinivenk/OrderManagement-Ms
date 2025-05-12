package com.cloudkitchen.repository;

import com.cloudkitchen.model.Order;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Order
 */
public interface OrderRepository {
    
    /**
     * Save an order
     * 
     * @param order The order to save
     * @return The saved order with ID
     */
    Order save(Order order);
    
    /**
     * Find an order by ID
     * 
     * @param id The ID of the order
     * @return An Optional containing the order if found, or empty if not found
     */
    Optional<Order> findById(int id);
    
    /**
     * Find all orders
     * 
     * @return A list of all orders
     */
    List<Order> findAll();
    
    /**
     * Find all orders by customer ID
     * 
     * @param customerId The ID of the customer
     * @return A list of orders for the specified customer
     */
    List<Order> findByCustomerId(int customerId);
    
    /**
     * Find all orders by status
     * 
     * @param status The status of the orders
     * @return A list of orders with the specified status
     */
    List<Order> findByStatus(String status);
    
    /**
     * Update an order
     * 
     * @param order The order to update
     * @return The updated order
     */
    Order update(Order order);
    
    /**
     * Delete an order by ID
     * 
     * @param id The ID of the order to delete
     * @return true if the order was deleted, false otherwise
     */
    boolean deleteById(int id);
}
