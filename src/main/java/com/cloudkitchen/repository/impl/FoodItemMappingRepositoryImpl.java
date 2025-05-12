package com.cloudkitchen.repository.impl;

import com.cloudkitchen.model.FoodItemMapping;
import com.cloudkitchen.repository.FoodItemMappingRepository;
import com.cloudkitchen.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of FoodItemMappingRepository
 */
public class FoodItemMappingRepositoryImpl implements FoodItemMappingRepository {
    
    @Override
    public FoodItemMapping save(FoodItemMapping mapping) {
        String sql = "INSERT INTO food_item_mappings (food_item_id, category_id) VALUES (?, ?)";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, mapping.getFoodItemId());
            stmt.setInt(2, mapping.getCategoryId());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating food item mapping failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    mapping.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating food item mapping failed, no ID obtained.");
                }
            }
            
            return mapping;
        } catch (SQLException e) {
            System.err.println("Error saving food item mapping: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public Optional<FoodItemMapping> findById(int id) {
        String sql = "SELECT * FROM food_item_mappings WHERE id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    FoodItemMapping mapping = mapResultSetToFoodItemMapping(rs);
                    return Optional.of(mapping);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding food item mapping by ID: " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }
    
    @Override
    public List<FoodItemMapping> findAll() {
        String sql = "SELECT * FROM food_item_mappings";
        List<FoodItemMapping> mappings = new ArrayList<>();
        
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                FoodItemMapping mapping = mapResultSetToFoodItemMapping(rs);
                mappings.add(mapping);
            }
            
            return mappings;
        } catch (SQLException e) {
            System.err.println("Error finding all food item mappings: " + e.getMessage());
            e.printStackTrace();
            return mappings;
        }
    }
    
    @Override
    public List<FoodItemMapping> findByFoodItemId(int foodItemId) {
        String sql = "SELECT * FROM food_item_mappings WHERE food_item_id = ?";
        List<FoodItemMapping> mappings = new ArrayList<>();
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, foodItemId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    FoodItemMapping mapping = mapResultSetToFoodItemMapping(rs);
                    mappings.add(mapping);
                }
            }
            
            return mappings;
        } catch (SQLException e) {
            System.err.println("Error finding food item mappings by food item ID: " + e.getMessage());
            e.printStackTrace();
            return mappings;
        }
    }
    
    @Override
    public List<FoodItemMapping> findByCategoryId(int categoryId) {
        String sql = "SELECT * FROM food_item_mappings WHERE category_id = ?";
        List<FoodItemMapping> mappings = new ArrayList<>();
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, categoryId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    FoodItemMapping mapping = mapResultSetToFoodItemMapping(rs);
                    mappings.add(mapping);
                }
            }
            
            return mappings;
        } catch (SQLException e) {
            System.err.println("Error finding food item mappings by category ID: " + e.getMessage());
            e.printStackTrace();
            return mappings;
        }
    }
    
    @Override
    public FoodItemMapping update(FoodItemMapping mapping) {
        String sql = "UPDATE food_item_mappings SET food_item_id = ?, category_id = ? WHERE id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, mapping.getFoodItemId());
            stmt.setInt(2, mapping.getCategoryId());
            stmt.setInt(3, mapping.getId());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Updating food item mapping failed, no rows affected.");
            }
            
            return mapping;
        } catch (SQLException e) {
            System.err.println("Error updating food item mapping: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public boolean deleteById(int id) {
        String sql = "DELETE FROM food_item_mappings WHERE id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            int affectedRows = stmt.executeUpdate();
            
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting food item mapping: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean deleteByFoodItemId(int foodItemId) {
        String sql = "DELETE FROM food_item_mappings WHERE food_item_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, foodItemId);
            
            int affectedRows = stmt.executeUpdate();
            
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting food item mappings by food item ID: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean deleteByCategoryId(int categoryId) {
        String sql = "DELETE FROM food_item_mappings WHERE category_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, categoryId);
            
            int affectedRows = stmt.executeUpdate();
            
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting food item mappings by category ID: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Map a ResultSet to a FoodItemMapping object
     * 
     * @param rs The ResultSet to map
     * @return The mapped FoodItemMapping object
     * @throws SQLException If an error occurs while mapping
     */
    private FoodItemMapping mapResultSetToFoodItemMapping(ResultSet rs) throws SQLException {
        FoodItemMapping mapping = new FoodItemMapping();
        mapping.setId(rs.getInt("id"));
        mapping.setFoodItemId(rs.getInt("food_item_id"));
        mapping.setCategoryId(rs.getInt("category_id"));
        
        return mapping;
    }
}
