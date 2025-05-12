package com.cloudkitchen.service;

import com.cloudkitchen.model.Order;
import com.cloudkitchen.model.OrderItem;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for Order
 */
public interface OrderService {
    
    /**
     * Create a new order
     * 
     * @param order The order to create
     * @return The created order with ID
     */
    Order createOrder(Order order);
    
    /**
     * Add an item to an order
     * 
     * @param orderId The ID of the order
     * @param orderItem The order item to add
     * @return The updated order
     */
    Order addItemToOrder(int orderId, OrderItem orderItem);
    
    /**
     * Remove an item from an order
     * 
     * @param orderId The ID of the order
     * @param orderItemId The ID of the order item to remove
     * @return The updated order
     */
    Order removeItemFromOrder(int orderId, int orderItemId);
    
    /**
     * Get an order by ID
     * 
     * @param id The ID of the order
     * @return An Optional containing the order if found, or empty if not found
     */
    Optional<Order> getOrderById(int id);
    
    /**
     * Get all orders
     * 
     * @return A list of all orders
     */
    List<Order> getAllOrders();
    
    /**
     * Get all orders for a customer
     * 
     * @param customerId The ID of the customer
     * @return A list of orders for the specified customer
     */
    List<Order> getOrdersByCustomerId(int customerId);
    
    /**
     * Get all orders with a specific status
     * 
     * @param status The status of the orders
     * @return A list of orders with the specified status
     */
    List<Order> getOrdersByStatus(String status);
    
    /**
     * Update the status of an order
     * 
     * @param orderId The ID of the order
     * @param status The new status
     * @return The updated order
     */
    Order updateOrderStatus(int orderId, String status);
    
    /**
     * Update the payment status of an order
     * 
     * @param orderId The ID of the order
     * @param paymentStatus The new payment status
     * @return The updated order
     */
    Order updatePaymentStatus(int orderId, String paymentStatus);
    
    /**
     * Cancel an order
     * 
     * @param orderId The ID of the order to cancel
     * @return The cancelled order
     */
    Order cancelOrder(int orderId);
    
    /**
     * Delete an order
     * 
     * @param orderId The ID of the order to delete
     * @return true if the order was deleted, false otherwise
     */
    boolean deleteOrder(int orderId);
}
