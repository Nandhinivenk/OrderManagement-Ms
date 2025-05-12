package com.cloudkitchen.service;

import com.cloudkitchen.model.Delivery;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for Delivery
 */
public interface DeliveryService {
    
    /**
     * Create a new delivery for an order
     * 
     * @param orderId The ID of the order
     * @param deliveryAddress The delivery address
     * @return The created delivery with ID
     */
    Delivery createDelivery(int orderId, String deliveryAddress);
    
    /**
     * Get a delivery by ID
     * 
     * @param id The ID of the delivery
     * @return An Optional containing the delivery if found, or empty if not found
     */
    Optional<Delivery> getDeliveryById(int id);
    
    /**
     * Get a delivery by order ID
     * 
     * @param orderId The ID of the order
     * @return An Optional containing the delivery if found, or empty if not found
     */
    Optional<Delivery> getDeliveryByOrderId(int orderId);
    
    /**
     * Get all deliveries
     * 
     * @return A list of all deliveries
     */
    List<Delivery> getAllDeliveries();
    
    /**
     * Get all deliveries with a specific status
     * 
     * @param status The status of the deliveries
     * @return A list of deliveries with the specified status
     */
    List<Delivery> getDeliveriesByStatus(String status);
    
    /**
     * Get all deliveries assigned to a specific delivery person
     * 
     * @param deliveryPerson The name of the delivery person
     * @return A list of deliveries assigned to the specified delivery person
     */
    List<Delivery> getDeliveriesByDeliveryPerson(String deliveryPerson);
    
    /**
     * Assign a delivery person to a delivery
     * 
     * @param deliveryId The ID of the delivery
     * @param deliveryPerson The name of the delivery person
     * @return The updated delivery
     */
    Delivery assignDeliveryPerson(int deliveryId, String deliveryPerson);
    
    /**
     * Update the status of a delivery
     * 
     * @param deliveryId The ID of the delivery
     * @param status The new status
     * @return The updated delivery
     */
    Delivery updateDeliveryStatus(int deliveryId, String status);
    
    /**
     * Mark a delivery as delivered
     * 
     * @param deliveryId The ID of the delivery
     * @param deliveryTime The time of delivery
     * @return The updated delivery
     */
    Delivery markAsDelivered(int deliveryId, LocalDateTime deliveryTime);
    
    /**
     * Delete a delivery
     * 
     * @param deliveryId The ID of the delivery to delete
     * @return true if the delivery was deleted, false otherwise
     */
    boolean deleteDelivery(int deliveryId);
}
