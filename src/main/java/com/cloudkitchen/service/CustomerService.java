package com.cloudkitchen.service;

import com.cloudkitchen.model.Customer;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for Customer
 */
public interface CustomerService {
    
    /**
     * Register a new customer
     * 
     * @param customer The customer to register
     * @return The registered customer with ID
     */
    Customer register(Customer customer);
    
    /**
     * Authenticate a customer
     * 
     * @param username The username of the customer
     * @param password The password of the customer
     * @return An Optional containing the authenticated customer if successful, or empty if not
     */
    Optional<Customer> authenticate(String username, String password);
    
    /**
     * Get a customer by ID
     * 
     * @param id The ID of the customer
     * @return An Optional containing the customer if found, or empty if not found
     */
    Optional<Customer> getCustomerById(int id);
    
    /**
     * Get a customer by username
     * 
     * @param username The username of the customer
     * @return An Optional containing the customer if found, or empty if not found
     */
    Optional<Customer> getCustomerByUsername(String username);
    
    /**
     * Get a customer by email
     * 
     * @param email The email of the customer
     * @return An Optional containing the customer if found, or empty if not found
     */
    Optional<Customer> getCustomerByEmail(String email);
    
    /**
     * Get all customers
     * 
     * @return A list of all customers
     */
    List<Customer> getAllCustomers();
    
    /**
     * Update a customer
     * 
     * @param customer The customer to update
     * @return The updated customer
     */
    Customer updateCustomer(Customer customer);
    
    /**
     * Delete a customer by ID
     * 
     * @param id The ID of the customer to delete
     * @return true if the customer was deleted, false otherwise
     */
    boolean deleteCustomer(int id);
    
    /**
     * Check if a username is available
     * 
     * @param username The username to check
     * @return true if the username is available, false otherwise
     */
    boolean isUsernameAvailable(String username);
    
    /**
     * Check if an email is available
     * 
     * @param email The email to check
     * @return true if the email is available, false otherwise
     */
    boolean isEmailAvailable(String email);
}
