package com.cloudkitchen.ui;

import com.cloudkitchen.model.InventoryItem;
import com.cloudkitchen.service.InventoryService;
import com.cloudkitchen.service.impl.InventoryServiceImpl;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Optional;

/**
 * Panel for inventory management
 */
public class InventoryPanel extends JPanel {
    
    private final InventoryService inventoryService;
    
    private JTable inventoryTable;
    private DefaultTableModel tableModel;
    private JTextField nameField;
    private JTextField quantityField;
    private JTextField unitField;
    private JTextField reorderLevelField;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton generateQRButton;
    private JButton refreshButton;
    
    public InventoryPanel() {
        this.inventoryService = new InventoryServiceImpl();
        initializeUI();
        loadInventoryItems();
    }
    
    /**
     * Initialize the UI components
     */
    private void initializeUI() {
        setLayout(new BorderLayout());
        
        // Create the table model with column names
        String[] columnNames = {"ID", "Name", "Quantity", "Unit", "Reorder Level", "Last Updated", "QR Code"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make the table non-editable
            }
        };
        
        // Create the table
        inventoryTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(inventoryTable);
        add(scrollPane, BorderLayout.CENTER);
        
        // Create the form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Add form components
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Name:"), gbc);
        
        gbc.gridx = 1;
        nameField = new JTextField(15);
        formPanel.add(nameField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Quantity:"), gbc);
        
        gbc.gridx = 1;
        quantityField = new JTextField(15);
        formPanel.add(quantityField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Unit:"), gbc);
        
        gbc.gridx = 1;
        unitField = new JTextField(15);
        formPanel.add(unitField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Reorder Level:"), gbc);
        
        gbc.gridx = 1;
        reorderLevelField = new JTextField(15);
        formPanel.add(reorderLevelField, gbc);
        
        // Create the button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        
        addButton = new JButton("Add");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");
        generateQRButton = new JButton("Generate QR");
        refreshButton = new JButton("Refresh");
        
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(generateQRButton);
        buttonPanel.add(refreshButton);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);
        
        add(formPanel, BorderLayout.SOUTH);
        
        // Add action listeners
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addInventoryItem();
            }
        });
        
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateInventoryItem();
            }
        });
        
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteInventoryItem();
            }
        });
        
        generateQRButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateQRCode();
            }
        });
        
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadInventoryItems();
            }
        });
        
        // Add table selection listener
        inventoryTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = inventoryTable.getSelectedRow();
                if (selectedRow >= 0) {
                    populateFormFields(selectedRow);
                }
            }
        });
    }
    
    /**
     * Load inventory items from the database and display them in the table
     */
    private void loadInventoryItems() {
        // Clear the table
        tableModel.setRowCount(0);
        
        // Get all inventory items
        List<InventoryItem> items = inventoryService.getAllInventoryItems();
        
        // Add items to the table
        for (InventoryItem item : items) {
            Object[] row = {
                item.getId(),
                item.getName(),
                item.getQuantity(),
                item.getUnit(),
                item.getReorderLevel(),
                item.getLastUpdated(),
                item.getQrCodePath() != null ? "Available" : "Not Available"
            };
            tableModel.addRow(row);
        }
    }
    
    /**
     * Populate the form fields with data from the selected row
     * 
     * @param selectedRow The selected row index
     */
    private void populateFormFields(int selectedRow) {
        nameField.setText(tableModel.getValueAt(selectedRow, 1).toString());
        quantityField.setText(tableModel.getValueAt(selectedRow, 2).toString());
        unitField.setText(tableModel.getValueAt(selectedRow, 3).toString());
        reorderLevelField.setText(tableModel.getValueAt(selectedRow, 4).toString());
    }
    
    /**
     * Add a new inventory item
     */
    private void addInventoryItem() {
        try {
            // Validate input
            String name = nameField.getText().trim();
            String quantityStr = quantityField.getText().trim();
            String unit = unitField.getText().trim();
            String reorderLevelStr = reorderLevelField.getText().trim();
            
            if (name.isEmpty() || quantityStr.isEmpty() || unit.isEmpty() || reorderLevelStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields", 
                                             "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            int quantity = Integer.parseInt(quantityStr);
            int reorderLevel = Integer.parseInt(reorderLevelStr);
            
            // Create a new inventory item
            InventoryItem item = new InventoryItem(name, quantity, unit);
            item.setReorderLevel(reorderLevel);
            
            // Add the item
            InventoryItem addedItem = inventoryService.addInventoryItem(item);
            
            if (addedItem != null) {
                JOptionPane.showMessageDialog(this, "Inventory item added successfully", 
                                             "Success", JOptionPane.INFORMATION_MESSAGE);
                
                // Clear the form fields
                clearFormFields();
                
                // Reload the inventory items
                loadInventoryItems();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add inventory item", 
                                             "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Quantity and Reorder Level must be numbers", 
                                         "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), 
                                         "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Update an existing inventory item
     */
    private void updateInventoryItem() {
        int selectedRow = inventoryTable.getSelectedRow();
        
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select an item to update", 
                                         "Selection Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            // Get the item ID
            int id = (int) tableModel.getValueAt(selectedRow, 0);
            
            // Validate input
            String name = nameField.getText().trim();
            String quantityStr = quantityField.getText().trim();
            String unit = unitField.getText().trim();
            String reorderLevelStr = reorderLevelField.getText().trim();
            
            if (name.isEmpty() || quantityStr.isEmpty() || unit.isEmpty() || reorderLevelStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields", 
                                             "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            int quantity = Integer.parseInt(quantityStr);
            int reorderLevel = Integer.parseInt(reorderLevelStr);
            
            // Get the existing item
            Optional<InventoryItem> itemOpt = inventoryService.getInventoryItemById(id);
            
            if (itemOpt.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Inventory item not found", 
                                             "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            InventoryItem item = itemOpt.get();
            item.setName(name);
            item.setQuantity(quantity);
            item.setUnit(unit);
            item.setReorderLevel(reorderLevel);
            
            // Update the item
            InventoryItem updatedItem = inventoryService.updateInventoryItem(item);
            
            if (updatedItem != null) {
                JOptionPane.showMessageDialog(this, "Inventory item updated successfully", 
                                             "Success", JOptionPane.INFORMATION_MESSAGE);
                
                // Reload the inventory items
                loadInventoryItems();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update inventory item", 
                                             "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Quantity and Reorder Level must be numbers", 
                                         "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), 
                                         "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Delete an inventory item
     */
    private void deleteInventoryItem() {
        int selectedRow = inventoryTable.getSelectedRow();
        
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select an item to delete", 
                                         "Selection Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Get the item ID and name
        int id = (int) tableModel.getValueAt(selectedRow, 0);
        String name = (String) tableModel.getValueAt(selectedRow, 1);
        
        // Confirm deletion
        int confirm = JOptionPane.showConfirmDialog(this, 
                                                  "Are you sure you want to delete the item: " + name + "?", 
                                                  "Confirm Deletion", 
                                                  JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            boolean deleted = inventoryService.deleteInventoryItem(id);
            
            if (deleted) {
                JOptionPane.showMessageDialog(this, "Inventory item deleted successfully", 
                                             "Success", JOptionPane.INFORMATION_MESSAGE);
                
                // Clear the form fields
                clearFormFields();
                
                // Reload the inventory items
                loadInventoryItems();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete inventory item", 
                                             "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Generate a QR code for an inventory item
     */
    private void generateQRCode() {
        int selectedRow = inventoryTable.getSelectedRow();
        
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select an item to generate QR code", 
                                         "Selection Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Get the item ID
        int id = (int) tableModel.getValueAt(selectedRow, 0);
        
        // Get the existing item
        Optional<InventoryItem> itemOpt = inventoryService.getInventoryItemById(id);
        
        if (itemOpt.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Inventory item not found", 
                                         "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        InventoryItem item = itemOpt.get();
        
        // Generate QR code
        String qrCodePath = inventoryService.generateQRCode(item);
        
        if (qrCodePath != null) {
            // Update the item with the QR code path
            item.setQrCodePath(qrCodePath);
            inventoryService.updateInventoryItem(item);
            
            JOptionPane.showMessageDialog(this, "QR code generated successfully: " + qrCodePath, 
                                         "Success", JOptionPane.INFORMATION_MESSAGE);
            
            // Reload the inventory items
            loadInventoryItems();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to generate QR code", 
                                         "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Clear the form fields
     */
    private void clearFormFields() {
        nameField.setText("");
        quantityField.setText("");
        unitField.setText("");
        reorderLevelField.setText("");
        inventoryTable.clearSelection();
    }
}
