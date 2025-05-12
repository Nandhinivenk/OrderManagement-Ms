package com.cloudkitchen.ui;

import com.cloudkitchen.model.FoodItem;
import com.cloudkitchen.model.FoodItemMapping;
import com.cloudkitchen.service.FoodItemMappingService;
import com.cloudkitchen.service.FoodItemService;
import com.cloudkitchen.service.impl.FoodItemMappingServiceImpl;
import com.cloudkitchen.service.impl.FoodItemServiceImpl;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Optional;

/**
 * Panel for food item mapping management
 */
public class FoodItemMappingPanel extends JPanel {
    
    private final FoodItemMappingService foodItemMappingService;
    private final FoodItemService foodItemService;
    
    private JTable mappingTable;
    private DefaultTableModel tableModel;
    private JTextField foodItemIdField;
    private JTextField categoryIdField;
    private JButton createButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton refreshButton;
    
    public FoodItemMappingPanel() {
        this.foodItemMappingService = new FoodItemMappingServiceImpl();
        this.foodItemService = new FoodItemServiceImpl();
        initializeUI();
        loadMappings();
    }
    
    /**
     * Initialize the UI components
     */
    private void initializeUI() {
        setLayout(new BorderLayout());
        
        // Create the table model with column names
        String[] columnNames = {"ID", "Food Item ID", "Food Item Name", "Category ID"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make the table non-editable
            }
        };
        
        // Create the table
        mappingTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(mappingTable);
        add(scrollPane, BorderLayout.CENTER);
        
        // Create the form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Add form components
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Food Item ID:"), gbc);
        
        gbc.gridx = 1;
        foodItemIdField = new JTextField(15);
        formPanel.add(foodItemIdField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Category ID:"), gbc);
        
        gbc.gridx = 1;
        categoryIdField = new JTextField(15);
        formPanel.add(categoryIdField, gbc);
        
        // Create the button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        
        createButton = new JButton("Create");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");
        refreshButton = new JButton("Refresh");
        
        buttonPanel.add(createButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);
        
        add(formPanel, BorderLayout.SOUTH);
        
        // Add action listeners
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createMapping();
            }
        });
        
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateMapping();
            }
        });
        
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteMapping();
            }
        });
        
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadMappings();
            }
        });
        
        // Add table selection listener
        mappingTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = mappingTable.getSelectedRow();
                if (selectedRow >= 0) {
                    populateFormFields(selectedRow);
                }
            }
        });
    }
    
    /**
     * Load mappings from the database and display them in the table
     */
    private void loadMappings() {
        // Clear the table
        tableModel.setRowCount(0);
        
        // Get all mappings
        List<FoodItemMapping> mappings = foodItemMappingService.getAllMappings();
        
        // Add mappings to the table
        for (FoodItemMapping mapping : mappings) {
            // Get the food item name
            String foodItemName = "Unknown";
            Optional<FoodItem> foodItemOpt = foodItemService.getFoodItemById(mapping.getFoodItemId());
            if (foodItemOpt.isPresent()) {
                foodItemName = foodItemOpt.get().getName();
            }
            
            Object[] row = {
                mapping.getId(),
                mapping.getFoodItemId(),
                foodItemName,
                mapping.getCategoryId()
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
        foodItemIdField.setText(tableModel.getValueAt(selectedRow, 1).toString());
        categoryIdField.setText(tableModel.getValueAt(selectedRow, 3).toString());
    }
    
    /**
     * Create a new food item mapping
     */
    private void createMapping() {
        try {
            // Validate input
            String foodItemIdStr = foodItemIdField.getText().trim();
            String categoryIdStr = categoryIdField.getText().trim();
            
            if (foodItemIdStr.isEmpty() || categoryIdStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields", 
                                             "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            int foodItemId = Integer.parseInt(foodItemIdStr);
            int categoryId = Integer.parseInt(categoryIdStr);
            
            // Create the mapping
            FoodItemMapping mapping = foodItemMappingService.createMapping(foodItemId, categoryId);
            
            if (mapping != null) {
                JOptionPane.showMessageDialog(this, "Food item mapping created successfully", 
                                             "Success", JOptionPane.INFORMATION_MESSAGE);
                
                // Clear the form fields
                clearFormFields();
                
                // Reload the mappings
                loadMappings();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to create food item mapping", 
                                             "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Food Item ID and Category ID must be numbers", 
                                         "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), 
                                         "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Update an existing food item mapping
     */
    private void updateMapping() {
        int selectedRow = mappingTable.getSelectedRow();
        
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a mapping to update", 
                                         "Selection Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            // Get the mapping ID
            int mappingId = (int) tableModel.getValueAt(selectedRow, 0);
            
            // Validate input
            String foodItemIdStr = foodItemIdField.getText().trim();
            String categoryIdStr = categoryIdField.getText().trim();
            
            if (foodItemIdStr.isEmpty() || categoryIdStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields", 
                                             "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            int foodItemId = Integer.parseInt(foodItemIdStr);
            int categoryId = Integer.parseInt(categoryIdStr);
            
            // Update the mapping
            FoodItemMapping updatedMapping = foodItemMappingService.updateMapping(mappingId, foodItemId, categoryId);
            
            if (updatedMapping != null) {
                JOptionPane.showMessageDialog(this, "Food item mapping updated successfully", 
                                             "Success", JOptionPane.INFORMATION_MESSAGE);
                
                // Reload the mappings
                loadMappings();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update food item mapping", 
                                             "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Food Item ID and Category ID must be numbers", 
                                         "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), 
                                         "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Delete a food item mapping
     */
    private void deleteMapping() {
        int selectedRow = mappingTable.getSelectedRow();
        
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a mapping to delete", 
                                         "Selection Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Get the mapping ID
        int mappingId = (int) tableModel.getValueAt(selectedRow, 0);
        
        // Confirm deletion
        int confirm = JOptionPane.showConfirmDialog(this, 
                                                  "Are you sure you want to delete this mapping?", 
                                                  "Confirm Deletion", 
                                                  JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            boolean deleted = foodItemMappingService.deleteMapping(mappingId);
            
            if (deleted) {
                JOptionPane.showMessageDialog(this, "Food item mapping deleted successfully", 
                                             "Success", JOptionPane.INFORMATION_MESSAGE);
                
                // Clear the form fields
                clearFormFields();
                
                // Reload the mappings
                loadMappings();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete food item mapping", 
                                             "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Clear the form fields
     */
    private void clearFormFields() {
        foodItemIdField.setText("");
        categoryIdField.setText("");
        mappingTable.clearSelection();
    }
}
