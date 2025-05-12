package com.cloudkitchen.inventoryservice.service;

import com.cloudkitchen.inventoryservice.dto.InventoryItemDTO;

import java.util.List;

public interface InventoryService {
    
    InventoryItemDTO addInventoryItem(InventoryItemDTO inventoryItemDTO);
    
    InventoryItemDTO getInventoryItemById(Long id);
    
    InventoryItemDTO getInventoryItemByName(String name);
    
    List<InventoryItemDTO> getAllInventoryItems();
    
    List<InventoryItemDTO> getItemsToReorder();
    
    InventoryItemDTO updateInventoryItem(Long id, InventoryItemDTO inventoryItemDTO);
    
    InventoryItemDTO updateInventoryItemQuantity(Long id, Integer quantity);
    
    void deleteInventoryItem(Long id);
    
    String generateQRCode(Long id);
}
