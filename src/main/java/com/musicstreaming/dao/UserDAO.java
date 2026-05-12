package com.musicstreaming.dao;

import java.sql.*;
import java.util.*;

public class UserDAO {
    
    /**
     * Create a new user
     */
    public static boolean createUser(String username, String email, String passwordHash, String userType) throws SQLException {
        String sql = "INSERT INTO users (username, email, password_hash, user_type) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DBCONNECT.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            stmt.setString(2, email);
            stmt.setString(3, passwordHash);
            stmt.setString(4, userType);
            
            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            System.err.println("Error creating user: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Get user by ID
     */
    public static Map<String, Object> getUserById(int userId) throws SQLException {
        String sql = "SELECT * FROM users WHERE id = ?";
        Map<String, Object> user = new HashMap<>();
        
        try (Connection conn = DBCONNECT.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                user.put("id", rs.getInt("id"));
                user.put("username", rs.getString("username"));
                user.put("email", rs.getString("email"));
                user.put("subscription_type", rs.getString("subscription_type"));
                user.put("user_type", rs.getString("user_type"));
                user.put("created_at", rs.getTimestamp("created_at"));
            }
        }
        return user;
    }
    
    /**
     * Get user by email
     */
    public static Map<String, Object> getUserByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM users WHERE email = ?";
        Map<String, Object> user = new HashMap<>();
        
        try (Connection conn = DBCONNECT.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                user.put("id", rs.getInt("id"));
                user.put("username", rs.getString("username"));
                user.put("email", rs.getString("email"));
                user.put("subscription_type", rs.getString("subscription_type"));
                user.put("user_type", rs.getString("user_type"));
                user.put("created_at", rs.getTimestamp("created_at"));
            }
        }
        return user;
    }
    
    /**
     * Update user subscription
     */
    public static boolean updateSubscription(int userId, String subscriptionType) throws SQLException {
        String sql = "UPDATE users SET subscription_type = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        
        try (Connection conn = DBCONNECT.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, subscriptionType);
            stmt.setInt(2, userId);
            
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        }
    }
    
    /**
     * Get all users
     */
    public static List<Map<String, Object>> getAllUsers() throws SQLException {
        String sql = "SELECT * FROM users ORDER BY created_at DESC";
        List<Map<String, Object>> users = new ArrayList<>();
        
        try (Connection conn = DBCONNECT.getConnection();
             Statement stmt = conn.createStatement()) {
            
            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                Map<String, Object> user = new HashMap<>();
                user.put("id", rs.getInt("id"));
                user.put("username", rs.getString("username"));
                user.put("email", rs.getString("email"));
                user.put("subscription_type", rs.getString("subscription_type"));
                users.add(user);
            }
        }
        return users;
    }
    
    /**
     * Delete user
     */
    public static boolean deleteUser(int userId) throws SQLException {
        String sql = "DELETE FROM users WHERE id = ?";
        
        try (Connection conn = DBCONNECT.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;
        }
    }
}
