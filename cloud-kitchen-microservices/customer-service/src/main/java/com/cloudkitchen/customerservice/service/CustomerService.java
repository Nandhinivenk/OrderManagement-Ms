package com.cloudkitchen.customerservice.service;

import com.cloudkitchen.customerservice.dto.CustomerDTO;
import com.cloudkitchen.customerservice.dto.CustomerRegistrationRequest;
import com.cloudkitchen.customerservice.dto.LoginRequest;

import java.util.List;

public interface CustomerService {
    
    CustomerDTO registerCustomer(CustomerRegistrationRequest request);
    
    CustomerDTO getCustomerById(Long id);
    
    CustomerDTO getCustomerByUsername(String username);
    
    List<CustomerDTO> getAllCustomers();
    
    CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO);
    
    void deleteCustomer(Long id);
    
    CustomerDTO authenticateCustomer(LoginRequest loginRequest);
}
