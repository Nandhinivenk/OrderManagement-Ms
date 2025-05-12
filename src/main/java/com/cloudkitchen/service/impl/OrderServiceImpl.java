package com.cloudkitchen.service.impl;

import com.cloudkitchen.model.FoodItem;
import com.cloudkitchen.model.Order;
import com.cloudkitchen.model.OrderItem;
import com.cloudkitchen.repository.FoodItemRepository;
import com.cloudkitchen.repository.OrderItemRepository;
import com.cloudkitchen.repository.OrderRepository;
import com.cloudkitchen.repository.impl.FoodItemRepositoryImpl;
import com.cloudkitchen.repository.impl.OrderItemRepositoryImpl;
import com.cloudkitchen.repository.impl.OrderRepositoryImpl;
import com.cloudkitchen.service.OrderService;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of OrderService
 */
public class OrderServiceImpl implements OrderService {
    
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final FoodItemRepository foodItemRepository;
    
    public OrderServiceImpl() {
        this.orderRepository = new OrderRepositoryImpl();
        this.orderItemRepository = new OrderItemRepositoryImpl();
        this.foodItemRepository = new FoodItemRepositoryImpl();
    }
    
    @Override
    public Order createOrder(Order order) {
        // Save the order first to get an ID
        Order savedOrder = orderRepository.save(order);
        
        if (savedOrder != null && order.getOrderItems() != null && !order.getOrderItems().isEmpty()) {
            // Save the order items
            for (OrderItem item : order.getOrderItems()) {
                item.setOrderId(savedOrder.getId());
                orderItemRepository.save(item);
            }
            
            // Load the order items
            List<OrderItem> orderItems = orderItemRepository.findByOrderId(savedOrder.getId());
            savedOrder.setOrderItems(orderItems);
        }
        
        return savedOrder;
    }
    
    @Override
    public Order addItemToOrder(int orderId, OrderItem orderItem) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        
        if (orderOpt.isEmpty()) {
            throw new IllegalArgumentException("Order not found");
        }
        
        Order order = orderOpt.get();
        
        // Check if the food item exists
        Optional<FoodItem> foodItemOpt = foodItemRepository.findById(orderItem.getFoodItemId());
        
        if (foodItemOpt.isEmpty()) {
            throw new IllegalArgumentException("Food item not found");
        }
        
        FoodItem foodItem = foodItemOpt.get();
        
        // Set the order ID and price
        orderItem.setOrderId(orderId);
        if (orderItem.getPrice() == null) {
            orderItem.setPrice(foodItem.getPrice());
        }
        
        // Save the order item
        OrderItem savedItem = orderItemRepository.save(orderItem);
        
        if (savedItem != null) {
            // Load the order items
            List<OrderItem> orderItems = orderItemRepository.findByOrderId(orderId);
            order.setOrderItems(orderItems);
            
            // Update the order total
            orderRepository.update(order);
        }
        
        return order;
    }
    
    @Override
    public Order removeItemFromOrder(int orderId, int orderItemId) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        
        if (orderOpt.isEmpty()) {
            throw new IllegalArgumentException("Order not found");
        }
        
        Order order = orderOpt.get();
        
        // Check if the order item exists
        Optional<OrderItem> orderItemOpt = orderItemRepository.findById(orderItemId);
        
        if (orderItemOpt.isEmpty()) {
            throw new IllegalArgumentException("Order item not found");
        }
        
        OrderItem orderItem = orderItemOpt.get();
        
        // Check if the order item belongs to the order
        if (orderItem.getOrderId() != orderId) {
            throw new IllegalArgumentException("Order item does not belong to the order");
        }
        
        // Delete the order item
        boolean deleted = orderItemRepository.deleteById(orderItemId);
        
        if (deleted) {
            // Load the order items
            List<OrderItem> orderItems = orderItemRepository.findByOrderId(orderId);
            order.setOrderItems(orderItems);
            
            // Update the order total
            orderRepository.update(order);
        }
        
        return order;
    }
    
    @Override
    public Optional<Order> getOrderById(int id) {
        Optional<Order> orderOpt = orderRepository.findById(id);
        
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            
            // Load the order items
            List<OrderItem> orderItems = orderItemRepository.findByOrderId(id);
            order.setOrderItems(orderItems);
        }
        
        return orderOpt;
    }
    
    @Override
    public List<Order> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        
        // Load the order items for each order
        for (Order order : orders) {
            List<OrderItem> orderItems = orderItemRepository.findByOrderId(order.getId());
            order.setOrderItems(orderItems);
        }
        
        return orders;
    }
    
    @Override
    public List<Order> getOrdersByCustomerId(int customerId) {
        List<Order> orders = orderRepository.findByCustomerId(customerId);
        
        // Load the order items for each order
        for (Order order : orders) {
            List<OrderItem> orderItems = orderItemRepository.findByOrderId(order.getId());
            order.setOrderItems(orderItems);
        }
        
        return orders;
    }
    
    @Override
    public List<Order> getOrdersByStatus(String status) {
        List<Order> orders = orderRepository.findByStatus(status);
        
        // Load the order items for each order
        for (Order order : orders) {
            List<OrderItem> orderItems = orderItemRepository.findByOrderId(order.getId());
            order.setOrderItems(orderItems);
        }
        
        return orders;
    }
    
    @Override
    public Order updateOrderStatus(int orderId, String status) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        
        if (orderOpt.isEmpty()) {
            throw new IllegalArgumentException("Order not found");
        }
        
        Order order = orderOpt.get();
        order.setStatus(status);
        
        Order updatedOrder = orderRepository.update(order);
        
        if (updatedOrder != null) {
            // Load the order items
            List<OrderItem> orderItems = orderItemRepository.findByOrderId(orderId);
            updatedOrder.setOrderItems(orderItems);
        }
        
        return updatedOrder;
    }
    
    @Override
    public Order updatePaymentStatus(int orderId, String paymentStatus) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        
        if (orderOpt.isEmpty()) {
            throw new IllegalArgumentException("Order not found");
        }
        
        Order order = orderOpt.get();
        order.setPaymentStatus(paymentStatus);
        
        Order updatedOrder = orderRepository.update(order);
        
        if (updatedOrder != null) {
            // Load the order items
            List<OrderItem> orderItems = orderItemRepository.findByOrderId(orderId);
            updatedOrder.setOrderItems(orderItems);
        }
        
        return updatedOrder;
    }
    
    @Override
    public Order cancelOrder(int orderId) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        
        if (orderOpt.isEmpty()) {
            throw new IllegalArgumentException("Order not found");
        }
        
        Order order = orderOpt.get();
        order.setStatus(Order.STATUS_CANCELLED);
        
        Order updatedOrder = orderRepository.update(order);
        
        if (updatedOrder != null) {
            // Load the order items
            List<OrderItem> orderItems = orderItemRepository.findByOrderId(orderId);
            updatedOrder.setOrderItems(orderItems);
        }
        
        return updatedOrder;
    }
    
    @Override
    public boolean deleteOrder(int orderId) {
        // Delete the order items first
        orderItemRepository.deleteByOrderId(orderId);
        
        // Delete the order
        return orderRepository.deleteById(orderId);
    }
}
