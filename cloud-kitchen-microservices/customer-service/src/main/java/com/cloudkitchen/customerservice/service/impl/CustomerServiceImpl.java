package com.cloudkitchen.customerservice.service.impl;

import com.cloudkitchen.customerservice.dto.CustomerDTO;
import com.cloudkitchen.customerservice.dto.CustomerRegistrationRequest;
import com.cloudkitchen.customerservice.dto.LoginRequest;
import com.cloudkitchen.customerservice.exception.AuthenticationException;
import com.cloudkitchen.customerservice.exception.CustomerNotFoundException;
import com.cloudkitchen.customerservice.model.Customer;
import com.cloudkitchen.customerservice.repository.CustomerRepository;
import com.cloudkitchen.customerservice.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    
    private final CustomerRepository customerRepository;
    
    @Override
    public CustomerDTO registerCustomer(CustomerRegistrationRequest request) {
        // Check if username or email already exists
        if (customerRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already taken");
        }
        
        if (customerRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }
        
        // Create new customer
        Customer customer = new Customer();
        customer.setUsername(request.getUsername());
        customer.setPassword(request.getPassword()); // In a real app, you would hash the password
        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());
        customer.setAddress(request.getAddress());
        
        // Save customer
        Customer savedCustomer = customerRepository.save(customer);
        
        // Return DTO
        return mapToDTO(savedCustomer);
    }
    
    @Override
    public CustomerDTO getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + id));
        
        return mapToDTO(customer);
    }
    
    @Override
    public CustomerDTO getCustomerByUsername(String username) {
        Customer customer = customerRepository.findByUsername(username)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with username: " + username));
        
        return mapToDTO(customer);
    }
    
    @Override
    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO) {
        // Check if customer exists
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + id));
        
        // Update customer
        customer.setName(customerDTO.getName());
        customer.setEmail(customerDTO.getEmail());
        customer.setPhone(customerDTO.getPhone());
        customer.setAddress(customerDTO.getAddress());
        
        // Save customer
        Customer updatedCustomer = customerRepository.save(customer);
        
        // Return DTO
        return mapToDTO(updatedCustomer);
    }
    
    @Override
    public void deleteCustomer(Long id) {
        // Check if customer exists
        if (!customerRepository.existsById(id)) {
            throw new CustomerNotFoundException("Customer not found with id: " + id);
        }
        
        // Delete customer
        customerRepository.deleteById(id);
    }
    
    @Override
    public CustomerDTO authenticateCustomer(LoginRequest loginRequest) {
        // Find customer by username
        Customer customer = customerRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new AuthenticationException("Invalid username or password"));
        
        // Verify password (in a real app, you would hash and compare)
        if (!customer.getPassword().equals(loginRequest.getPassword())) {
            throw new AuthenticationException("Invalid username or password");
        }
        
        // Return DTO
        return mapToDTO(customer);
    }
    
    private CustomerDTO mapToDTO(Customer customer) {
        CustomerDTO dto = new CustomerDTO();
        dto.setId(customer.getId());
        dto.setUsername(customer.getUsername());
        dto.setName(customer.getName());
        dto.setEmail(customer.getEmail());
        dto.setPhone(customer.getPhone());
        dto.setAddress(customer.getAddress());
        dto.setCreatedAt(customer.getCreatedAt());
        return dto;
    }
}
