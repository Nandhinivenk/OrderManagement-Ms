package com.cloudkitchen.inventoryservice.service.impl;

import com.cloudkitchen.inventoryservice.dto.InventoryItemDTO;
import com.cloudkitchen.inventoryservice.exception.InventoryItemAlreadyExistsException;
import com.cloudkitchen.inventoryservice.exception.InventoryItemNotFoundException;
import com.cloudkitchen.inventoryservice.exception.QRCodeGenerationException;
import com.cloudkitchen.inventoryservice.model.InventoryItem;
import com.cloudkitchen.inventoryservice.repository.InventoryRepository;
import com.cloudkitchen.inventoryservice.service.InventoryService;
import com.cloudkitchen.inventoryservice.util.QRCodeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {
    
    private final InventoryRepository inventoryRepository;
    private final QRCodeGenerator qrCodeGenerator;
    
    @Override
    @Transactional
    public InventoryItemDTO addInventoryItem(InventoryItemDTO inventoryItemDTO) {
        // Check if item with the same name already exists
        if (inventoryRepository.existsByName(inventoryItemDTO.getName())) {
            throw new InventoryItemAlreadyExistsException("Inventory item with name " + inventoryItemDTO.getName() + " already exists");
        }
        
        // Create new inventory item
        InventoryItem inventoryItem = mapToEntity(inventoryItemDTO);
        
        // Save inventory item
        InventoryItem savedItem = inventoryRepository.save(inventoryItem);
        
        // Generate QR code
        try {
            String qrCodePath = qrCodeGenerator.generateQRCodeForInventoryItem(savedItem.getId(), savedItem.getName());
            savedItem.setQrCodePath(qrCodePath);
            savedItem = inventoryRepository.save(savedItem);
        } catch (Exception e) {
            throw new QRCodeGenerationException("Failed to generate QR code for inventory item", e);
        }
        
        // Return DTO
        return mapToDTO(savedItem);
    }
    
    @Override
    public InventoryItemDTO getInventoryItemById(Long id) {
        InventoryItem inventoryItem = inventoryRepository.findById(id)
                .orElseThrow(() -> new InventoryItemNotFoundException("Inventory item not found with id: " + id));
        
        return mapToDTO(inventoryItem);
    }
    
    @Override
    public InventoryItemDTO getInventoryItemByName(String name) {
        InventoryItem inventoryItem = inventoryRepository.findByName(name)
                .orElseThrow(() -> new InventoryItemNotFoundException("Inventory item not found with name: " + name));
        
        return mapToDTO(inventoryItem);
    }
    
    @Override
    public List<InventoryItemDTO> getAllInventoryItems() {
        return inventoryRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<InventoryItemDTO> getItemsToReorder() {
        return inventoryRepository.findItemsToReorder().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public InventoryItemDTO updateInventoryItem(Long id, InventoryItemDTO inventoryItemDTO) {
        // Check if inventory item exists
        InventoryItem inventoryItem = inventoryRepository.findById(id)
                .orElseThrow(() -> new InventoryItemNotFoundException("Inventory item not found with id: " + id));
        
        // Check if name is being changed and if it conflicts with another item
        if (!inventoryItem.getName().equals(inventoryItemDTO.getName()) && 
                inventoryRepository.existsByName(inventoryItemDTO.getName())) {
            throw new InventoryItemAlreadyExistsException("Inventory item with name " + inventoryItemDTO.getName() + " already exists");
        }
        
        // Update inventory item
        inventoryItem.setName(inventoryItemDTO.getName());
        inventoryItem.setQuantity(inventoryItemDTO.getQuantity());
        inventoryItem.setUnit(inventoryItemDTO.getUnit());
        inventoryItem.setReorderLevel(inventoryItemDTO.getReorderLevel());
        
        // Save inventory item
        InventoryItem updatedItem = inventoryRepository.save(inventoryItem);
        
        // Return DTO
        return mapToDTO(updatedItem);
    }
    
    @Override
    @Transactional
    public InventoryItemDTO updateInventoryItemQuantity(Long id, Integer quantity) {
        // Check if inventory item exists
        InventoryItem inventoryItem = inventoryRepository.findById(id)
                .orElseThrow(() -> new InventoryItemNotFoundException("Inventory item not found with id: " + id));
        
        // Update quantity
        inventoryItem.setQuantity(quantity);
        
        // Save inventory item
        InventoryItem updatedItem = inventoryRepository.save(inventoryItem);
        
        // Return DTO
        return mapToDTO(updatedItem);
    }
    
    @Override
    @Transactional
    public void deleteInventoryItem(Long id) {
        // Check if inventory item exists
        if (!inventoryRepository.existsById(id)) {
            throw new InventoryItemNotFoundException("Inventory item not found with id: " + id);
        }
        
        // Delete inventory item
        inventoryRepository.deleteById(id);
    }
    
    @Override
    @Transactional
    public String generateQRCode(Long id) {
        // Check if inventory item exists
        InventoryItem inventoryItem = inventoryRepository.findById(id)
                .orElseThrow(() -> new InventoryItemNotFoundException("Inventory item not found with id: " + id));
        
        // Generate QR code
        try {
            String qrCodePath = qrCodeGenerator.generateQRCodeForInventoryItem(inventoryItem.getId(), inventoryItem.getName());
            inventoryItem.setQrCodePath(qrCodePath);
            inventoryRepository.save(inventoryItem);
            return qrCodePath;
        } catch (Exception e) {
            throw new QRCodeGenerationException("Failed to generate QR code for inventory item", e);
        }
    }
    
    private InventoryItemDTO mapToDTO(InventoryItem inventoryItem) {
        InventoryItemDTO dto = new InventoryItemDTO();
        dto.setId(inventoryItem.getId());
        dto.setName(inventoryItem.getName());
        dto.setQuantity(inventoryItem.getQuantity());
        dto.setUnit(inventoryItem.getUnit());
        dto.setReorderLevel(inventoryItem.getReorderLevel());
        dto.setQrCodePath(inventoryItem.getQrCodePath());
        dto.setLastUpdated(inventoryItem.getLastUpdated());
        return dto;
    }
    
    private InventoryItem mapToEntity(InventoryItemDTO dto) {
        InventoryItem inventoryItem = new InventoryItem();
        inventoryItem.setName(dto.getName());
        inventoryItem.setQuantity(dto.getQuantity());
        inventoryItem.setUnit(dto.getUnit());
        inventoryItem.setReorderLevel(dto.getReorderLevel() != null ? dto.getReorderLevel() : 0);
        return inventoryItem;
    }
}
