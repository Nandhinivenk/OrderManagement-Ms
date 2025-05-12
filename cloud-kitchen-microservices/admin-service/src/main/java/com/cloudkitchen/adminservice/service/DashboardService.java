package com.cloudkitchen.adminservice.service;

import java.util.List;
import java.util.Map;

public interface DashboardService {
    
    List<Map<String, Object>> getAllCustomers();
    
    Map<String, Object> getCustomerById(Long id);
    
    List<Map<String, Object>> getAllOrders();
    
    Map<String, Object> getOrderById(Long id);
    
    Map<String, Object> updateOrderStatus(Long id, String status);
    
    List<Map<String, Object>> getAllOrderFlows();
    
    Map<String, Object> getOrderFlowById(Long orderId);
    
    List<Map<String, Object>> getOrderFlowsByStatus(String status);
    
    Map<String, Object> updateOrderFlowStatus(Long orderId, String status, String notes);
    
    List<Map<String, Object>> getAllDeliveries();
    
    Map<String, Object> getDeliveryById(Long id);
    
    Map<String, Object> getDeliveryByOrderId(Long orderId);
    
    Map<String, Object> assignDeliveryPerson(Long id, String deliveryPerson);
    
    Map<String, Object> updateDeliveryStatus(Long id, String status);
    
    List<Map<String, Object>> getAllInventoryItems();
    
    Map<String, Object> getInventoryItemById(Long id);
    
    Map<String, Object> updateInventoryQuantity(Long id, int quantity);
    
    Map<String, Object> getDashboardSummary();
}
