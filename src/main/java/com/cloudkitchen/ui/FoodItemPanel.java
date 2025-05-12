package com.cloudkitchen.ui;

import com.cloudkitchen.model.FoodItem;
import com.cloudkitchen.service.FoodItemService;
import com.cloudkitchen.service.impl.FoodItemServiceImpl;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Panel for food item management
 */
public class FoodItemPanel extends JPanel {
    
    private final FoodItemService foodItemService;
    
    private JTable foodItemTable;
    private DefaultTableModel tableModel;
    private JTextField nameField;
    private JTextField descriptionField;
    private JTextField priceField;
    private JTextField categoryField;
    private JCheckBox availableCheckBox;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton refreshButton;
    
    public FoodItemPanel() {
        this.foodItemService = new FoodItemServiceImpl();
        initializeUI();
        loadFoodItems();
    }
    
    /**
     * Initialize the UI components
     */
    private void initializeUI() {
        setLayout(new BorderLayout());
        
        // Create the table model with column names
        String[] columnNames = {"ID", "Name", "Description", "Price", "Category", "Available"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make the table non-editable
            }
        };
        
        // Create the table
        foodItemTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(foodItemTable);
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
        formPanel.add(new JLabel("Description:"), gbc);
        
        gbc.gridx = 1;
        descriptionField = new JTextField(15);
        formPanel.add(descriptionField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Price:"), gbc);
        
        gbc.gridx = 1;
        priceField = new JTextField(15);
        formPanel.add(priceField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Category:"), gbc);
        
        gbc.gridx = 1;
        categoryField = new JTextField(15);
        formPanel.add(categoryField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Available:"), gbc);
        
        gbc.gridx = 1;
        availableCheckBox = new JCheckBox();
        availableCheckBox.setSelected(true);
        formPanel.add(availableCheckBox, gbc);
        
        // Create the button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        
        addButton = new JButton("Add");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");
        refreshButton = new JButton("Refresh");
        
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);
        
        add(formPanel, BorderLayout.SOUTH);
        
        // Add action listeners
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addFoodItem();
            }
        });
        
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateFoodItem();
            }
        });
        
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteFoodItem();
            }
        });
        
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadFoodItems();
            }
        });
        
        // Add table selection listener
        foodItemTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = foodItemTable.getSelectedRow();
                if (selectedRow >= 0) {
                    populateFormFields(selectedRow);
                }
            }
        });
    }
    
    /**
     * Load food items from the database and display them in the table
     */
    private void loadFoodItems() {
        // Clear the table
        tableModel.setRowCount(0);
        
        // Get all food items
        List<FoodItem> items = foodItemService.getAllFoodItems();
        
        // Add items to the table
        for (FoodItem item : items) {
            Object[] row = {
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getPrice(),
                item.getCategory(),
                item.isAvailable()
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
        descriptionField.setText(tableModel.getValueAt(selectedRow, 2).toString());
        priceField.setText(tableModel.getValueAt(selectedRow, 3).toString());
        categoryField.setText(tableModel.getValueAt(selectedRow, 4).toString());
        availableCheckBox.setSelected((Boolean) tableModel.getValueAt(selectedRow, 5));
    }
    
    /**
     * Add a new food item
     */
    private void addFoodItem() {
        try {
            // Validate input
            String name = nameField.getText().trim();
            String description = descriptionField.getText().trim();
            String priceStr = priceField.getText().trim();
            String category = categoryField.getText().trim();
            boolean isAvailable = availableCheckBox.isSelected();
            
            if (name.isEmpty() || priceStr.isEmpty() || category.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all required fields", 
                                             "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            BigDecimal price = new BigDecimal(priceStr);
            
            // Create a new food item
            FoodItem item = new FoodItem(name, price);
            item.setDescription(description);
            item.setCategory(category);
            item.setAvailable(isAvailable);
            
            // Add the item
            FoodItem addedItem = foodItemService.addFoodItem(item);
            
            if (addedItem != null) {
                JOptionPane.showMessageDialog(this, "Food item added successfully", 
                                             "Success", JOptionPane.INFORMATION_MESSAGE);
                
                // Clear the form fields
                clearFormFields();
                
                // Reload the food items
                loadFoodItems();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add food item", 
                                             "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Price must be a valid number", 
                                         "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), 
                                         "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Update an existing food item
     */
    private void updateFoodItem() {
        int selectedRow = foodItemTable.getSelectedRow();
        
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
            String description = descriptionField.getText().trim();
            String priceStr = priceField.getText().trim();
            String category = categoryField.getText().trim();
            boolean isAvailable = availableCheckBox.isSelected();
            
            if (name.isEmpty() || priceStr.isEmpty() || category.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all required fields", 
                                             "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            BigDecimal price = new BigDecimal(priceStr);
            
            // Get the existing item
            Optional<FoodItem> itemOpt = foodItemService.getFoodItemById(id);
            
            if (itemOpt.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Food item not found", 
                                             "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            FoodItem item = itemOpt.get();
            item.setName(name);
            item.setDescription(description);
            item.setPrice(price);
            item.setCategory(category);
            item.setAvailable(isAvailable);
            
            // Update the item
            FoodItem updatedItem = foodItemService.updateFoodItem(item);
            
            if (updatedItem != null) {
                JOptionPane.showMessageDialog(this, "Food item updated successfully", 
                                             "Success", JOptionPane.INFORMATION_MESSAGE);
                
                // Reload the food items
                loadFoodItems();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update food item", 
                                             "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Price must be a valid number", 
                                         "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), 
                                         "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Delete a food item
     */
    private void deleteFoodItem() {
        int selectedRow = foodItemTable.getSelectedRow();
        
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
                                                  "Are you sure you want to delete the food item: " + name + "?", 
                                                  "Confirm Deletion", 
                                                  JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            boolean deleted = foodItemService.deleteFoodItem(id);
            
            if (deleted) {
                JOptionPane.showMessageDialog(this, "Food item deleted successfully", 
                                             "Success", JOptionPane.INFORMATION_MESSAGE);
                
                // Clear the form fields
                clearFormFields();
                
                // Reload the food items
                loadFoodItems();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete food item", 
                                             "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Clear the form fields
     */
    private void clearFormFields() {
        nameField.setText("");
        descriptionField.setText("");
        priceField.setText("");
        categoryField.setText("");
        availableCheckBox.setSelected(true);
        foodItemTable.clearSelection();
    }
}
