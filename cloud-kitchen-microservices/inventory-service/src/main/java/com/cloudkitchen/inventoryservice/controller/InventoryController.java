package com.cloudkitchen.inventoryservice.controller;

import com.cloudkitchen.inventoryservice.dto.InventoryItemDTO;
import com.cloudkitchen.inventoryservice.dto.UpdateQuantityRequest;
import com.cloudkitchen.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {
    
    private final InventoryService inventoryService;
    
    @PostMapping
    public ResponseEntity<InventoryItemDTO> addInventoryItem(@Valid @RequestBody InventoryItemDTO inventoryItemDTO) {
        InventoryItemDTO createdItem = inventoryService.addInventoryItem(inventoryItemDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdItem);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<InventoryItemDTO> getInventoryItemById(@PathVariable Long id) {
        InventoryItemDTO inventoryItem = inventoryService.getInventoryItemById(id);
        return ResponseEntity.ok(inventoryItem);
    }
    
    @GetMapping("/name/{name}")
    public ResponseEntity<InventoryItemDTO> getInventoryItemByName(@PathVariable String name) {
        InventoryItemDTO inventoryItem = inventoryService.getInventoryItemByName(name);
        return ResponseEntity.ok(inventoryItem);
    }
    
    @GetMapping
    public ResponseEntity<List<InventoryItemDTO>> getAllInventoryItems() {
        List<InventoryItemDTO> inventoryItems = inventoryService.getAllInventoryItems();
        return ResponseEntity.ok(inventoryItems);
    }
    
    @GetMapping("/reorder")
    public ResponseEntity<List<InventoryItemDTO>> getItemsToReorder() {
        List<InventoryItemDTO> inventoryItems = inventoryService.getItemsToReorder();
        return ResponseEntity.ok(inventoryItems);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<InventoryItemDTO> updateInventoryItem(@PathVariable Long id, @Valid @RequestBody InventoryItemDTO inventoryItemDTO) {
        InventoryItemDTO updatedItem = inventoryService.updateInventoryItem(id, inventoryItemDTO);
        return ResponseEntity.ok(updatedItem);
    }
    
    @PatchMapping("/{id}/quantity")
    public ResponseEntity<InventoryItemDTO> updateInventoryItemQuantity(@PathVariable Long id, @Valid @RequestBody UpdateQuantityRequest request) {
        InventoryItemDTO updatedItem = inventoryService.updateInventoryItemQuantity(id, request.getQuantity());
        return ResponseEntity.ok(updatedItem);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInventoryItem(@PathVariable Long id) {
        inventoryService.deleteInventoryItem(id);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/{id}/qrcode")
    public ResponseEntity<String> generateQRCode(@PathVariable Long id) {
        String qrCodePath = inventoryService.generateQRCode(id);
        return ResponseEntity.ok(qrCodePath);
    }
}
