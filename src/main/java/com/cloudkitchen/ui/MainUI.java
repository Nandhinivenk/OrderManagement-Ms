package com.cloudkitchen.ui;

import com.cloudkitchen.model.Customer;
import com.cloudkitchen.service.CustomerService;
import com.cloudkitchen.service.impl.CustomerServiceImpl;
import com.cloudkitchen.util.AuthUtil;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;

/**
 * Main UI class for the application
 */
public class MainUI {

    private JFrame mainFrame;
    private JPanel mainPanel;
    private CardLayout cardLayout;

    private final CustomerService customerService;

    // Panel names
    private static final String WELCOME_PANEL = "Welcome";
    private static final String LOGIN_PANEL = "Login";
    private static final String REGISTER_PANEL = "Register";
    private static final String CUSTOMER_DASHBOARD_PANEL = "CustomerDashboard";
    private static final String ADMIN_LOGIN_PANEL = "AdminLogin";
    private static final String ADMIN_DASHBOARD_PANEL = "AdminDashboard";

    public MainUI() {
        this.customerService = new CustomerServiceImpl();
    }

    /**
     * Initialize and show the UI
     */
    public void start() {
        // Create the main frame
        mainFrame = new JFrame("Cloud Kitchen Order Management");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(800, 600);
        mainFrame.setLocationRelativeTo(null);

        // Create the card layout and main panel
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Create and add panels
        mainPanel.add(createWelcomePanel(), WELCOME_PANEL);
        mainPanel.add(createLoginPanel(), LOGIN_PANEL);
        mainPanel.add(createRegisterPanel(), REGISTER_PANEL);
        mainPanel.add(createCustomerDashboardPanel(), CUSTOMER_DASHBOARD_PANEL);
        mainPanel.add(createAdminLoginPanel(), ADMIN_LOGIN_PANEL);
        mainPanel.add(createAdminDashboardPanel(), ADMIN_DASHBOARD_PANEL);

        // Add the main panel to the frame
        mainFrame.add(mainPanel);

        // Show the welcome panel
        cardLayout.show(mainPanel, WELCOME_PANEL);

        // Show the frame
        mainFrame.setVisible(true);
    }

