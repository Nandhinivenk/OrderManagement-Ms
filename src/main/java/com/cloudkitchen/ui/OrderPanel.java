package com.cloudkitchen.ui;

import com.cloudkitchen.model.Customer;
import com.cloudkitchen.model.FoodItem;
import com.cloudkitchen.model.Order;
import com.cloudkitchen.model.OrderItem;
import com.cloudkitchen.service.CustomerService;
import com.cloudkitchen.service.FoodItemService;
import com.cloudkitchen.service.OrderService;
import com.cloudkitchen.service.impl.CustomerServiceImpl;
import com.cloudkitchen.service.impl.FoodItemServiceImpl;
import com.cloudkitchen.service.impl.OrderServiceImpl;
import com.cloudkitchen.util.AuthUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Panel for order management
 */
public class OrderPanel extends JPanel {
    
    private final OrderService orderService;
    private final CustomerService customerService;
    private final FoodItemService foodItemService;
    
    private JTable orderTable;
    private DefaultTableModel orderTableModel;
    private JTable orderItemTable;
    private DefaultTableModel orderItemTableModel;
    private JComboBox<String> statusComboBox;
    private JComboBox<String> paymentStatusComboBox;
    private JComboBox<String> paymentMethodComboBox;
    private JButton createOrderButton;
    private JButton addItemButton;
    private JButton removeItemButton;
    private JButton updateStatusButton;
    private JButton cancelOrderButton;
    private JButton refreshButton;
    
    private Order currentOrder;
    
    public OrderPanel() {
        this.orderService = new OrderServiceImpl();
        this.customerService = new CustomerServiceImpl();
        this.foodItemService = new FoodItemServiceImpl();
        initializeUI();
        loadOrders();
    }
    
    /**
     * Initialize the UI components
     */
    private void initializeUI() {
        setLayout(new BorderLayout());
        
        // Create the split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setResizeWeight(0.5);
        
        // Create the order table panel
        JPanel orderTablePanel = new JPanel(new BorderLayout());
        orderTablePanel.setBorder(BorderFactory.createTitledBorder("Orders"));
        
        // Create the order table model with column names
        String[] orderColumnNames = {"ID", "Customer", "Date", "Status", "Total", "Payment Method", "Payment Status"};
        orderTableModel = new DefaultTableModel(orderColumnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make the table non-editable
            }
        };
        
        // Create the order table
        orderTable = new JTable(orderTableModel);
        JScrollPane orderScrollPane = new JScrollPane(orderTable);
        orderTablePanel.add(orderScrollPane, BorderLayout.CENTER);
        
        // Create the order item table panel
        JPanel orderItemTablePanel = new JPanel(new BorderLayout());
        orderItemTablePanel.setBorder(BorderFactory.createTitledBorder("Order Items"));
        
