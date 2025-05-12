package com.cloudkitchen.service.impl;

import com.cloudkitchen.model.Delivery;
import com.cloudkitchen.model.Order;
import com.cloudkitchen.repository.DeliveryRepository;
import com.cloudkitchen.repository.OrderRepository;
import com.cloudkitchen.repository.impl.DeliveryRepositoryImpl;
import com.cloudkitchen.repository.impl.OrderRepositoryImpl;
import com.cloudkitchen.service.DeliveryService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of DeliveryService
 */
public class DeliveryServiceImpl implements DeliveryService {
    
    private final DeliveryRepository deliveryRepository;
    private final OrderRepository orderRepository;
    
    public DeliveryServiceImpl() {
        this.deliveryRepository = new DeliveryRepositoryImpl();
        this.orderRepository = new OrderRepositoryImpl();
    }
    
    @Override
    public Delivery createDelivery(int orderId, String deliveryAddress) {
        // Check if the order exists
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        
        if (orderOpt.isEmpty()) {
            throw new IllegalArgumentException("Order not found");
        }
        
        // Check if a delivery already exists for this order
        Optional<Delivery> existingDelivery = deliveryRepository.findByOrderId(orderId);
        
        if (existingDelivery.isPresent()) {
            throw new IllegalArgumentException("A delivery already exists for this order");
        }
        
        // Create a new delivery
        Delivery delivery = new Delivery(orderId, deliveryAddress);
        
        return deliveryRepository.save(delivery);
    }
    
    @Override
    public Optional<Delivery> getDeliveryById(int id) {
        return deliveryRepository.findById(id);
    }
    
    @Override
    public Optional<Delivery> getDeliveryByOrderId(int orderId) {
        return deliveryRepository.findByOrderId(orderId);
    }
    
    @Override
    public List<Delivery> getAllDeliveries() {
        return deliveryRepository.findAll();
    }
    
    @Override
    public List<Delivery> getDeliveriesByStatus(String status) {
        return deliveryRepository.findByStatus(status);
    }
    
    @Override
    public List<Delivery> getDeliveriesByDeliveryPerson(String deliveryPerson) {
        return deliveryRepository.findByDeliveryPerson(deliveryPerson);
    }
    
    @Override
    public Delivery assignDeliveryPerson(int deliveryId, String deliveryPerson) {
        Optional<Delivery> deliveryOpt = deliveryRepository.findById(deliveryId);
        
        if (deliveryOpt.isEmpty()) {
            throw new IllegalArgumentException("Delivery not found");
        }
        
        Delivery delivery = deliveryOpt.get();
        delivery.setDeliveryPerson(deliveryPerson);
        delivery.setDeliveryStatus(Delivery.STATUS_ASSIGNED);
        
        return deliveryRepository.update(delivery);
    }
    
    @Override
    public Delivery updateDeliveryStatus(int deliveryId, String status) {
        Optional<Delivery> deliveryOpt = deliveryRepository.findById(deliveryId);
        
        if (deliveryOpt.isEmpty()) {
            throw new IllegalArgumentException("Delivery not found");
        }
        
        Delivery delivery = deliveryOpt.get();
        delivery.setDeliveryStatus(status);
        
        // If the status is DELIVERED, set the delivery time
        if (status.equals(Delivery.STATUS_DELIVERED) && delivery.getDeliveryTime() == null) {
            delivery.setDeliveryTime(LocalDateTime.now());
        }
        
        return deliveryRepository.update(delivery);
    }
    
    @Override
    public Delivery markAsDelivered(int deliveryId, LocalDateTime deliveryTime) {
        Optional<Delivery> deliveryOpt = deliveryRepository.findById(deliveryId);
        
        if (deliveryOpt.isEmpty()) {
            throw new IllegalArgumentException("Delivery not found");
        }
        
        Delivery delivery = deliveryOpt.get();
        delivery.setDeliveryStatus(Delivery.STATUS_DELIVERED);
        delivery.setDeliveryTime(deliveryTime);
        
        // Update the order status
        Optional<Order> orderOpt = orderRepository.findById(delivery.getOrderId());
        
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            order.setStatus(Order.STATUS_DELIVERED);
            orderRepository.update(order);
        }
        
        return deliveryRepository.update(delivery);
    }
    
    @Override
    public boolean deleteDelivery(int deliveryId) {
        return deliveryRepository.deleteById(deliveryId);
    }
}
