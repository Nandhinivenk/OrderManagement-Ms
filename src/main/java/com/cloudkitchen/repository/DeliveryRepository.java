package com.cloudkitchen.repository;

import com.cloudkitchen.model.Delivery;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Delivery
 */
public interface DeliveryRepository {
    
    /**
     * Save a delivery
     * 
     * @param delivery The delivery to save
     * @return The saved delivery with ID
     */
    Delivery save(Delivery delivery);
    
    /**
     * Find a delivery by ID
     * 
     * @param id The ID of the delivery
     * @return An Optional containing the delivery if found, or empty if not found
     */
    Optional<Delivery> findById(int id);
    
    /**
     * Find a delivery by order ID
     * 
     * @param orderId The ID of the order
     * @return An Optional containing the delivery if found, or empty if not found
     */
    Optional<Delivery> findByOrderId(int orderId);
    
    /**
     * Find all deliveries
     * 
     * @return A list of all deliveries
     */
    List<Delivery> findAll();
    
    /**
     * Find all deliveries by status
     * 
     * @param status The status of the deliveries
     * @return A list of deliveries with the specified status
     */
    List<Delivery> findByStatus(String status);
    
    /**
     * Find all deliveries by delivery person
     * 
     * @param deliveryPerson The name of the delivery person
     * @return A list of deliveries assigned to the specified delivery person
     */
    List<Delivery> findByDeliveryPerson(String deliveryPerson);
    
    /**
     * Update a delivery
     * 
     * @param delivery The delivery to update
     * @return The updated delivery
     */
    Delivery update(Delivery delivery);
    
    /**
     * Delete a delivery by ID
     * 
     * @param id The ID of the delivery to delete
     * @return true if the delivery was deleted, false otherwise
     */
    boolean deleteById(int id);
}
