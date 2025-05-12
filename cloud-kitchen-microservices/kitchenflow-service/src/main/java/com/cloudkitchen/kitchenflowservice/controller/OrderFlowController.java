package com.cloudkitchen.kitchenflowservice.controller;

import com.cloudkitchen.kitchenflowservice.dto.AssignDeliveryPersonRequest;
import com.cloudkitchen.kitchenflowservice.dto.OrderFlowDTO;
import com.cloudkitchen.kitchenflowservice.dto.StatusUpdateDTO;
import com.cloudkitchen.kitchenflowservice.dto.UpdateStatusRequest;
import com.cloudkitchen.kitchenflowservice.service.OrderFlowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/kitchen-flow")
@RequiredArgsConstructor
public class OrderFlowController {
    
    private final OrderFlowService orderFlowService;
    
    @PostMapping("/orders/{orderId}/initialize")
    public ResponseEntity<OrderFlowDTO> initializeOrderFlow(@PathVariable Long orderId) {
        OrderFlowDTO orderFlow = orderFlowService.initializeOrderFlow(orderId);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderFlow);
    }
    
    @GetMapping("/orders/{orderId}")
    public ResponseEntity<OrderFlowDTO> getOrderFlowById(@PathVariable Long orderId) {
        OrderFlowDTO orderFlow = orderFlowService.getOrderFlowById(orderId);
        return ResponseEntity.ok(orderFlow);
    }
    
    @GetMapping("/orders")
    public ResponseEntity<List<OrderFlowDTO>> getAllOrderFlows() {
        List<OrderFlowDTO> orderFlows = orderFlowService.getAllOrderFlows();
        return ResponseEntity.ok(orderFlows);
    }
    
    @GetMapping("/orders/status/{status}")
    public ResponseEntity<List<OrderFlowDTO>> getOrderFlowsByStatus(@PathVariable String status) {
        List<OrderFlowDTO> orderFlows = orderFlowService.getOrderFlowsByStatus(status);
        return ResponseEntity.ok(orderFlows);
    }
    
    @GetMapping("/orders/customer/{customerId}")
    public ResponseEntity<List<OrderFlowDTO>> getOrderFlowsByCustomerId(@PathVariable Long customerId) {
        List<OrderFlowDTO> orderFlows = orderFlowService.getOrderFlowsByCustomerId(customerId);
        return ResponseEntity.ok(orderFlows);
    }
    
    @GetMapping("/orders/delivery-person/{deliveryPerson}")
    public ResponseEntity<List<OrderFlowDTO>> getOrderFlowsByDeliveryPerson(@PathVariable String deliveryPerson) {
        List<OrderFlowDTO> orderFlows = orderFlowService.getOrderFlowsByDeliveryPerson(deliveryPerson);
        return ResponseEntity.ok(orderFlows);
    }
    
    @PatchMapping("/orders/{orderId}/status")
    public ResponseEntity<OrderFlowDTO> updateOrderStatus(
            @PathVariable Long orderId,
            @Valid @RequestBody UpdateStatusRequest request) {
        OrderFlowDTO orderFlow = orderFlowService.updateOrderStatus(
                orderId, request.getStatus(), request.getNotes());
        return ResponseEntity.ok(orderFlow);
    }
    
    @PatchMapping("/orders/{orderId}/assign")
    public ResponseEntity<OrderFlowDTO> assignDeliveryPerson(
            @PathVariable Long orderId,
            @Valid @RequestBody AssignDeliveryPersonRequest request) {
        OrderFlowDTO orderFlow = orderFlowService.assignDeliveryPerson(
                orderId, request.getDeliveryPerson());
        return ResponseEntity.ok(orderFlow);
    }
    
    @GetMapping("/orders/{orderId}/history")
    public ResponseEntity<List<StatusUpdateDTO>> getOrderStatusHistory(@PathVariable Long orderId) {
        List<StatusUpdateDTO> statusHistory = orderFlowService.getOrderStatusHistory(orderId);
        return ResponseEntity.ok(statusHistory);
    }
    
    @DeleteMapping("/orders/{orderId}")
    public ResponseEntity<Void> deleteOrderFlow(@PathVariable Long orderId) {
        orderFlowService.deleteOrderFlow(orderId);
        return ResponseEntity.noContent().build();
    }
}
