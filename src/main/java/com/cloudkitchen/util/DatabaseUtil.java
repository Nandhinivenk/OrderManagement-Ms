package com.cloudkitchen.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Utility class for database operations
 */
public class DatabaseUtil {
    private static final String DB_URL = "jdbc:h2:./data/cloudkitchen";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";
    
    private static Connection connection;
    
    /**
     * Get a database connection
     * @return Connection object
     */
    public static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                initializeDatabase();
            } catch (SQLException e) {
                System.err.println("Database connection error: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return connection;
    }
    
    /**
     * Initialize the database with required tables
     */
    private static void initializeDatabase() {
        try (Statement stmt = connection.createStatement()) {
            // Create Customer table
            stmt.execute("CREATE TABLE IF NOT EXISTS customers (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "username VARCHAR(50) UNIQUE NOT NULL, " +
                    "password VARCHAR(100) NOT NULL, " +
                    "name VARCHAR(100) NOT NULL, " +
                    "email VARCHAR(100) UNIQUE NOT NULL, " +
                    "phone VARCHAR(20), " +
                    "address VARCHAR(255), " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ")");
            
            // Create FoodItem table
            stmt.execute("CREATE TABLE IF NOT EXISTS food_items (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "name VARCHAR(100) NOT NULL, " +
                    "description VARCHAR(255), " +
                    "price DECIMAL(10, 2) NOT NULL, " +
                    "category VARCHAR(50), " +
                    "is_available BOOLEAN DEFAULT TRUE, " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ")");
            
            // Create FoodItemMapping table
            stmt.execute("CREATE TABLE IF NOT EXISTS food_item_mappings (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "food_item_id INT NOT NULL, " +
                    "category_id INT NOT NULL, " +
                    "FOREIGN KEY (food_item_id) REFERENCES food_items(id)" +
                    ")");
            
            // Create Order table
            stmt.execute("CREATE TABLE IF NOT EXISTS orders (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "customer_id INT NOT NULL, " +
                    "order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "status VARCHAR(20) NOT NULL, " +
                    "total_amount DECIMAL(10, 2) NOT NULL, " +
                    "payment_method VARCHAR(20), " +
                    "payment_status VARCHAR(20), " +
                    "FOREIGN KEY (customer_id) REFERENCES customers(id)" +
                    ")");
            
            // Create OrderItem table
            stmt.execute("CREATE TABLE IF NOT EXISTS order_items (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "order_id INT NOT NULL, " +
                    "food_item_id INT NOT NULL, " +
                    "quantity INT NOT NULL, " +
                    "price DECIMAL(10, 2) NOT NULL, " +
                    "FOREIGN KEY (order_id) REFERENCES orders(id), " +
                    "FOREIGN KEY (food_item_id) REFERENCES food_items(id)" +
                    ")");
            
            // Create Delivery table
            stmt.execute("CREATE TABLE IF NOT EXISTS deliveries (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "order_id INT NOT NULL, " +
                    "delivery_person VARCHAR(100), " +
                    "delivery_status VARCHAR(20) NOT NULL, " +
                    "delivery_time TIMESTAMP, " +
                    "delivery_address VARCHAR(255) NOT NULL, " +
                    "FOREIGN KEY (order_id) REFERENCES orders(id)" +
                    ")");
            
            // Create Inventory table
            stmt.execute("CREATE TABLE IF NOT EXISTS inventory_items (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "name VARCHAR(100) NOT NULL, " +
                    "quantity INT NOT NULL, " +
                    "unit VARCHAR(20), " +
                    "reorder_level INT, " +
                    "qr_code_path VARCHAR(255), " +
                    "last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ")");
            
            // Create Admin table
            stmt.execute("CREATE TABLE IF NOT EXISTS admins (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "username VARCHAR(50) UNIQUE NOT NULL, " +
                    "password VARCHAR(100) NOT NULL, " +
                    "name VARCHAR(100) NOT NULL, " +
                    "email VARCHAR(100) UNIQUE NOT NULL, " +
                    "role VARCHAR(20) NOT NULL, " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ")");
            
        } catch (SQLException e) {
            System.err.println("Database initialization error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Close the database connection
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                System.err.println("Error closing database connection: " + e.getMessage());
            }
        }
    }
}
