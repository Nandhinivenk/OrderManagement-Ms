package com.cloudkitchen.orderservice.service.impl;

import com.cloudkitchen.orderservice.client.CustomerClient;
import com.cloudkitchen.orderservice.client.FoodItemClient;
import com.cloudkitchen.orderservice.dto.*;
import com.cloudkitchen.orderservice.exception.CustomerNotFoundException;
import com.cloudkitchen.orderservice.exception.FoodItemNotFoundException;
import com.cloudkitchen.orderservice.exception.OrderItemNotFoundException;
import com.cloudkitchen.orderservice.exception.OrderNotFoundException;
import com.cloudkitchen.orderservice.model.Order;
import com.cloudkitchen.orderservice.model.OrderItem;
import com.cloudkitchen.orderservice.repository.OrderItemRepository;
import com.cloudkitchen.orderservice.repository.OrderRepository;
import com.cloudkitchen.orderservice.service.OrderService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CustomerClient customerClient;
    private final FoodItemClient foodItemClient;
    
    @Override
    @Transactional
    public OrderDTO createOrder(CreateOrderRequest createOrderRequest) {
        // Validate customer
        try {
            customerClient.getCustomerById(createOrderRequest.getCustomerId());
        } catch (FeignException e) {
            throw new CustomerNotFoundException("Customer not found with id: " + createOrderRequest.getCustomerId());
        }
        
        // Create order
        Order order = new Order();
        order.setCustomerId(createOrderRequest.getCustomerId());
        order.setPaymentMethod(createOrderRequest.getPaymentMethod());
        order.setStatus(Order.STATUS_PENDING);
        order.setPaymentStatus(Order.PAYMENT_PENDING);
        
        // Save order to get ID
        Order savedOrder = orderRepository.save(order);
        
        // Add order items
        for (OrderItemDTO itemDTO : createOrderRequest.getOrderItems()) {
            // Validate food item
            FoodItemDTO foodItem;
            try {
                foodItem = foodItemClient.getFoodItemById(itemDTO.getFoodItemId()).getBody();
                if (foodItem == null) {
                    throw new FoodItemNotFoundException("Food item not found with id: " + itemDTO.getFoodItemId());
                }
            } catch (FeignException e) {
                throw new FoodItemNotFoundException("Food item not found with id: " + itemDTO.getFoodItemId());
            }
            
            // Create order item
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(savedOrder);
            orderItem.setFoodItemId(foodItem.getId());
            orderItem.setFoodItemName(foodItem.getName());
            orderItem.setQuantity(itemDTO.getQuantity());
            orderItem.setPrice(foodItem.getPrice());
            
            // Add to order
            savedOrder.addOrderItem(orderItem);
        }
        
        // Save updated order
        Order finalOrder = orderRepository.save(savedOrder);
        
        // Return DTO
        return mapToDTO(finalOrder);
    }
    
    @Override
    public OrderDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));
        
        return mapToDTO(order);
    }
    
    @Override
    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<OrderDTO> getOrdersByCustomerId(Long customerId) {
        return orderRepository.findByCustomerId(customerId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<OrderDTO> getOrdersByStatus(String status) {
        return orderRepository.findByStatus(status).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public OrderDTO updateOrderStatus(Long id, String status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));
        
        order.setStatus(status);
        Order updatedOrder = orderRepository.save(order);
        
        return mapToDTO(updatedOrder);
    }
    
    @Override
    @Transactional
    public OrderDTO updatePaymentStatus(Long id, String paymentStatus) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));
        
        order.setPaymentStatus(paymentStatus);
        Order updatedOrder = orderRepository.save(order);
        
        return mapToDTO(updatedOrder);
    }
    
    @Override
    @Transactional
    public OrderDTO addItemToOrder(Long orderId, OrderItemDTO orderItemDTO) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + orderId));
        
        // Validate food item
        FoodItemDTO foodItem;
        try {
            foodItem = foodItemClient.getFoodItemById(orderItemDTO.getFoodItemId()).getBody();
            if (foodItem == null) {
                throw new FoodItemNotFoundException("Food item not found with id: " + orderItemDTO.getFoodItemId());
            }
        } catch (FeignException e) {
            throw new FoodItemNotFoundException("Food item not found with id: " + orderItemDTO.getFoodItemId());
        }
        
        // Create order item
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setFoodItemId(foodItem.getId());
        orderItem.setFoodItemName(foodItem.getName());
        orderItem.setQuantity(orderItemDTO.getQuantity());
        orderItem.setPrice(foodItem.getPrice());
        
        // Add to order
        order.addOrderItem(orderItem);
        
        // Save updated order
        Order updatedOrder = orderRepository.save(order);
        
        return mapToDTO(updatedOrder);
    }
    
    @Override
    @Transactional
    public OrderDTO removeItemFromOrder(Long orderId, Long orderItemId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + orderId));
        
        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new OrderItemNotFoundException("Order item not found with id: " + orderItemId));
        
        // Verify the item belongs to the order
        if (!orderItem.getOrder().getId().equals(orderId)) {
            throw new IllegalArgumentException("Order item does not belong to the specified order");
        }
        
        // Remove from order
        order.removeOrderItem(orderItem);
        
        // Save updated order
        Order updatedOrder = orderRepository.save(order);
        
        return mapToDTO(updatedOrder);
    }
    
    @Override
    @Transactional
    public OrderDTO cancelOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));
        
        order.setStatus(Order.STATUS_CANCELLED);
        Order updatedOrder = orderRepository.save(order);
        
        return mapToDTO(updatedOrder);
    }
    
    @Override
    @Transactional
    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new OrderNotFoundException("Order not found with id: " + id);
        }
        
        orderRepository.deleteById(id);
    }
    
    private OrderDTO mapToDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setCustomerId(order.getCustomerId());
        dto.setOrderDate(order.getOrderDate());
        dto.setStatus(order.getStatus());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setPaymentMethod(order.getPaymentMethod());
        dto.setPaymentStatus(order.getPaymentStatus());
        
        // Try to get customer name
        try {
            CustomerDTO customer = customerClient.getCustomerById(order.getCustomerId()).getBody();
            if (customer != null) {
                dto.setCustomerName(customer.getName());
            }
        } catch (Exception e) {
            // Ignore if customer service is not available
        }
        
        // Map order items
        List<OrderItemDTO> orderItemDTOs = order.getOrderItems().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
        dto.setOrderItems(orderItemDTOs);
        
        return dto;
    }
    
    private OrderItemDTO mapToDTO(OrderItem orderItem) {
        OrderItemDTO dto = new OrderItemDTO();
        dto.setId(orderItem.getId());
        dto.setFoodItemId(orderItem.getFoodItemId());
        dto.setFoodItemName(orderItem.getFoodItemName());
        dto.setQuantity(orderItem.getQuantity());
        dto.setPrice(orderItem.getPrice());
        dto.setSubtotal(orderItem.getSubtotal());
        return dto;
    }
}
