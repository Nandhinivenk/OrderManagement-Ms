package com.cloudkitchen.adminservice.controller;

import com.cloudkitchen.adminservice.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/dashboard")
@RequiredArgsConstructor
public class DashboardController {
    
    private final DashboardService dashboardService;
    
    @GetMapping("/summary")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getDashboardSummary() {
        Map<String, Object> summary = dashboardService.getDashboardSummary();
        return ResponseEntity.ok(summary);
    }
    
    @GetMapping("/customers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Map<String, Object>>> getAllCustomers() {
        List<Map<String, Object>> customers = dashboardService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }
    
    @GetMapping("/customers/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getCustomerById(@PathVariable Long id) {
        Map<String, Object> customer = dashboardService.getCustomerById(id);
        return ResponseEntity.ok(customer);
    }
    
    @GetMapping("/orders")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Map<String, Object>>> getAllOrders() {
        List<Map<String, Object>> orders = dashboardService.getAllOrders();
        return ResponseEntity.ok(orders);
    }
    
    @GetMapping("/orders/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getOrderById(@PathVariable Long id) {
        Map<String, Object> order = dashboardService.getOrderById(id);
        return ResponseEntity.ok(order);
    }
    
    @PatchMapping("/orders/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        Map<String, Object> updatedOrder = dashboardService.updateOrderStatus(id, status);
        return ResponseEntity.ok(updatedOrder);
    }
    
    @GetMapping("/kitchen-flow")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Map<String, Object>>> getAllOrderFlows() {
        List<Map<String, Object>> orderFlows = dashboardService.getAllOrderFlows();
        return ResponseEntity.ok(orderFlows);
    }
    
    @GetMapping("/kitchen-flow/{orderId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getOrderFlowById(@PathVariable Long orderId) {
        Map<String, Object> orderFlow = dashboardService.getOrderFlowById(orderId);
        return ResponseEntity.ok(orderFlow);
    }
    
    @GetMapping("/kitchen-flow/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Map<String, Object>>> getOrderFlowsByStatus(@PathVariable String status) {
        List<Map<String, Object>> orderFlows = dashboardService.getOrderFlowsByStatus(status);
        return ResponseEntity.ok(orderFlows);
    }
    
    @PatchMapping("/kitchen-flow/{orderId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> updateOrderFlowStatus(
            @PathVariable Long orderId,
            @RequestParam String status,
            @RequestParam(required = false) String notes) {
        Map<String, Object> updatedOrderFlow = dashboardService.updateOrderFlowStatus(orderId, status, notes);
        return ResponseEntity.ok(updatedOrderFlow);
    }
    
    @GetMapping("/deliveries")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Map<String, Object>>> getAllDeliveries() {
        List<Map<String, Object>> deliveries = dashboardService.getAllDeliveries();
        return ResponseEntity.ok(deliveries);
    }
    
    @GetMapping("/deliveries/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getDeliveryById(@PathVariable Long id) {
        Map<String, Object> delivery = dashboardService.getDeliveryById(id);
        return ResponseEntity.ok(delivery);
    }
    
    @GetMapping("/deliveries/order/{orderId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getDeliveryByOrderId(@PathVariable Long orderId) {
        Map<String, Object> delivery = dashboardService.getDeliveryByOrderId(orderId);
        return ResponseEntity.ok(delivery);
    }
    
    @PatchMapping("/deliveries/{id}/assign")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> assignDeliveryPerson(
            @PathVariable Long id,
            @RequestParam String deliveryPerson) {
        Map<String, Object> updatedDelivery = dashboardService.assignDeliveryPerson(id, deliveryPerson);
        return ResponseEntity.ok(updatedDelivery);
    }
    
    @PatchMapping("/deliveries/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> updateDeliveryStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        Map<String, Object> updatedDelivery = dashboardService.updateDeliveryStatus(id, status);
        return ResponseEntity.ok(updatedDelivery);
    }
    
    @GetMapping("/inventory")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Map<String, Object>>> getAllInventoryItems() {
        List<Map<String, Object>> inventoryItems = dashboardService.getAllInventoryItems();
        return ResponseEntity.ok(inventoryItems);
    }
    
    @GetMapping("/inventory/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getInventoryItemById(@PathVariable Long id) {
        Map<String, Object> inventoryItem = dashboardService.getInventoryItemById(id);
        return ResponseEntity.ok(inventoryItem);
    }
    
    @PatchMapping("/inventory/{id}/quantity")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> updateInventoryQuantity(
            @PathVariable Long id,
            @RequestParam int quantity) {
        Map<String, Object> updatedInventoryItem = dashboardService.updateInventoryQuantity(id, quantity);
        return ResponseEntity.ok(updatedInventoryItem);
    }
}
