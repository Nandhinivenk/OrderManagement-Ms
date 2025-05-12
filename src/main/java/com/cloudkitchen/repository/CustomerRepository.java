package com.cloudkitchen.repository;

import com.cloudkitchen.model.Customer;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Customer
 */
public interface CustomerRepository {
    
    /**
     * Save a customer
     * 
     * @param customer The customer to save
     * @return The saved customer with ID
     */
    Customer save(Customer customer);
    
    /**
     * Find a customer by ID
     * 
     * @param id The ID of the customer
     * @return An Optional containing the customer if found, or empty if not found
     */
    Optional<Customer> findById(int id);
    
    /**
     * Find a customer by username
     * 
     * @param username The username of the customer
     * @return An Optional containing the customer if found, or empty if not found
     */
    Optional<Customer> findByUsername(String username);
    
    /**
     * Find a customer by email
     * 
     * @param email The email of the customer
     * @return An Optional containing the customer if found, or empty if not found
     */
    Optional<Customer> findByEmail(String email);
    
    /**
     * Find all customers
     * 
     * @return A list of all customers
     */
    List<Customer> findAll();
    
    /**
     * Update a customer
     * 
     * @param customer The customer to update
     * @return The updated customer
     */
    Customer update(Customer customer);
    
    /**
     * Delete a customer by ID
     * 
     * @param id The ID of the customer to delete
     * @return true if the customer was deleted, false otherwise
     */
    boolean deleteById(int id);
}
