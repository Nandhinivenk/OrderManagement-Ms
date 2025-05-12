package com.cloudkitchen.deliveryservice.service.impl;

import com.cloudkitchen.deliveryservice.client.OrderClient;
import com.cloudkitchen.deliveryservice.dto.CreateDeliveryRequest;
import com.cloudkitchen.deliveryservice.dto.DeliveryDTO;
import com.cloudkitchen.deliveryservice.dto.OrderDTO;
import com.cloudkitchen.deliveryservice.exception.DeliveryAlreadyExistsException;
import com.cloudkitchen.deliveryservice.exception.DeliveryNotFoundException;
import com.cloudkitchen.deliveryservice.exception.OrderNotFoundException;
import com.cloudkitchen.deliveryservice.model.Delivery;
import com.cloudkitchen.deliveryservice.repository.DeliveryRepository;
import com.cloudkitchen.deliveryservice.service.DeliveryService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {
    
    private final DeliveryRepository deliveryRepository;
    private final OrderClient orderClient;
    
    @Override
    @Transactional
    public DeliveryDTO createDelivery(CreateDeliveryRequest createDeliveryRequest) {
        // Check if delivery already exists for this order
        Optional<Delivery> existingDelivery = deliveryRepository.findByOrderId(createDeliveryRequest.getOrderId());
        if (existingDelivery.isPresent()) {
            throw new DeliveryAlreadyExistsException("Delivery already exists for order ID: " + createDeliveryRequest.getOrderId());
        }
        
        // Validate order
        OrderDTO order;
        try {
            order = orderClient.getOrderById(createDeliveryRequest.getOrderId()).getBody();
            if (order == null) {
                throw new OrderNotFoundException("Order not found with id: " + createDeliveryRequest.getOrderId());
            }
        } catch (FeignException e) {
            throw new OrderNotFoundException("Order not found with id: " + createDeliveryRequest.getOrderId());
        }
        
        // Create delivery
        Delivery delivery = new Delivery();
        delivery.setOrderId(createDeliveryRequest.getOrderId());
        delivery.setDeliveryAddress(createDeliveryRequest.getDeliveryAddress());
        delivery.setDeliveryStatus(Delivery.STATUS_PENDING);
        
        // Save delivery
        Delivery savedDelivery = deliveryRepository.save(delivery);
        
        // Return DTO
        return mapToDTO(savedDelivery, order);
    }
    
    @Override
    public DeliveryDTO getDeliveryById(Long id) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new DeliveryNotFoundException("Delivery not found with id: " + id));
        
        // Get order information
        OrderDTO order = null;
        try {
            order = orderClient.getOrderById(delivery.getOrderId()).getBody();
        } catch (Exception e) {
            // Ignore if order service is not available
        }
        
        return mapToDTO(delivery, order);
    }
    
    @Override
    public DeliveryDTO getDeliveryByOrderId(Long orderId) {
        Delivery delivery = deliveryRepository.findByOrderId(orderId)
                .orElseThrow(() -> new DeliveryNotFoundException("Delivery not found for order id: " + orderId));
        
        // Get order information
        OrderDTO order = null;
        try {
            order = orderClient.getOrderById(delivery.getOrderId()).getBody();
        } catch (Exception e) {
            // Ignore if order service is not available
        }
        
        return mapToDTO(delivery, order);
    }
    
    @Override
    public List<DeliveryDTO> getAllDeliveries() {
        return deliveryRepository.findAll().stream()
                .map(delivery -> {
                    // Get order information
                    OrderDTO order = null;
                    try {
                        order = orderClient.getOrderById(delivery.getOrderId()).getBody();
                    } catch (Exception e) {
                        // Ignore if order service is not available
                    }
                    return mapToDTO(delivery, order);
                })
                .collect(Collectors.toList());
    }
    
    @Override
    public List<DeliveryDTO> getDeliveriesByStatus(String status) {
        return deliveryRepository.findByDeliveryStatus(status).stream()
                .map(delivery -> {
                    // Get order information
                    OrderDTO order = null;
                    try {
                        order = orderClient.getOrderById(delivery.getOrderId()).getBody();
                    } catch (Exception e) {
                        // Ignore if order service is not available
                    }
                    return mapToDTO(delivery, order);
                })
                .collect(Collectors.toList());
    }
    
    @Override
    public List<DeliveryDTO> getDeliveriesByDeliveryPerson(String deliveryPerson) {
        return deliveryRepository.findByDeliveryPerson(deliveryPerson).stream()
                .map(delivery -> {
                    // Get order information
                    OrderDTO order = null;
                    try {
                        order = orderClient.getOrderById(delivery.getOrderId()).getBody();
                    } catch (Exception e) {
                        // Ignore if order service is not available
                    }
                    return mapToDTO(delivery, order);
                })
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public DeliveryDTO assignDeliveryPerson(Long id, String deliveryPerson) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new DeliveryNotFoundException("Delivery not found with id: " + id));
        
        delivery.setDeliveryPerson(deliveryPerson);
        delivery.setDeliveryStatus(Delivery.STATUS_ASSIGNED);
        
        Delivery updatedDelivery = deliveryRepository.save(delivery);
        
        // Get order information
        OrderDTO order = null;
        try {
            order = orderClient.getOrderById(delivery.getOrderId()).getBody();
        } catch (Exception e) {
            // Ignore if order service is not available
        }
        
        return mapToDTO(updatedDelivery, order);
    }
    
    @Override
    @Transactional
    public DeliveryDTO updateDeliveryStatus(Long id, String status) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new DeliveryNotFoundException("Delivery not found with id: " + id));
        
        delivery.setDeliveryStatus(status);
        
        // If status is DELIVERED, set delivery time
        if (Delivery.STATUS_DELIVERED.equals(status)) {
            delivery.setDeliveryTime(LocalDateTime.now());
            
            // Update order status
            try {
                orderClient.updateOrderStatus(delivery.getOrderId(), "DELIVERED");
            } catch (Exception e) {
                // Ignore if order service is not available
            }
        }
        
        Delivery updatedDelivery = deliveryRepository.save(delivery);
        
        // Get order information
        OrderDTO order = null;
        try {
            order = orderClient.getOrderById(delivery.getOrderId()).getBody();
        } catch (Exception e) {
            // Ignore if order service is not available
        }
        
        return mapToDTO(updatedDelivery, order);
    }
    
    @Override
    @Transactional
    public DeliveryDTO markAsDelivered(Long id) {
        return updateDeliveryStatus(id, Delivery.STATUS_DELIVERED);
    }
    
    @Override
    @Transactional
    public void deleteDelivery(Long id) {
        if (!deliveryRepository.existsById(id)) {
            throw new DeliveryNotFoundException("Delivery not found with id: " + id);
        }
        
        deliveryRepository.deleteById(id);
    }
    
    private DeliveryDTO mapToDTO(Delivery delivery, OrderDTO order) {
        DeliveryDTO dto = new DeliveryDTO();
        dto.setId(delivery.getId());
        dto.setOrderId(delivery.getOrderId());
        dto.setDeliveryPerson(delivery.getDeliveryPerson());
        dto.setDeliveryStatus(delivery.getDeliveryStatus());
        dto.setDeliveryTime(delivery.getDeliveryTime());
        dto.setDeliveryAddress(delivery.getDeliveryAddress());
        
        if (order != null) {
            dto.setOrderStatus(order.getStatus());
            dto.setCustomerName(order.getCustomerName());
        }
        
        return dto;
    }
}
