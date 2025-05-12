package com.cloudkitchen.service.impl;

import com.cloudkitchen.model.InventoryItem;
import com.cloudkitchen.repository.InventoryRepository;
import com.cloudkitchen.repository.impl.InventoryRepositoryImpl;
import com.cloudkitchen.service.InventoryService;
import com.cloudkitchen.util.QRCodeGenerator;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of InventoryService
 */
public class InventoryServiceImpl implements InventoryService {
    
    private final InventoryRepository inventoryRepository;
    
    public InventoryServiceImpl() {
        this.inventoryRepository = new InventoryRepositoryImpl();
    }
    
    @Override
    public InventoryItem addInventoryItem(InventoryItem item) {
        // Check if an item with the same name already exists
        Optional<InventoryItem> existingItem = inventoryRepository.findByName(item.getName());
        
        if (existingItem.isPresent()) {
            throw new IllegalArgumentException("An inventory item with this name already exists");
        }
        
        // Save the item first to get an ID
        InventoryItem savedItem = inventoryRepository.save(item);
        
        if (savedItem != null) {
            // Generate QR code
            String qrCodePath = generateQRCode(savedItem);
            
            // Update the item with the QR code path
            savedItem.setQrCodePath(qrCodePath);
            inventoryRepository.update(savedItem);
        }
        
        return savedItem;
    }
    
    @Override
    public Optional<InventoryItem> getInventoryItemById(int id) {
        return inventoryRepository.findById(id);
    }
    
    @Override
    public Optional<InventoryItem> getInventoryItemByName(String name) {
        return inventoryRepository.findByName(name);
    }
    
    @Override
    public List<InventoryItem> getAllInventoryItems() {
        return inventoryRepository.findAll();
    }
    
    @Override
    public List<InventoryItem> getItemsToReorder() {
        return inventoryRepository.findItemsToReorder();
    }
    
    @Override
    public InventoryItem updateInventoryItem(InventoryItem item) {
        // Check if the item exists
        Optional<InventoryItem> existingItem = inventoryRepository.findById(item.getId());
        
        if (existingItem.isEmpty()) {
            throw new IllegalArgumentException("Inventory item not found");
        }
        
        // Check if the name is changed and if it conflicts with another item
        if (!existingItem.get().getName().equals(item.getName())) {
            Optional<InventoryItem> itemWithSameName = inventoryRepository.findByName(item.getName());
            
            if (itemWithSameName.isPresent() && itemWithSameName.get().getId() != item.getId()) {
                throw new IllegalArgumentException("An inventory item with this name already exists");
            }
        }
        
        // If the QR code path is null or empty, generate a new one
        if (item.getQrCodePath() == null || item.getQrCodePath().isEmpty()) {
            String qrCodePath = generateQRCode(item);
            item.setQrCodePath(qrCodePath);
        }
        
        return inventoryRepository.update(item);
    }
    
    @Override
    public InventoryItem updateInventoryItemQuantity(int id, int quantity) {
        Optional<InventoryItem> itemOpt = inventoryRepository.findById(id);
        
        if (itemOpt.isEmpty()) {
            throw new IllegalArgumentException("Inventory item not found");
        }
        
        InventoryItem item = itemOpt.get();
        item.setQuantity(quantity);
        
        return inventoryRepository.update(item);
    }
    
    @Override
    public boolean deleteInventoryItem(int id) {
        return inventoryRepository.deleteById(id);
    }
    
    @Override
    public String generateQRCode(InventoryItem item) {
        return QRCodeGenerator.generateQRCodeForInventoryItem(item.getId(), item.getName());
    }
}
