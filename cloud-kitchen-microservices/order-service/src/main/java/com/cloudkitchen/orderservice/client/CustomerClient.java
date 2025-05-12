package com.cloudkitchen.orderservice.client;

import com.cloudkitchen.orderservice.dto.CustomerDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "customer-service")
public interface CustomerClient {
    
    @GetMapping("/api/customers/{id}")
    ResponseEntity<CustomerDTO> getCustomerById(@PathVariable("id") Long id);
}
