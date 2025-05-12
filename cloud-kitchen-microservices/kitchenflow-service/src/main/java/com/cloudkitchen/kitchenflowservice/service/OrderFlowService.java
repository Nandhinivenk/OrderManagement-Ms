package com.cloudkitchen.kitchenflowservice.service;

import com.cloudkitchen.kitchenflowservice.dto.OrderFlowDTO;
import com.cloudkitchen.kitchenflowservice.dto.StatusUpdateDTO;

import java.util.List;

public interface OrderFlowService {
    
    OrderFlowDTO initializeOrderFlow(Long orderId);
    
    OrderFlowDTO getOrderFlowById(Long orderId);
    
    List<OrderFlowDTO> getAllOrderFlows();
    
    List<OrderFlowDTO> getOrderFlowsByStatus(String status);
    
    List<OrderFlowDTO> getOrderFlowsByCustomerId(Long customerId);
    
    List<OrderFlowDTO> getOrderFlowsByDeliveryPerson(String deliveryPerson);
    
    OrderFlowDTO updateOrderStatus(Long orderId, String status, String notes);
    
    OrderFlowDTO assignDeliveryPerson(Long orderId, String deliveryPerson);
    
    List<StatusUpdateDTO> getOrderStatusHistory(Long orderId);
    
    void deleteOrderFlow(Long orderId);
}
