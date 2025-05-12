package com.cloudkitchen.deliveryservice.controller;

import com.cloudkitchen.deliveryservice.dto.AssignDeliveryPersonRequest;
import com.cloudkitchen.deliveryservice.dto.CreateDeliveryRequest;
import com.cloudkitchen.deliveryservice.dto.DeliveryDTO;
import com.cloudkitchen.deliveryservice.dto.UpdateDeliveryStatusRequest;
import com.cloudkitchen.deliveryservice.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/deliveries")
@RequiredArgsConstructor
public class DeliveryController {
    
    private final DeliveryService deliveryService;
    
    @PostMapping
    public ResponseEntity<DeliveryDTO> createDelivery(@Valid @RequestBody CreateDeliveryRequest createDeliveryRequest) {
        DeliveryDTO delivery = deliveryService.createDelivery(createDeliveryRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(delivery);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<DeliveryDTO> getDeliveryById(@PathVariable Long id) {
        DeliveryDTO delivery = deliveryService.getDeliveryById(id);
        return ResponseEntity.ok(delivery);
    }
    
    @GetMapping("/order/{orderId}")
    public ResponseEntity<DeliveryDTO> getDeliveryByOrderId(@PathVariable Long orderId) {
        DeliveryDTO delivery = deliveryService.getDeliveryByOrderId(orderId);
        return ResponseEntity.ok(delivery);
    }
    
    @GetMapping
    public ResponseEntity<List<DeliveryDTO>> getAllDeliveries() {
        List<DeliveryDTO> deliveries = deliveryService.getAllDeliveries();
        return ResponseEntity.ok(deliveries);
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<List<DeliveryDTO>> getDeliveriesByStatus(@PathVariable String status) {
        List<DeliveryDTO> deliveries = deliveryService.getDeliveriesByStatus(status);
        return ResponseEntity.ok(deliveries);
    }
    
    @GetMapping("/person/{deliveryPerson}")
    public ResponseEntity<List<DeliveryDTO>> getDeliveriesByDeliveryPerson(@PathVariable String deliveryPerson) {
        List<DeliveryDTO> deliveries = deliveryService.getDeliveriesByDeliveryPerson(deliveryPerson);
        return ResponseEntity.ok(deliveries);
    }
    
    @PatchMapping("/{id}/assign")
    public ResponseEntity<DeliveryDTO> assignDeliveryPerson(@PathVariable Long id, @Valid @RequestBody AssignDeliveryPersonRequest request) {
        DeliveryDTO delivery = deliveryService.assignDeliveryPerson(id, request.getDeliveryPerson());
        return ResponseEntity.ok(delivery);
    }
    
    @PatchMapping("/{id}/status")
    public ResponseEntity<DeliveryDTO> updateDeliveryStatus(@PathVariable Long id, @Valid @RequestBody UpdateDeliveryStatusRequest request) {
        DeliveryDTO delivery = deliveryService.updateDeliveryStatus(id, request.getDeliveryStatus());
        return ResponseEntity.ok(delivery);
    }
    
    @PostMapping("/{id}/deliver")
    public ResponseEntity<DeliveryDTO> markAsDelivered(@PathVariable Long id) {
        DeliveryDTO delivery = deliveryService.markAsDelivered(id);
        return ResponseEntity.ok(delivery);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDelivery(@PathVariable Long id) {
        deliveryService.deleteDelivery(id);
        return ResponseEntity.noContent().build();
    }
}