    /**
     * Create the welcome panel
     *
     * @return The welcome panel
     */
    private JPanel createWelcomePanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Title
        JLabel titleLabel = new JLabel("Welcome to Cloud Kitchen Order Management", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));

        JButton customerLoginButton = new JButton("Customer Login");
        customerLoginButton.addActionListener(e -> cardLayout.show(mainPanel, LOGIN_PANEL));

        JButton customerRegisterButton = new JButton("Customer Register");
        customerRegisterButton.addActionListener(e -> cardLayout.show(mainPanel, REGISTER_PANEL));

        JButton adminLoginButton = new JButton("Admin Login");
        adminLoginButton.addActionListener(e -> cardLayout.show(mainPanel, ADMIN_LOGIN_PANEL));

        buttonsPanel.add(customerLoginButton);
        buttonsPanel.add(customerRegisterButton);
        buttonsPanel.add(adminLoginButton);

        panel.add(buttonsPanel, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Create the login panel
     *
     * @return The login panel
     */
    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Title
        JLabel titleLabel = new JLabel("Customer Login", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField(20);

        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField(20);

        JButton loginButton = new JButton("Login");
        JButton backButton = new JButton("Back");

        // Add components to the form panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.add(loginButton);
        buttonPanel.add(backButton);
        formPanel.add(buttonPanel, gbc);

        panel.add(formPanel, BorderLayout.CENTER);

        // Add action listeners
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(mainFrame, "Please enter username and password",
                                             "Login Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Optional<Customer> customerOpt = customerService.authenticate(username, password);

            if (customerOpt.isPresent()) {
                Customer customer = customerOpt.get();
                AuthUtil.loginCustomer(customer);
                JOptionPane.showMessageDialog(mainFrame, "Login successful",
                                             "Success", JOptionPane.INFORMATION_MESSAGE);
                cardLayout.show(mainPanel, CUSTOMER_DASHBOARD_PANEL);
            } else {
                JOptionPane.showMessageDialog(mainFrame, "Invalid username or password",
                                             "Login Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        backButton.addActionListener(e -> {
            usernameField.setText("");
            passwordField.setText("");
            cardLayout.show(mainPanel, WELCOME_PANEL);
        });

        return panel;
    }

    /**
     * Create the register panel
     *
     * @return The register panel
     */
    private JPanel createRegisterPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Title
        JLabel titleLabel = new JLabel("Customer Registration", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField(20);

        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField(20);

        JLabel nameLabel = new JLabel("Full Name:");
        JTextField nameField = new JTextField(20);

        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField(20);

        JLabel phoneLabel = new JLabel("Phone:");
        JTextField phoneField = new JTextField(20);

        JLabel addressLabel = new JLabel("Address:");
        JTextField addressField = new JTextField(20);

        JButton registerButton = new JButton("Register");
        JButton backButton = new JButton("Back");

        // Add components to the form panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(nameLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(emailLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(phoneLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(phoneField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(addressLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(addressField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.add(registerButton);
        buttonPanel.add(backButton);
        formPanel.add(buttonPanel, gbc);

        panel.add(formPanel, BorderLayout.CENTER);

        // Add action listeners
        registerButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String name = nameField.getText();
            String email = emailField.getText();
            String phone = phoneField.getText();
            String address = addressField.getText();

            if (username.isEmpty() || password.isEmpty() || name.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(mainFrame, "Please fill in all required fields",
                                             "Registration Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Customer customer = new Customer(username, password, name, email);
            customer.setPhone(phone);
            customer.setAddress(address);

            try {
                Customer registeredCustomer = customerService.register(customer);

                if (registeredCustomer != null) {
                    JOptionPane.showMessageDialog(mainFrame, "Registration successful",
                                                 "Success", JOptionPane.INFORMATION_MESSAGE);

                    // Clear fields
                    usernameField.setText("");
                    passwordField.setText("");
                    nameField.setText("");
                    emailField.setText("");
                    phoneField.setText("");
                    addressField.setText("");

                    // Go to login panel
                    cardLayout.show(mainPanel, LOGIN_PANEL);
                } else {
                    JOptionPane.showMessageDialog(mainFrame, "Registration failed",
                                                 "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(mainFrame, ex.getMessage(),
                                             "Registration Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        backButton.addActionListener(e -> {
            // Clear fields
            usernameField.setText("");
            passwordField.setText("");
            nameField.setText("");
            emailField.setText("");
            phoneField.setText("");
            addressField.setText("");

            cardLayout.show(mainPanel, WELCOME_PANEL);
        });

        return panel;
    }

    /**
     * Create the customer dashboard panel
     *
     * @return The customer dashboard panel
     */
    private JPanel createCustomerDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Title
        JLabel titleLabel = new JLabel("Customer Dashboard", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Tabs
        JTabbedPane tabbedPane = new JTabbedPane();

        // Menu tab
        JPanel menuPanel = new FoodItemPanel();
        tabbedPane.addTab("Menu", menuPanel);

        // Orders tab
        JPanel ordersPanel = new OrderPanel();
        tabbedPane.addTab("Orders", ordersPanel);

        // Profile tab
        JPanel profilePanel = new JPanel(new BorderLayout());
        profilePanel.add(new JLabel("Your profile information will be displayed here", JLabel.CENTER), BorderLayout.CENTER);
        tabbedPane.addTab("Profile", profilePanel);

        panel.add(tabbedPane, BorderLayout.CENTER);

        // Logout button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
            AuthUtil.logout();
            cardLayout.show(mainPanel, WELCOME_PANEL);
        });
        bottomPanel.add(logoutButton);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

    /**
     * Create the admin login panel
     *
     * @return The admin login panel
     */
    private JPanel createAdminLoginPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Title
        JLabel titleLabel = new JLabel("Admin Login", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField(20);

        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField(20);

        JButton loginButton = new JButton("Login");
        JButton backButton = new JButton("Back");

        // Add components to the form panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.add(loginButton);
        buttonPanel.add(backButton);
        formPanel.add(buttonPanel, gbc);

        panel.add(formPanel, BorderLayout.CENTER);

        // Add action listeners
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(mainFrame, "Please enter username and password",
                                             "Login Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // TODO: Implement admin authentication
            // For now, use a hardcoded admin account
            if (username.equals("admin") && password.equals("admin")) {
                JOptionPane.showMessageDialog(mainFrame, "Admin login successful",
                                             "Success", JOptionPane.INFORMATION_MESSAGE);
                cardLayout.show(mainPanel, ADMIN_DASHBOARD_PANEL);
            } else {
                JOptionPane.showMessageDialog(mainFrame, "Invalid username or password",
                                             "Login Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        backButton.addActionListener(e -> {
            usernameField.setText("");
            passwordField.setText("");
            cardLayout.show(mainPanel, WELCOME_PANEL);
        });

        return panel;
    }

    /**
     * Create the admin dashboard panel
     *
     * @return The admin dashboard panel
     */
    private JPanel createAdminDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Title
        JLabel titleLabel = new JLabel("Admin Dashboard", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Tabs
        JTabbedPane tabbedPane = new JTabbedPane();

        // Food Items tab
        JPanel foodItemsPanel = new FoodItemPanel();
        tabbedPane.addTab("Food Items", foodItemsPanel);

        // Orders tab
        JPanel ordersPanel = new OrderPanel();
        tabbedPane.addTab("Orders", ordersPanel);

        // Customers tab
        JPanel customersPanel = new JPanel(new BorderLayout());
        customersPanel.add(new JLabel("Customers management will be displayed here", JLabel.CENTER), BorderLayout.CENTER);
        tabbedPane.addTab("Customers", customersPanel);

        // Inventory tab
        JPanel inventoryPanel = new InventoryPanel();
        tabbedPane.addTab("Inventory", inventoryPanel);

        // Delivery tab
        JPanel deliveryPanel = new DeliveryPanel();
        tabbedPane.addTab("Delivery", deliveryPanel);

        // Food Item Mapping tab
        JPanel foodItemMappingPanel = new FoodItemMappingPanel();
        tabbedPane.addTab("Food Item Mapping", foodItemMappingPanel);

        panel.add(tabbedPane, BorderLayout.CENTER);

        // Logout button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
            AuthUtil.logout();
            cardLayout.show(mainPanel, WELCOME_PANEL);
        });
        bottomPanel.add(logoutButton);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }
}
