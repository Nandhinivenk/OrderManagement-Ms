package com.cloudkitchen.adminservice.service.impl;

import com.cloudkitchen.adminservice.client.*;
import com.cloudkitchen.adminservice.service.AuditLogService;
import com.cloudkitchen.adminservice.service.DashboardService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final CustomerClient customerClient;
    private final OrderClient orderClient;
    private final KitchenFlowClient kitchenFlowClient;
    private final DeliveryClient deliveryClient;
    private final InventoryClient inventoryClient;
    private final AuditLogService auditLogService;

    @Override
    public List<Map<String, Object>> getAllCustomers() {
        try {
            ResponseEntity<List<Map<String, Object>>> response = customerClient.getAllCustomers();
            return response.getBody() != null ? response.getBody() : Collections.emptyList();
        } catch (FeignException e) {
            logError("Failed to get all customers", e);
            return Collections.emptyList();
        }
    }

    @Override
    public Map<String, Object> getCustomerById(Long id) {
        try {
            ResponseEntity<Map<String, Object>> response = customerClient.getCustomerById(id);
            return response.getBody() != null ? response.getBody() : Collections.emptyMap();
        } catch (FeignException e) {
            logError("Failed to get customer with id: " + id, e);
            return Collections.emptyMap();
        }
    }

    @Override
    public List<Map<String, Object>> getAllOrders() {
        try {
            ResponseEntity<List<Map<String, Object>>> response = orderClient.getAllOrders();
            return response.getBody() != null ? response.getBody() : Collections.emptyList();
        } catch (FeignException e) {
            logError("Failed to get all orders", e);
            return Collections.emptyList();
        }
    }

    @Override
    public Map<String, Object> getOrderById(Long id) {
        try {
            ResponseEntity<Map<String, Object>> response = orderClient.getOrderById(id);
            return response.getBody() != null ? response.getBody() : Collections.emptyMap();
        } catch (FeignException e) {
            logError("Failed to get order with id: " + id, e);
            return Collections.emptyMap();
        }
    }

    @Override
    public Map<String, Object> updateOrderStatus(Long id, String status) {
        try {
            Map<String, String> statusUpdate = new HashMap<>();
            statusUpdate.put("status", status);

            ResponseEntity<Map<String, Object>> response = orderClient.updateOrderStatus(id, statusUpdate);

            // Log the action
            String adminUsername = SecurityContextHolder.getContext().getAuthentication().getName();
            auditLogService.logAction(
                    adminUsername,
                    "UPDATE_STATUS",
                    "ORDER",
                    id,
                    "Order status updated to: " + status
            );

            return response.getBody() != null ? response.getBody() : Collections.emptyMap();
        } catch (FeignException e) {
            logError("Failed to update order status for id: " + id, e);
            return Collections.emptyMap();
        }
    }

    @Override
    public List<Map<String, Object>> getAllOrderFlows() {
        try {
            ResponseEntity<List<Map<String, Object>>> response = kitchenFlowClient.getAllOrderFlows();
            return response.getBody() != null ? response.getBody() : Collections.emptyList();
        } catch (FeignException e) {
            logError("Failed to get all order flows", e);
            return Collections.emptyList();
        }
    }

    @Override
    public Map<String, Object> getOrderFlowById(Long orderId) {
        try {
            ResponseEntity<Map<String, Object>> response = kitchenFlowClient.getOrderFlowById(orderId);
            return response.getBody() != null ? response.getBody() : Collections.emptyMap();
        } catch (FeignException e) {
            logError("Failed to get order flow for order id: " + orderId, e);
            return Collections.emptyMap();
        }
    }

    @Override
    public List<Map<String, Object>> getOrderFlowsByStatus(String status) {
        try {
            ResponseEntity<List<Map<String, Object>>> response = kitchenFlowClient.getOrderFlowsByStatus(status);
            return response.getBody() != null ? response.getBody() : Collections.emptyList();
        } catch (FeignException e) {
            logError("Failed to get order flows with status: " + status, e);
            return Collections.emptyList();
        }
    }

    @Override
    public Map<String, Object> updateOrderFlowStatus(Long orderId, String status, String notes) {
        try {
            Map<String, String> statusUpdate = new HashMap<>();
            statusUpdate.put("status", status);
            statusUpdate.put("notes", notes);

            ResponseEntity<Map<String, Object>> response = kitchenFlowClient.updateOrderStatus(orderId, statusUpdate);

            // Log the action
            String adminUsername = SecurityContextHolder.getContext().getAuthentication().getName();
            auditLogService.logAction(
                    adminUsername,
                    "UPDATE_KITCHEN_STATUS",
                    "ORDER",
                    orderId,
                    "Kitchen flow status updated to: " + status
            );

            return response.getBody() != null ? response.getBody() : Collections.emptyMap();
        } catch (FeignException e) {
            logError("Failed to update order flow status for order id: " + orderId, e);
            return Collections.emptyMap();
        }
    }

    @Override
    public List<Map<String, Object>> getAllDeliveries() {
        try {
            ResponseEntity<List<Map<String, Object>>> response = deliveryClient.getAllDeliveries();
            return response.getBody() != null ? response.getBody() : Collections.emptyList();
        } catch (FeignException e) {
            logError("Failed to get all deliveries", e);
            return Collections.emptyList();
        }
    }

    @Override
    public Map<String, Object> getDeliveryById(Long id) {
        try {
            ResponseEntity<Map<String, Object>> response = deliveryClient.getDeliveryById(id);
            return response.getBody() != null ? response.getBody() : Collections.emptyMap();
        } catch (FeignException e) {
            logError("Failed to get delivery with id: " + id, e);
            return Collections.emptyMap();
        }
    }

    @Override
    public Map<String, Object> getDeliveryByOrderId(Long orderId) {
        try {
            ResponseEntity<Map<String, Object>> response = deliveryClient.getDeliveryByOrderId(orderId);
            return response.getBody() != null ? response.getBody() : Collections.emptyMap();
        } catch (FeignException e) {
            logError("Failed to get delivery for order id: " + orderId, e);
            return Collections.emptyMap();
        }
    }

    @Override
    public Map<String, Object> assignDeliveryPerson(Long id, String deliveryPerson) {
        try {
            Map<String, String> assignRequest = new HashMap<>();
            assignRequest.put("deliveryPerson", deliveryPerson);

            ResponseEntity<Map<String, Object>> response = deliveryClient.assignDeliveryPerson(id, assignRequest);

            // Log the action
            String adminUsername = SecurityContextHolder.getContext().getAuthentication().getName();
            auditLogService.logAction(
                    adminUsername,
                    "ASSIGN_DELIVERY",
                    "DELIVERY",
                    id,
                    "Delivery person assigned: " + deliveryPerson
            );

            return response.getBody() != null ? response.getBody() : Collections.emptyMap();
        } catch (FeignException e) {
            logError("Failed to assign delivery person for delivery id: " + id, e);
            return Collections.emptyMap();
        }
    }

    @Override
    public Map<String, Object> updateDeliveryStatus(Long id, String status) {
        try {
            Map<String, String> statusUpdate = new HashMap<>();
            statusUpdate.put("deliveryStatus", status);

            ResponseEntity<Map<String, Object>> response = deliveryClient.updateDeliveryStatus(id, statusUpdate);

            // Log the action
            String adminUsername = SecurityContextHolder.getContext().getAuthentication().getName();
            auditLogService.logAction(
                    adminUsername,
                    "UPDATE_DELIVERY_STATUS",
                    "DELIVERY",
                    id,
                    "Delivery status updated to: " + status
            );

            return response.getBody() != null ? response.getBody() : Collections.emptyMap();
        } catch (FeignException e) {
            logError("Failed to update delivery status for id: " + id, e);
            return Collections.emptyMap();
        }
    }

    @Override
    public List<Map<String, Object>> getAllInventoryItems() {
        try {
            ResponseEntity<List<Map<String, Object>>> response = inventoryClient.getAllInventoryItems();
            return response.getBody() != null ? response.getBody() : Collections.emptyList();
        } catch (FeignException e) {
            logError("Failed to get all inventory items", e);
            return Collections.emptyList();
        }
    }

    @Override
    public Map<String, Object> getInventoryItemById(Long id) {
        try {
            ResponseEntity<Map<String, Object>> response = inventoryClient.getInventoryItemById(id);
            return response.getBody() != null ? response.getBody() : Collections.emptyMap();
        } catch (FeignException e) {
            logError("Failed to get inventory item with id: " + id, e);
            return Collections.emptyMap();
        }
    }

    @Override
    public Map<String, Object> updateInventoryQuantity(Long id, int quantity) {
        try {
            Map<String, Integer> quantityUpdate = new HashMap<>();
            quantityUpdate.put("quantity", quantity);

            ResponseEntity<Map<String, Object>> response = inventoryClient.updateInventoryQuantity(id, quantityUpdate);

            // Log the action
            String adminUsername = SecurityContextHolder.getContext().getAuthentication().getName();
            auditLogService.logAction(
                    adminUsername,
                    "UPDATE_INVENTORY",
                    "INVENTORY",
                    id,
                    "Inventory quantity updated to: " + quantity
            );

            return response.getBody() != null ? response.getBody() : Collections.emptyMap();
        } catch (FeignException e) {
            logError("Failed to update inventory quantity for id: " + id, e);
            return Collections.emptyMap();
        }
    }

    @Override
    public Map<String, Object> getDashboardSummary() {
        Map<String, Object> summary = new HashMap<>();

        try {
            // Get customer count
            List<Map<String, Object>> customers = getAllCustomers();
            summary.put("customerCount", customers.size());

            // Get order counts by status
            List<Map<String, Object>> orders = getAllOrders();
            summary.put("orderCount", orders.size());

            // Get kitchen flow counts by status
            List<Map<String, Object>> orderFlows = getAllOrderFlows();

            Map<String, Integer> orderStatusCounts = new HashMap<>();
            for (Map<String, Object> flow : orderFlows) {
                String status = (String) flow.get("currentStatus");
                orderStatusCounts.put(status, orderStatusCounts.getOrDefault(status, 0) + 1);
            }
            summary.put("orderStatusCounts", orderStatusCounts);

            // Get delivery counts
            List<Map<String, Object>> deliveries = getAllDeliveries();
            summary.put("deliveryCount", deliveries.size());

            // Get inventory items that need reordering
            List<Map<String, Object>> inventoryItems = getAllInventoryItems();
            List<Map<String, Object>> lowInventoryItems = inventoryItems.stream()
                    .filter(item -> {
                        Integer quantity = (Integer) item.get("quantity");
                        Integer reorderLevel = (Integer) item.get("reorderLevel");
                        return quantity <= reorderLevel;
                    })
                    .collect(Collectors.toList());
            summary.put("lowInventoryCount", lowInventoryItems.size());
            summary.put("lowInventoryItems", lowInventoryItems);

            return summary;
        } catch (Exception e) {
            logError("Failed to generate dashboard summary", e);
            return Collections.emptyMap();
        }
    }

    private void logError(String message, Exception e) {
        System.err.println(message + ": " + e.getMessage());

        // Log the error if we have an authenticated user
        try {
            String adminUsername = SecurityContextHolder.getContext().getAuthentication().getName();
            if (adminUsername != null && !adminUsername.equals("anonymousUser")) {
                auditLogService.logAction(
                        adminUsername,
                        "ERROR",
                        "SYSTEM",
                        null,
                        message + ": " + e.getMessage()
                );
            }
        } catch (Exception ex) {
            // Ignore if we can't log the error
        }
    }
}