        // Create the order item table model with column names
        String[] orderItemColumnNames = {"ID", "Food Item", "Quantity", "Price", "Subtotal"};
        orderItemTableModel = new DefaultTableModel(orderItemColumnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make the table non-editable
            }
        };
        
        // Create the order item table
        orderItemTable = new JTable(orderItemTableModel);
        JScrollPane orderItemScrollPane = new JScrollPane(orderItemTable);
        orderItemTablePanel.add(orderItemScrollPane, BorderLayout.CENTER);
        
        // Add the tables to the split pane
        splitPane.setTopComponent(orderTablePanel);
        splitPane.setBottomComponent(orderItemTablePanel);
        
        add(splitPane, BorderLayout.CENTER);
        
        // Create the control panel
        JPanel controlPanel = new JPanel(new BorderLayout());
        
        // Create the status panel
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.add(new JLabel("Status:"));
        
        statusComboBox = new JComboBox<>(new String[]{
            Order.STATUS_PENDING,
            Order.STATUS_PREPARING,
            Order.STATUS_READY,
            Order.STATUS_DELIVERED,
            Order.STATUS_CANCELLED
        });
        statusPanel.add(statusComboBox);
        
        updateStatusButton = new JButton("Update Status");
        statusPanel.add(updateStatusButton);
        
        statusPanel.add(new JLabel("Payment Status:"));
        paymentStatusComboBox = new JComboBox<>(new String[]{
            Order.PAYMENT_PENDING,
            Order.PAYMENT_COMPLETED,
            Order.PAYMENT_FAILED
        });
        statusPanel.add(paymentStatusComboBox);
        
        statusPanel.add(new JLabel("Payment Method:"));
        paymentMethodComboBox = new JComboBox<>(new String[]{
            "CASH",
            "CREDIT_CARD",
            "DEBIT_CARD",
            "ONLINE"
        });
        statusPanel.add(paymentMethodComboBox);
        
        controlPanel.add(statusPanel, BorderLayout.NORTH);
        
        // Create the button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        
        createOrderButton = new JButton("Create Order");
        addItemButton = new JButton("Add Item");
        removeItemButton = new JButton("Remove Item");
        cancelOrderButton = new JButton("Cancel Order");
        refreshButton = new JButton("Refresh");
        
        buttonPanel.add(createOrderButton);
        buttonPanel.add(addItemButton);
        buttonPanel.add(removeItemButton);
        buttonPanel.add(cancelOrderButton);
        buttonPanel.add(refreshButton);
        
        controlPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(controlPanel, BorderLayout.SOUTH);
        
        // Add action listeners
        createOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createOrder();
            }
        });
        
        addItemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addItemToOrder();
            }
        });
        
        removeItemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeItemFromOrder();
            }
        });
        
        updateStatusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateOrderStatus();
            }
        });
        
        cancelOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelOrder();
            }
        });
        
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadOrders();
            }
        });
        
        // Add table selection listener
        orderTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = orderTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int orderId = (int) orderTableModel.getValueAt(selectedRow, 0);
                    loadOrderItems(orderId);
                }
            }
        });
    }
    
    /**
     * Load orders from the database and display them in the table
     */
    private void loadOrders() {
        // Clear the table
        orderTableModel.setRowCount(0);
        
        // Get all orders
        List<Order> orders;
        
        // If a customer is logged in, only show their orders
        if (AuthUtil.isCustomerLoggedIn()) {
            Customer customer = AuthUtil.getCurrentCustomer();
            orders = orderService.getOrdersByCustomerId(customer.getId());
        } else {
            // Otherwise, show all orders (for admin)
            orders = orderService.getAllOrders();
        }
        
        // Add orders to the table
        for (Order order : orders) {
            // Get the customer name
            String customerName = "Unknown";
            Optional<Customer> customerOpt = customerService.getCustomerById(order.getCustomerId());
            if (customerOpt.isPresent()) {
                customerName = customerOpt.get().getName();
            }
            
            Object[] row = {
                order.getId(),
                customerName,
                order.getOrderDate(),
                order.getStatus(),
                order.getTotalAmount(),
                order.getPaymentMethod(),
                order.getPaymentStatus()
            };
            orderTableModel.addRow(row);
        }
        
        // Clear the order item table
        orderItemTableModel.setRowCount(0);
    }
    
    /**
     * Load order items for an order and display them in the table
     * 
     * @param orderId The ID of the order
     */
    private void loadOrderItems(int orderId) {
        // Clear the table
        orderItemTableModel.setRowCount(0);
        
        // Get the order
        Optional<Order> orderOpt = orderService.getOrderById(orderId);
        
        if (orderOpt.isPresent()) {
            currentOrder = orderOpt.get();
            
            // Add order items to the table
            for (OrderItem item : currentOrder.getOrderItems()) {
                // Get the food item name
                String foodItemName = "Unknown";
                if (item.getFoodItem() != null) {
                    foodItemName = item.getFoodItem().getName();
                } else {
                    Optional<FoodItem> foodItemOpt = foodItemService.getFoodItemById(item.getFoodItemId());
                    if (foodItemOpt.isPresent()) {
                        foodItemName = foodItemOpt.get().getName();
                    }
                }
                
                Object[] row = {
                    item.getId(),
                    foodItemName,
                    item.getQuantity(),
                    item.getPrice(),
                    item.getSubtotal()
                };
                orderItemTableModel.addRow(row);
            }
        }
    }
    
    /**
     * Create a new order
     */
    private void createOrder() {
        // Check if a customer is logged in
        if (!AuthUtil.isCustomerLoggedIn() && !AuthUtil.isAdminLoggedIn()) {
            JOptionPane.showMessageDialog(this, "You must be logged in to create an order", 
                                         "Authentication Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // If an admin is creating an order, ask for the customer ID
        int customerId;
        
        if (AuthUtil.isAdminLoggedIn()) {
            String customerIdStr = JOptionPane.showInputDialog(this, "Enter customer ID:", "Create Order", JOptionPane.QUESTION_MESSAGE);
            
            if (customerIdStr == null || customerIdStr.isEmpty()) {
                return;
            }
            
            try {
                customerId = Integer.parseInt(customerIdStr);
                
                // Check if the customer exists
                Optional<Customer> customerOpt = customerService.getCustomerById(customerId);
                
                if (customerOpt.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Customer not found", 
                                                 "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid customer ID", 
                                             "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } else {
            // Use the logged-in customer's ID
            customerId = AuthUtil.getCurrentCustomer().getId();
        }
        
        // Create a new order
        Order order = new Order(customerId);
        
        // Set the payment method
        String paymentMethod = (String) paymentMethodComboBox.getSelectedItem();
        order.setPaymentMethod(paymentMethod);
        
        // Create the order
        Order createdOrder = orderService.createOrder(order);
        
        if (createdOrder != null) {
            JOptionPane.showMessageDialog(this, "Order created successfully", 
                                         "Success", JOptionPane.INFORMATION_MESSAGE);
            
            // Reload the orders
            loadOrders();
            
            // Select the new order
            for (int i = 0; i < orderTableModel.getRowCount(); i++) {
                if ((int) orderTableModel.getValueAt(i, 0) == createdOrder.getId()) {
                    orderTable.setRowSelectionInterval(i, i);
                    break;
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Failed to create order", 
                                         "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Add an item to the current order
     */
    private void addItemToOrder() {
        int selectedRow = orderTable.getSelectedRow();
        
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select an order", 
                                         "Selection Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int orderId = (int) orderTableModel.getValueAt(selectedRow, 0);
        
        // Get the food item ID
        String foodItemIdStr = JOptionPane.showInputDialog(this, "Enter food item ID:", "Add Item", JOptionPane.QUESTION_MESSAGE);
        
        if (foodItemIdStr == null || foodItemIdStr.isEmpty()) {
            return;
        }
        
        try {
            int foodItemId = Integer.parseInt(foodItemIdStr);
            
            // Check if the food item exists
            Optional<FoodItem> foodItemOpt = foodItemService.getFoodItemById(foodItemId);
            
            if (foodItemOpt.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Food item not found", 
                                             "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            FoodItem foodItem = foodItemOpt.get();
            
            // Get the quantity
            String quantityStr = JOptionPane.showInputDialog(this, "Enter quantity:", "Add Item", JOptionPane.QUESTION_MESSAGE);
            
            if (quantityStr == null || quantityStr.isEmpty()) {
                return;
            }
            
            int quantity = Integer.parseInt(quantityStr);
            
            if (quantity <= 0) {
                JOptionPane.showMessageDialog(this, "Quantity must be greater than 0", 
                                             "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Create a new order item
            OrderItem orderItem = new OrderItem(foodItemId, quantity, foodItem.getPrice());
            
            // Add the item to the order
            Order updatedOrder = orderService.addItemToOrder(orderId, orderItem);
            
            if (updatedOrder != null) {
                JOptionPane.showMessageDialog(this, "Item added to order", 
                                             "Success", JOptionPane.INFORMATION_MESSAGE);
                
                // Reload the order items
                loadOrderItems(orderId);
                
                // Reload the orders to update the total
                loadOrders();
                
                // Reselect the order
                for (int i = 0; i < orderTableModel.getRowCount(); i++) {
                    if ((int) orderTableModel.getValueAt(i, 0) == orderId) {
                        orderTable.setRowSelectionInterval(i, i);
                        break;
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add item to order", 
                                             "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid input", 
                                         "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), 
                                         "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Remove an item from the current order
     */
    private void removeItemFromOrder() {
        int selectedOrderRow = orderTable.getSelectedRow();
        int selectedItemRow = orderItemTable.getSelectedRow();
        
        if (selectedOrderRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select an order", 
                                         "Selection Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (selectedItemRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select an order item", 
                                         "Selection Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int orderId = (int) orderTableModel.getValueAt(selectedOrderRow, 0);
        int orderItemId = (int) orderItemTableModel.getValueAt(selectedItemRow, 0);
        
        // Confirm removal
        int confirm = JOptionPane.showConfirmDialog(this, 
                                                  "Are you sure you want to remove this item from the order?", 
                                                  "Confirm Removal", 
                                                  JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // Remove the item from the order
                Order updatedOrder = orderService.removeItemFromOrder(orderId, orderItemId);
                
                if (updatedOrder != null) {
                    JOptionPane.showMessageDialog(this, "Item removed from order", 
                                                 "Success", JOptionPane.INFORMATION_MESSAGE);
                    
                    // Reload the order items
                    loadOrderItems(orderId);
                    
                    // Reload the orders to update the total
                    loadOrders();
                    
                    // Reselect the order
                    for (int i = 0; i < orderTableModel.getRowCount(); i++) {
                        if ((int) orderTableModel.getValueAt(i, 0) == orderId) {
                            orderTable.setRowSelectionInterval(i, i);
                            break;
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to remove item from order", 
                                                 "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), 
                                             "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Update the status of the current order
     */
    private void updateOrderStatus() {
        int selectedRow = orderTable.getSelectedRow();
        
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select an order", 
                                         "Selection Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int orderId = (int) orderTableModel.getValueAt(selectedRow, 0);
        String status = (String) statusComboBox.getSelectedItem();
        String paymentStatus = (String) paymentStatusComboBox.getSelectedItem();
        
        try {
            // Update the order status
            Order updatedOrder = orderService.updateOrderStatus(orderId, status);
            
            if (updatedOrder != null) {
                // Update the payment status
                updatedOrder = orderService.updatePaymentStatus(orderId, paymentStatus);
                
                if (updatedOrder != null) {
                    JOptionPane.showMessageDialog(this, "Order status updated", 
                                                 "Success", JOptionPane.INFORMATION_MESSAGE);
                    
                    // Reload the orders
                    loadOrders();
                    
                    // Reselect the order
                    for (int i = 0; i < orderTableModel.getRowCount(); i++) {
                        if ((int) orderTableModel.getValueAt(i, 0) == orderId) {
                            orderTable.setRowSelectionInterval(i, i);
                            break;
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update payment status", 
                                                 "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update order status", 
                                             "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), 
                                         "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Cancel the current order
     */
    private void cancelOrder() {
        int selectedRow = orderTable.getSelectedRow();
        
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select an order", 
                                         "Selection Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int orderId = (int) orderTableModel.getValueAt(selectedRow, 0);
        
        // Confirm cancellation
        int confirm = JOptionPane.showConfirmDialog(this, 
                                                  "Are you sure you want to cancel this order?", 
                                                  "Confirm Cancellation", 
                                                  JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // Cancel the order
                Order cancelledOrder = orderService.cancelOrder(orderId);
                
                if (cancelledOrder != null) {
                    JOptionPane.showMessageDialog(this, "Order cancelled", 
                                                 "Success", JOptionPane.INFORMATION_MESSAGE);
                    
                    // Reload the orders
                    loadOrders();
                    
                    // Reselect the order
                    for (int i = 0; i < orderTableModel.getRowCount(); i++) {
                        if ((int) orderTableModel.getValueAt(i, 0) == orderId) {
                            orderTable.setRowSelectionInterval(i, i);
                            break;
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to cancel order", 
                                                 "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), 
                                             "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
