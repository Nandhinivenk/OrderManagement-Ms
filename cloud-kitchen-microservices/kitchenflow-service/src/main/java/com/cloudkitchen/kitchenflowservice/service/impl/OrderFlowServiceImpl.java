package com.cloudkitchen.kitchenflowservice.service.impl;

import com.cloudkitchen.kitchenflowservice.client.DeliveryClient;
import com.cloudkitchen.kitchenflowservice.client.OrderClient;
import com.cloudkitchen.kitchenflowservice.dto.OrderDTO;
import com.cloudkitchen.kitchenflowservice.dto.OrderFlowDTO;
import com.cloudkitchen.kitchenflowservice.dto.StatusUpdateDTO;
import com.cloudkitchen.kitchenflowservice.exception.InvalidStatusTransitionException;
import com.cloudkitchen.kitchenflowservice.exception.OrderFlowNotFoundException;
import com.cloudkitchen.kitchenflowservice.model.OrderFlow;
import com.cloudkitchen.kitchenflowservice.model.StatusUpdate;
import com.cloudkitchen.kitchenflowservice.repository.OrderFlowRepository;
import com.cloudkitchen.kitchenflowservice.repository.StatusUpdateRepository;
import com.cloudkitchen.kitchenflowservice.service.OrderFlowService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderFlowServiceImpl implements OrderFlowService {

    private final OrderFlowRepository orderFlowRepository;
    private final StatusUpdateRepository statusUpdateRepository;
    private final OrderClient orderClient;
    private final DeliveryClient deliveryClient;

    @Override
    @Transactional
    public OrderFlowDTO initializeOrderFlow(Long orderId) {
        // Check if order flow already exists
        if (orderFlowRepository.existsById(orderId)) {
            return getOrderFlowById(orderId);
        }

        // Get order details from order service
        OrderDTO orderDTO;
        try {
            orderDTO = orderClient.getOrderById(orderId).getBody();
            if (orderDTO == null) {
                throw new RuntimeException("Order not found with id: " + orderId);
            }
        } catch (FeignException e) {
            throw new RuntimeException("Failed to fetch order details: " + e.getMessage());
        }

        // Create new order flow
        OrderFlow orderFlow = new OrderFlow();
        orderFlow.setOrderId(orderId);
        orderFlow.setCustomerId(orderDTO.getCustomerId());
        orderFlow.setCustomerName(orderDTO.getCustomerName());
        orderFlow.setOrderDate(orderDTO.getOrderDate());
        orderFlow.setCurrentStatus(OrderFlow.STATUS_RECEIVED);

        // Add initial status update
        orderFlow.addStatusUpdate(OrderFlow.STATUS_RECEIVED, "Order received");

        // Save order flow
        OrderFlow savedOrderFlow = orderFlowRepository.save(orderFlow);

        // Return DTO
        return mapToDTO(savedOrderFlow);
    }

    @Override
    public OrderFlowDTO getOrderFlowById(Long orderId) {
        OrderFlow orderFlow = orderFlowRepository.findById(orderId)
                .orElseThrow(() -> new OrderFlowNotFoundException("Order flow not found for order id: " + orderId));

        return mapToDTO(orderFlow);
    }

    @Override
    public List<OrderFlowDTO> getAllOrderFlows() {
        return orderFlowRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderFlowDTO> getOrderFlowsByStatus(String status) {
        return orderFlowRepository.findByCurrentStatus(status).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderFlowDTO> getOrderFlowsByCustomerId(Long customerId) {
        return orderFlowRepository.findByCustomerId(customerId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderFlowDTO> getOrderFlowsByDeliveryPerson(String deliveryPerson) {
        return orderFlowRepository.findByDeliveryPerson(deliveryPerson).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OrderFlowDTO updateOrderStatus(Long orderId, String status, String notes) {
        OrderFlow orderFlow = orderFlowRepository.findById(orderId)
                .orElseThrow(() -> new OrderFlowNotFoundException("Order flow not found for order id: " + orderId));

        // Validate status transition
        validateStatusTransition(orderFlow.getCurrentStatus(), status);

        // Update order flow status
        orderFlow.addStatusUpdate(status, notes);

        // If status is ASSIGNED, update estimated delivery time
        if (OrderFlow.STATUS_ASSIGNED.equals(status)) {
            orderFlow.setEstimatedDeliveryTime(LocalDateTime.now().plusMinutes(30));
        }

        // Save order flow
        OrderFlow updatedOrderFlow = orderFlowRepository.save(orderFlow);

        // Update order status in order service
        try {
            orderClient.updateOrderStatus(orderId, status);
        } catch (FeignException e) {
            // Log error but continue
            System.err.println("Failed to update order status in order service: " + e.getMessage());
        }

        // Update delivery status if applicable
        if (OrderFlow.STATUS_ASSIGNED.equals(status) ||
            OrderFlow.STATUS_IN_TRANSIT.equals(status) ||
            OrderFlow.STATUS_DELIVERED.equals(status)) {
            try {
                Map<String, String> request = new HashMap<>();
                request.put("deliveryStatus", status);
                deliveryClient.updateDeliveryStatus(orderId, request);
            } catch (FeignException e) {
                // Log error but continue
                System.err.println("Failed to update delivery status in delivery service: " + e.getMessage());
            }
        }

        return mapToDTO(updatedOrderFlow);
    }

    @Override
    @Transactional
    public OrderFlowDTO assignDeliveryPerson(Long orderId, String deliveryPerson) {
        OrderFlow orderFlow = orderFlowRepository.findById(orderId)
                .orElseThrow(() -> new OrderFlowNotFoundException("Order flow not found for order id: " + orderId));

        // Update delivery person
        orderFlow.setDeliveryPerson(deliveryPerson);

        // Update status to ASSIGNED if it's in READY state
        if (OrderFlow.STATUS_READY.equals(orderFlow.getCurrentStatus())) {
            orderFlow.addStatusUpdate(OrderFlow.STATUS_ASSIGNED, "Assigned to delivery person: " + deliveryPerson);
            orderFlow.setEstimatedDeliveryTime(LocalDateTime.now().plusMinutes(30));
        }

        // Save order flow
        OrderFlow updatedOrderFlow = orderFlowRepository.save(orderFlow);

        // Update delivery person in delivery service
        try {
            Map<String, String> request = new HashMap<>();
            request.put("deliveryPerson", deliveryPerson);
            deliveryClient.assignDeliveryPerson(orderId, request);
        } catch (FeignException e) {
            // Log error but continue
            System.err.println("Failed to assign delivery person in delivery service: " + e.getMessage());
        }

        return mapToDTO(updatedOrderFlow);
    }

    @Override
    public List<StatusUpdateDTO> getOrderStatusHistory(Long orderId) {
        // Check if order flow exists
        if (!orderFlowRepository.existsById(orderId)) {
            throw new OrderFlowNotFoundException("Order flow not found for order id: " + orderId);
        }

        return statusUpdateRepository.findByOrderFlowOrderIdOrderByUpdateTimeDesc(orderId).stream()
                .map(this::mapToStatusUpdateDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteOrderFlow(Long orderId) {
        if (!orderFlowRepository.existsById(orderId)) {
            throw new OrderFlowNotFoundException("Order flow not found for order id: " + orderId);
        }

        orderFlowRepository.deleteById(orderId);
    }

    private void validateStatusTransition(String currentStatus, String newStatus) {
        // Define valid transitions
        boolean validTransition;
        switch (currentStatus) {
            case OrderFlow.STATUS_RECEIVED:
                validTransition =
                    newStatus.equals(OrderFlow.STATUS_CONFIRMED) ||
                    newStatus.equals(OrderFlow.STATUS_CANCELLED);
                break;

            case OrderFlow.STATUS_CONFIRMED:
                validTransition =
                    newStatus.equals(OrderFlow.STATUS_PREPARING) ||
                    newStatus.equals(OrderFlow.STATUS_CANCELLED);
                break;

            case OrderFlow.STATUS_PREPARING:
                validTransition =
                    newStatus.equals(OrderFlow.STATUS_READY) ||
                    newStatus.equals(OrderFlow.STATUS_CANCELLED);
                break;

            case OrderFlow.STATUS_READY:
                validTransition =
                    newStatus.equals(OrderFlow.STATUS_ASSIGNED) ||
                    newStatus.equals(OrderFlow.STATUS_CANCELLED);
                break;

            case OrderFlow.STATUS_ASSIGNED:
                validTransition =
                    newStatus.equals(OrderFlow.STATUS_IN_TRANSIT) ||
                    newStatus.equals(OrderFlow.STATUS_CANCELLED);
                break;

            case OrderFlow.STATUS_IN_TRANSIT:
                validTransition =
                    newStatus.equals(OrderFlow.STATUS_DELIVERED) ||
                    newStatus.equals(OrderFlow.STATUS_CANCELLED);
                break;

            case OrderFlow.STATUS_DELIVERED:
            case OrderFlow.STATUS_CANCELLED:
                validTransition = false;
                break;

            default:
                validTransition = false;
                break;
        }

        if (!validTransition) {
            throw new InvalidStatusTransitionException(
                    "Invalid status transition from " + currentStatus + " to " + newStatus);
        }
    }

    private OrderFlowDTO mapToDTO(OrderFlow orderFlow) {
        OrderFlowDTO dto = new OrderFlowDTO();
        dto.setOrderId(orderFlow.getOrderId());
        dto.setCurrentStatus(orderFlow.getCurrentStatus());
        dto.setCustomerId(orderFlow.getCustomerId());
        dto.setCustomerName(orderFlow.getCustomerName());
        dto.setOrderDate(orderFlow.getOrderDate());
        dto.setDeliveryPerson(orderFlow.getDeliveryPerson());
        dto.setEstimatedDeliveryTime(orderFlow.getEstimatedDeliveryTime());

        // Map status updates
        dto.setStatusUpdates(orderFlow.getStatusUpdates().stream()
                .map(this::mapToStatusUpdateDTO)
                .collect(Collectors.toList()));

        return dto;
    }

    private StatusUpdateDTO mapToStatusUpdateDTO(StatusUpdate statusUpdate) {
        StatusUpdateDTO dto = new StatusUpdateDTO();
        dto.setId(statusUpdate.getId());
        dto.setStatus(statusUpdate.getStatus());
        dto.setNotes(statusUpdate.getNotes());
        dto.setUpdateTime(statusUpdate.getUpdateTime());
        return dto;
    }
}
