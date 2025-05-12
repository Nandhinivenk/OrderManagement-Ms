package com.cloudkitchen.repository.impl;

import com.cloudkitchen.model.FoodItem;
import com.cloudkitchen.repository.FoodItemRepository;
import com.cloudkitchen.util.DatabaseUtil;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of FoodItemRepository
 */
public class FoodItemRepositoryImpl implements FoodItemRepository {
    
    @Override
    public FoodItem save(FoodItem foodItem) {
        String sql = "INSERT INTO food_items (name, description, price, category, is_available) " +
                     "VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, foodItem.getName());
            stmt.setString(2, foodItem.getDescription());
            stmt.setBigDecimal(3, foodItem.getPrice());
            stmt.setString(4, foodItem.getCategory());
            stmt.setBoolean(5, foodItem.isAvailable());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating food item failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    foodItem.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating food item failed, no ID obtained.");
                }
            }
            
            return foodItem;
        } catch (SQLException e) {
            System.err.println("Error saving food item: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public Optional<FoodItem> findById(int id) {
        String sql = "SELECT * FROM food_items WHERE id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    FoodItem foodItem = mapResultSetToFoodItem(rs);
                    return Optional.of(foodItem);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding food item by ID: " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }
    
    @Override
    public Optional<FoodItem> findByName(String name) {
        String sql = "SELECT * FROM food_items WHERE name = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, name);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    FoodItem foodItem = mapResultSetToFoodItem(rs);
                    return Optional.of(foodItem);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding food item by name: " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }
    
    @Override
    public List<FoodItem> findAll() {
        String sql = "SELECT * FROM food_items";
        List<FoodItem> foodItems = new ArrayList<>();
        
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                FoodItem foodItem = mapResultSetToFoodItem(rs);
                foodItems.add(foodItem);
            }
            
            return foodItems;
        } catch (SQLException e) {
            System.err.println("Error finding all food items: " + e.getMessage());
            e.printStackTrace();
            return foodItems;
        }
    }
    
    @Override
    public List<FoodItem> findByCategory(String category) {
        String sql = "SELECT * FROM food_items WHERE category = ?";
        List<FoodItem> foodItems = new ArrayList<>();
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, category);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    FoodItem foodItem = mapResultSetToFoodItem(rs);
                    foodItems.add(foodItem);
                }
            }
            
            return foodItems;
        } catch (SQLException e) {
            System.err.println("Error finding food items by category: " + e.getMessage());
            e.printStackTrace();
            return foodItems;
        }
    }
    
    @Override
    public List<FoodItem> findAvailable() {
        String sql = "SELECT * FROM food_items WHERE is_available = TRUE";
        List<FoodItem> foodItems = new ArrayList<>();
        
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                FoodItem foodItem = mapResultSetToFoodItem(rs);
                foodItems.add(foodItem);
            }
            
            return foodItems;
        } catch (SQLException e) {
            System.err.println("Error finding available food items: " + e.getMessage());
            e.printStackTrace();
            return foodItems;
        }
    }
    
    @Override
    public FoodItem update(FoodItem foodItem) {
        String sql = "UPDATE food_items SET name = ?, description = ?, price = ?, " +
                     "category = ?, is_available = ? WHERE id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, foodItem.getName());
            stmt.setString(2, foodItem.getDescription());
            stmt.setBigDecimal(3, foodItem.getPrice());
            stmt.setString(4, foodItem.getCategory());
            stmt.setBoolean(5, foodItem.isAvailable());
            stmt.setInt(6, foodItem.getId());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Updating food item failed, no rows affected.");
            }
            
            return foodItem;
        } catch (SQLException e) {
            System.err.println("Error updating food item: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public boolean deleteById(int id) {
        String sql = "DELETE FROM food_items WHERE id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            int affectedRows = stmt.executeUpdate();
            
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting food item: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Map a ResultSet to a FoodItem object
     * 
     * @param rs The ResultSet to map
     * @return The mapped FoodItem object
     * @throws SQLException If an error occurs while mapping
     */
    private FoodItem mapResultSetToFoodItem(ResultSet rs) throws SQLException {
        FoodItem foodItem = new FoodItem();
        foodItem.setId(rs.getInt("id"));
        foodItem.setName(rs.getString("name"));
        foodItem.setDescription(rs.getString("description"));
        foodItem.setPrice(rs.getBigDecimal("price"));
        foodItem.setCategory(rs.getString("category"));
        foodItem.setAvailable(rs.getBoolean("is_available"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            foodItem.setCreatedAt(createdAt.toLocalDateTime());
        } else {
            foodItem.setCreatedAt(LocalDateTime.now());
        }
        
        return foodItem;
    }
}
