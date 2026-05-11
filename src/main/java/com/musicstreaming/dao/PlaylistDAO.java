package com.musicstreaming.dao;

import java.sql.*;
import java.util.*;

public class PlaylistDAO {
    
    /**
     * Create a new playlist
     */
    public static int createPlaylist(int userId, String title, String description, boolean isPublic) throws SQLException {
        String sql = "INSERT INTO playlists (user_id, title, description, is_public) VALUES (?, ?, ?, ?) RETURNING id";
        
        try (Connection conn = DBCONNECT.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            stmt.setString(2, title);
            stmt.setString(3, description);
            stmt.setBoolean(4, isPublic);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        }
        return -1;
    }
    
    /**
     * Get playlist by ID
     */
    public static Map<String, Object> getPlaylistById(int playlistId) throws SQLException {
        String sql = "SELECT * FROM playlists WHERE id = ?";
        Map<String, Object> playlist = new HashMap<>();
        
        try (Connection conn = DBCONNECT.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, playlistId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                playlist.put("id", rs.getInt("id"));
                playlist.put("user_id", rs.getInt("user_id"));
                playlist.put("title", rs.getString("title"));
                playlist.put("description", rs.getString("description"));
                playlist.put("is_public", rs.getBoolean("is_public"));
                playlist.put("created_at", rs.getTimestamp("created_at"));
            }
        }
        return playlist;
    }
    
    /**
     * Get all playlists for a user
     */
    public static List<Map<String, Object>> getUserPlaylists(int userId) throws SQLException {
        String sql = "SELECT * FROM playlists WHERE user_id = ? ORDER BY created_at DESC";
        List<Map<String, Object>> playlists = new ArrayList<>();
        
        try (Connection conn = DBCONNECT.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Map<String, Object> playlist = new HashMap<>();
                playlist.put("id", rs.getInt("id"));
                playlist.put("title", rs.getString("title"));
                playlist.put("description", rs.getString("description"));
                playlist.put("is_public", rs.getBoolean("is_public"));
                playlists.add(playlist);
            }
        }
        return playlists;
    }
    
    /**
     * Add song to playlist
     */
    public static boolean addSongToPlaylist(int playlistId, int songId) throws SQLException {
        String sql = "INSERT INTO playlist_songs (playlist_id, song_id) VALUES (?, ?) " +
                     "ON CONFLICT DO NOTHING";
        
        try (Connection conn = DBCONNECT.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, playlistId);
            stmt.setInt(2, songId);
            
            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        }
    }
    
    /**
     * Remove song from playlist
     */
    public static boolean removeSongFromPlaylist(int playlistId, int songId) throws SQLException {
        String sql = "DELETE FROM playlist_songs WHERE playlist_id = ? AND song_id = ?";
        
        try (Connection conn = DBCONNECT.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, playlistId);
            stmt.setInt(2, songId);
            
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;
        }
    }
    
    /**
     * Get songs in a playlist
     */
    public static List<Map<String, Object>> getPlaylistSongs(int playlistId) throws SQLException {
        String sql = "SELECT s.*, a.name as artist_name FROM songs s " +
                     "LEFT JOIN artists a ON s.artist_id = a.id " +
                     "WHERE s.id IN (SELECT song_id FROM playlist_songs WHERE playlist_id = ?) " +
                     "ORDER BY ps.added_at";
        List<Map<String, Object>> songs = new ArrayList<>();
        
        try (Connection conn = DBCONNECT.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, playlistId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Map<String, Object> song = new HashMap<>();
                song.put("id", rs.getInt("id"));
                song.put("title", rs.getString("title"));
                song.put("artist_name", rs.getString("artist_name"));
                song.put("duration", rs.getInt("duration"));
                songs.add(song);
            }
        }
        return songs;
    }
    
    /**
     * Update playlist
     */
    public static boolean updatePlaylist(int playlistId, String title, String description) throws SQLException {
        String sql = "UPDATE playlists SET title = ?, description = ?, updated_at = CURRENT_TIMESTAMP " +
                     "WHERE id = ?";
        
        try (Connection conn = DBCONNECT.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, title);
            stmt.setString(2, description);
            stmt.setInt(3, playlistId);
            
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        }
    }
    
    /**
     * Delete playlist
     */
    public static boolean deletePlaylist(int playlistId) throws SQLException {
        String sql = "DELETE FROM playlists WHERE id = ?";
        
        try (Connection conn = DBCONNECT.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, playlistId);
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;
        }
    }
}
