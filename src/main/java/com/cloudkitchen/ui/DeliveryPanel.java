package com.cloudkitchen.ui;

import com.cloudkitchen.model.Delivery;
import com.cloudkitchen.model.Order;
import com.cloudkitchen.service.DeliveryService;
import com.cloudkitchen.service.OrderService;
import com.cloudkitchen.service.impl.DeliveryServiceImpl;
import com.cloudkitchen.service.impl.OrderServiceImpl;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

/**
 * Panel for delivery management
 */
public class DeliveryPanel extends JPanel {
    
    private final DeliveryService deliveryService;
    private final OrderService orderService;
    
    private JTable deliveryTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> statusComboBox;
    private JTextField deliveryPersonField;
    private JButton createDeliveryButton;
    private JButton assignButton;
    private JButton updateStatusButton;
    private JButton markDeliveredButton;
    private JButton refreshButton;
    
    public DeliveryPanel() {
        this.deliveryService = new DeliveryServiceImpl();
        this.orderService = new OrderServiceImpl();
        initializeUI();
        loadDeliveries();
    }
    
    /**
     * Initialize the UI components
     */
    private void initializeUI() {
        setLayout(new BorderLayout());
        
        // Create the table model with column names
        String[] columnNames = {"ID", "Order ID", "Delivery Person", "Status", "Delivery Time", "Address"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make the table non-editable
            }
        };
        
        // Create the table
        deliveryTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(deliveryTable);
        add(scrollPane, BorderLayout.CENTER);
        
        // Create the control panel
        JPanel controlPanel = new JPanel(new BorderLayout());
        
        // Create the status panel
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.add(new JLabel("Status:"));
        
        statusComboBox = new JComboBox<>(new String[]{
            Delivery.STATUS_PENDING,
            Delivery.STATUS_ASSIGNED,
            Delivery.STATUS_IN_TRANSIT,
            Delivery.STATUS_DELIVERED,
            Delivery.STATUS_FAILED
        });
        statusPanel.add(statusComboBox);
        
        updateStatusButton = new JButton("Update Status");
        statusPanel.add(updateStatusButton);
        
        statusPanel.add(new JLabel("Delivery Person:"));
        deliveryPersonField = new JTextField(15);
        statusPanel.add(deliveryPersonField);
        
        assignButton = new JButton("Assign");
        statusPanel.add(assignButton);
        
        controlPanel.add(statusPanel, BorderLayout.NORTH);
        
        // Create the button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        
        createDeliveryButton = new JButton("Create Delivery");
        markDeliveredButton = new JButton("Mark as Delivered");
        refreshButton = new JButton("Refresh");
        
        buttonPanel.add(createDeliveryButton);
        buttonPanel.add(markDeliveredButton);
        buttonPanel.add(refreshButton);
        
        controlPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(controlPanel, BorderLayout.SOUTH);
        
        // Add action listeners
        createDeliveryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createDelivery();
            }
        });
        
        assignButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                assignDeliveryPerson();
            }
        });
        
        updateStatusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateDeliveryStatus();
            }
        });
        
        markDeliveredButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                markAsDelivered();
            }
        });
        
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadDeliveries();
            }
        });
    }
    
    /**
     * Load deliveries from the database and display them in the table
     */
    private void loadDeliveries() {
        // Clear the table
        tableModel.setRowCount(0);
        
        // Get all deliveries
        List<Delivery> deliveries = deliveryService.getAllDeliveries();
        
        // Add deliveries to the table
        for (Delivery delivery : deliveries) {
            Object[] row = {
                delivery.getId(),
                delivery.getOrderId(),
                delivery.getDeliveryPerson(),
                delivery.getDeliveryStatus(),
                delivery.getDeliveryTime(),
                delivery.getDeliveryAddress()
            };
            tableModel.addRow(row);
        }
    }
    
    /**
     * Create a new delivery
     */
    private void createDelivery() {
        // Get the order ID
        String orderIdStr = JOptionPane.showInputDialog(this, "Enter order ID:", "Create Delivery", JOptionPane.QUESTION_MESSAGE);
        
        if (orderIdStr == null || orderIdStr.isEmpty()) {
            return;
        }
        
        try {
            int orderId = Integer.parseInt(orderIdStr);
            
            // Check if the order exists
            Optional<Order> orderOpt = orderService.getOrderById(orderId);
            
            if (orderOpt.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Order not found", 
                                             "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Get the delivery address
            String deliveryAddress = JOptionPane.showInputDialog(this, "Enter delivery address:", "Create Delivery", JOptionPane.QUESTION_MESSAGE);
            
            if (deliveryAddress == null || deliveryAddress.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Delivery address is required", 
                                             "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Create the delivery
            Delivery delivery = deliveryService.createDelivery(orderId, deliveryAddress);
            
            if (delivery != null) {
                JOptionPane.showMessageDialog(this, "Delivery created successfully", 
                                             "Success", JOptionPane.INFORMATION_MESSAGE);
                
                // Reload the deliveries
                loadDeliveries();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to create delivery", 
                                             "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid order ID", 
                                         "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), 
                                         "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Assign a delivery person to a delivery
     */
    private void assignDeliveryPerson() {
        int selectedRow = deliveryTable.getSelectedRow();
        
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a delivery", 
                                         "Selection Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int deliveryId = (int) tableModel.getValueAt(selectedRow, 0);
        String deliveryPerson = deliveryPersonField.getText().trim();
        
        if (deliveryPerson.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a delivery person", 
                                         "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            // Assign the delivery person
            Delivery updatedDelivery = deliveryService.assignDeliveryPerson(deliveryId, deliveryPerson);
            
            if (updatedDelivery != null) {
                JOptionPane.showMessageDialog(this, "Delivery person assigned successfully", 
                                             "Success", JOptionPane.INFORMATION_MESSAGE);
                
                // Reload the deliveries
                loadDeliveries();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to assign delivery person", 
                                             "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), 
                                         "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Update the status of a delivery
     */
    private void updateDeliveryStatus() {
        int selectedRow = deliveryTable.getSelectedRow();
        
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a delivery", 
                                         "Selection Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int deliveryId = (int) tableModel.getValueAt(selectedRow, 0);
        String status = (String) statusComboBox.getSelectedItem();
        
        try {
            // Update the delivery status
            Delivery updatedDelivery = deliveryService.updateDeliveryStatus(deliveryId, status);
            
            if (updatedDelivery != null) {
                JOptionPane.showMessageDialog(this, "Delivery status updated successfully", 
                                             "Success", JOptionPane.INFORMATION_MESSAGE);
                
                // Reload the deliveries
                loadDeliveries();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update delivery status", 
                                             "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), 
                                         "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Mark a delivery as delivered
     */
    private void markAsDelivered() {
        int selectedRow = deliveryTable.getSelectedRow();
        
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a delivery", 
                                         "Selection Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int deliveryId = (int) tableModel.getValueAt(selectedRow, 0);
        
        try {
            // Mark the delivery as delivered
            Delivery updatedDelivery = deliveryService.markAsDelivered(deliveryId, LocalDateTime.now());
            
            if (updatedDelivery != null) {
                JOptionPane.showMessageDialog(this, "Delivery marked as delivered", 
                                             "Success", JOptionPane.INFORMATION_MESSAGE);
                
                // Reload the deliveries
                loadDeliveries();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to mark delivery as delivered", 
                                             "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), 
                                         "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
