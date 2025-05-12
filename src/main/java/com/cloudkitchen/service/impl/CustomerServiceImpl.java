package com.cloudkitchen.service.impl;

import com.cloudkitchen.model.Customer;
import com.cloudkitchen.repository.CustomerRepository;
import com.cloudkitchen.repository.impl.CustomerRepositoryImpl;
import com.cloudkitchen.service.CustomerService;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of CustomerService
 */
public class CustomerServiceImpl implements CustomerService {
    
    private final CustomerRepository customerRepository;
    
    public CustomerServiceImpl() {
        this.customerRepository = new CustomerRepositoryImpl();
    }
    
    @Override
    public Customer register(Customer customer) {
        // Check if username is available
        if (!isUsernameAvailable(customer.getUsername())) {
            throw new IllegalArgumentException("Username is already taken");
        }
        
        // Check if email is available
        if (!isEmailAvailable(customer.getEmail())) {
            throw new IllegalArgumentException("Email is already registered");
        }
        
        // TODO: Hash the password before saving
        // For now, we'll just save the plain password
        
        return customerRepository.save(customer);
    }
    
    @Override
    public Optional<Customer> authenticate(String username, String password) {
        Optional<Customer> customerOpt = customerRepository.findByUsername(username);
        
        if (customerOpt.isPresent()) {
            Customer customer = customerOpt.get();
            
            // TODO: Implement proper password hashing and verification
            // For now, we'll just compare the plain passwords
            if (customer.getPassword().equals(password)) {
                return Optional.of(customer);
            }
        }
        
        return Optional.empty();
    }
    
    @Override
    public Optional<Customer> getCustomerById(int id) {
        return customerRepository.findById(id);
    }
    
    @Override
    public Optional<Customer> getCustomerByUsername(String username) {
        return customerRepository.findByUsername(username);
    }
    
    @Override
    public Optional<Customer> getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email);
    }
    
    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }
    
    @Override
    public Customer updateCustomer(Customer customer) {
        // Check if the customer exists
        Optional<Customer> existingCustomer = customerRepository.findById(customer.getId());
        
        if (existingCustomer.isEmpty()) {
            throw new IllegalArgumentException("Customer not found");
        }
        
        // Check if username is available (if changed)
        if (!existingCustomer.get().getUsername().equals(customer.getUsername()) && 
            !isUsernameAvailable(customer.getUsername())) {
            throw new IllegalArgumentException("Username is already taken");
        }
        
        // Check if email is available (if changed)
        if (!existingCustomer.get().getEmail().equals(customer.getEmail()) && 
            !isEmailAvailable(customer.getEmail())) {
            throw new IllegalArgumentException("Email is already registered");
        }
        
        return customerRepository.update(customer);
    }
    
    @Override
    public boolean deleteCustomer(int id) {
        return customerRepository.deleteById(id);
    }
    
    @Override
    public boolean isUsernameAvailable(String username) {
        return customerRepository.findByUsername(username).isEmpty();
    }
    
    @Override
    public boolean isEmailAvailable(String email) {
        return customerRepository.findByEmail(email).isEmpty();
    }
}
