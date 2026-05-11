package com.musicstreaming.dao;

import java.sql.*;
import java.util.*;

public class RecommendationDAO {
    
    /**
     * Add a recommendation for a user
     */
    public static int addRecommendation(int userId, int songId, String recommendationType) throws SQLException {
        String sql = "INSERT INTO recommendations (user_id, song_id, recommendation_type) " +
                     "VALUES (?, ?, ?) RETURNING id";
        
        try (Connection conn = DBCONNECT.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            stmt.setInt(2, songId);
            stmt.setString(3, recommendationType);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        }
        return -1;
    }
    
    /**
     * Get recommendations for a user
     */
    public static List<Map<String, Object>> getUserRecommendations(int userId) throws SQLException {
        String sql = "SELECT r.*, s.title, s.duration, a.name as artist_name FROM recommendations r " +
                     "LEFT JOIN songs s ON r.song_id = s.id " +
                     "LEFT JOIN artists a ON s.artist_id = a.id " +
                     "WHERE r.user_id = ? ORDER BY r.created_at DESC LIMIT 20";
        List<Map<String, Object>> recommendations = new ArrayList<>();
        
        try (Connection conn = DBCONNECT.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Map<String, Object> rec = new HashMap<>();
                rec.put("id", rs.getInt("id"));
                rec.put("song_id", rs.getInt("song_id"));
                rec.put("title", rs.getString("title"));
                rec.put("artist_name", rs.getString("artist_name"));
                rec.put("duration", rs.getInt("duration"));
                rec.put("recommendation_type", rs.getString("recommendation_type"));
                recommendations.add(rec);
            }
        }
        return recommendations;
    }
    
    /**
     * Get popular songs (based on recommendations)
     */
    public static List<Map<String, Object>> getPopularSongs() throws SQLException {
        String sql = "SELECT s.*, a.name as artist_name, COUNT(r.id) as recommendation_count " +
                     "FROM songs s " +
                     "LEFT JOIN artists a ON s.artist_id = a.id " +
                     "LEFT JOIN recommendations r ON s.id = r.song_id " +
                     "GROUP BY s.id, a.name " +
                     "ORDER BY recommendation_count DESC LIMIT 20";
        List<Map<String, Object>> songs = new ArrayList<>();
        
        try (Connection conn = DBCONNECT.getConnection();
             Statement stmt = conn.createStatement()) {
            
            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                Map<String, Object> song = new HashMap<>();
                song.put("id", rs.getInt("id"));
                song.put("title", rs.getString("title"));
                song.put("artist_name", rs.getString("artist_name"));
                song.put("duration", rs.getInt("duration"));
                song.put("recommendation_count", rs.getInt("recommendation_count"));
                songs.add(song);
            }
        }
        return songs;
    }
    
    /**
     * Delete recommendation
     */
    public static boolean deleteRecommendation(int recommendationId) throws SQLException {
        String sql = "DELETE FROM recommendations WHERE id = ?";
        
        try (Connection conn = DBCONNECT.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, recommendationId);
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;
        }
    }
}
