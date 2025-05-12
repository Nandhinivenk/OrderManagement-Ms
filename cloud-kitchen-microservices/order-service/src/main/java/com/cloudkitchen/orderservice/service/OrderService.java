package com.cloudkitchen.orderservice.service;

import com.cloudkitchen.orderservice.dto.CreateOrderRequest;
import com.cloudkitchen.orderservice.dto.OrderDTO;
import com.cloudkitchen.orderservice.dto.OrderItemDTO;

import java.util.List;

public interface OrderService {
    
    OrderDTO createOrder(CreateOrderRequest createOrderRequest);
    
    OrderDTO getOrderById(Long id);
    
    List<OrderDTO> getAllOrders();
    
    List<OrderDTO> getOrdersByCustomerId(Long customerId);
    
    List<OrderDTO> getOrdersByStatus(String status);
    
    OrderDTO updateOrderStatus(Long id, String status);
    
    OrderDTO updatePaymentStatus(Long id, String paymentStatus);
    
    OrderDTO addItemToOrder(Long orderId, OrderItemDTO orderItemDTO);
    
    OrderDTO removeItemFromOrder(Long orderId, Long orderItemId);
    
    OrderDTO cancelOrder(Long id);
    
    void deleteOrder(Long id);
}
