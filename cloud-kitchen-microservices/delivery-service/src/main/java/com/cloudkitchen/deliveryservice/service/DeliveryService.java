package com.cloudkitchen.deliveryservice.service;

import com.cloudkitchen.deliveryservice.dto.CreateDeliveryRequest;
import com.cloudkitchen.deliveryservice.dto.DeliveryDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface DeliveryService {
    
    DeliveryDTO createDelivery(CreateDeliveryRequest createDeliveryRequest);
    
    DeliveryDTO getDeliveryById(Long id);
    
    DeliveryDTO getDeliveryByOrderId(Long orderId);
    
    List<DeliveryDTO> getAllDeliveries();
    
    List<DeliveryDTO> getDeliveriesByStatus(String status);
    
    List<DeliveryDTO> getDeliveriesByDeliveryPerson(String deliveryPerson);
    
    DeliveryDTO assignDeliveryPerson(Long id, String deliveryPerson);
    
    DeliveryDTO updateDeliveryStatus(Long id, String status);
    
    DeliveryDTO markAsDelivered(Long id);
    
    void deleteDelivery(Long id);
}
