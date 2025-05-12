package com.cloudkitchen.adminservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

@FeignClient(name = "customer-service")
public interface CustomerClient {
    
    @GetMapping("/api/customers")
    ResponseEntity<List<Map<String, Object>>> getAllCustomers();
    
    @GetMapping("/api/customers/{id}")
    ResponseEntity<Map<String, Object>> getCustomerById(@PathVariable("id") Long id);
}
