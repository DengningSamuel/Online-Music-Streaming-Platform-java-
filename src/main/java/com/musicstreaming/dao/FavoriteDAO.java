package com.musicstreaming.dao;

import java.sql.*;
import java.util.*;

public class FavoriteDAO {
    
    /**
     * Add song to favorites
     */
    public static boolean addFavorite(int userId, int songId) throws SQLException {
        String sql = "INSERT INTO user_favorites (user_id, song_id) VALUES (?, ?) " +
                     "ON CONFLICT DO NOTHING";
        
        try (Connection conn = DBCONNECT.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            stmt.setInt(2, songId);
            
            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        }
    }
    
    /**
     * Remove song from favorites
     */
    public static boolean removeFavorite(int userId, int songId) throws SQLException {
        String sql = "DELETE FROM user_favorites WHERE user_id = ? AND song_id = ?";
        
        try (Connection conn = DBCONNECT.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            stmt.setInt(2, songId);
            
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;
        }
    }
    
    /**
     * Get all favorite songs for a user
     */
    public static List<Map<String, Object>> getUserFavorites(int userId) throws SQLException {
        String sql = "SELECT s.*, a.name as artist_name FROM songs s " +
                     "LEFT JOIN artists a ON s.artist_id = a.id " +
                     "WHERE s.id IN (SELECT song_id FROM user_favorites WHERE user_id = ?) " +
                     "ORDER BY uf.created_at DESC";
        List<Map<String, Object>> favorites = new ArrayList<>();
        
        try (Connection conn = DBCONNECT.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Map<String, Object> song = new HashMap<>();
                song.put("id", rs.getInt("id"));
                song.put("title", rs.getString("title"));
                song.put("artist_name", rs.getString("artist_name"));
                song.put("duration", rs.getInt("duration"));
                favorites.add(song);
            }
        }
        return favorites;
    }
    
    /**
     * Check if song is a favorite
     */
    public static boolean isFavorite(int userId, int songId) throws SQLException {
        String sql = "SELECT 1 FROM user_favorites WHERE user_id = ? AND song_id = ? LIMIT 1";
        
        try (Connection conn = DBCONNECT.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            stmt.setInt(2, songId);
            
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
    }
}
