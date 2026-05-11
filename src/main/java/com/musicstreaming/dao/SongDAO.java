package com.musicstreaming.dao;

import java.sql.*;
import java.util.*;

public class SongDAO {
    
    /**
     * Create a new song
     */
    public static int createSong(String title, int artistId, Integer albumId, int duration, String genre, String filePath) throws SQLException {
        String sql = "INSERT INTO songs (title, artist_id, album_id, duration, genre, file_path) VALUES (?, ?, ?, ?, ?, ?) RETURNING id";
        
        try (Connection conn = DBCONNECT.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, title);
            stmt.setInt(2, artistId);
            if (albumId != null) {
                stmt.setInt(3, albumId);
            } else {
                stmt.setNull(3, Types.INTEGER);
            }
            stmt.setInt(4, duration);
            stmt.setString(5, genre);
            stmt.setString(6, filePath);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        }
        return -1;
    }
    
    /**
     * Get song by ID
     */
    public static Map<String, Object> getSongById(int songId) throws SQLException {
        String sql = "SELECT s.*, a.name as artist_name, al.title as album_title FROM songs s " +
                     "LEFT JOIN artists a ON s.artist_id = a.id " +
                     "LEFT JOIN albums al ON s.album_id = al.id " +
                     "WHERE s.id = ?";
        Map<String, Object> song = new HashMap<>();
        
        try (Connection conn = DBCONNECT.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, songId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                song.put("id", rs.getInt("id"));
                song.put("title", rs.getString("title"));
                song.put("artist_id", rs.getInt("artist_id"));
                song.put("artist_name", rs.getString("artist_name"));
                song.put("album_id", rs.getInt("album_id"));
                song.put("album_title", rs.getString("album_title"));
                song.put("duration", rs.getInt("duration"));
                song.put("genre", rs.getString("genre"));
                song.put("file_path", rs.getString("file_path"));
            }
        }
        return song;
    }
    
    /**
     * Get all songs
     */
    public static List<Map<String, Object>> getAllSongs() throws SQLException {
        String sql = "SELECT s.*, a.name as artist_name FROM songs s " +
                     "LEFT JOIN artists a ON s.artist_id = a.id " +
                     "ORDER BY s.created_at DESC";
        List<Map<String, Object>> songs = new ArrayList<>();
        
        try (Connection conn = DBCONNECT.getConnection();
             Statement stmt = conn.createStatement()) {
            
            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                Map<String, Object> song = new HashMap<>();
                song.put("id", rs.getInt("id"));
                song.put("title", rs.getString("title"));
                song.put("artist_id", rs.getInt("artist_id"));
                song.put("artist_name", rs.getString("artist_name"));
                song.put("duration", rs.getInt("duration"));
                song.put("genre", rs.getString("genre"));
                songs.add(song);
            }
        }
        return songs;
    }
    
    /**
     * Search songs by title
     */
    public static List<Map<String, Object>> searchSongs(String query) throws SQLException {
        String sql = "SELECT s.*, a.name as artist_name FROM songs s " +
                     "LEFT JOIN artists a ON s.artist_id = a.id " +
                     "WHERE LOWER(s.title) LIKE LOWER(?) OR LOWER(a.name) LIKE LOWER(?) " +
                     "ORDER BY s.title";
        List<Map<String, Object>> songs = new ArrayList<>();
        
        try (Connection conn = DBCONNECT.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + query + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Map<String, Object> song = new HashMap<>();
                song.put("id", rs.getInt("id"));
                song.put("title", rs.getString("title"));
                song.put("artist_name", rs.getString("artist_name"));
                song.put("duration", rs.getInt("duration"));
                song.put("genre", rs.getString("genre"));
                songs.add(song);
            }
        }
        return songs;
    }
    
    /**
     * Get songs by artist
     */
    public static List<Map<String, Object>> getSongsByArtist(int artistId) throws SQLException {
        String sql = "SELECT s.*, a.name as artist_name FROM songs s " +
                     "LEFT JOIN artists a ON s.artist_id = a.id " +
                     "WHERE s.artist_id = ? ORDER BY s.title";
        List<Map<String, Object>> songs = new ArrayList<>();
        
        try (Connection conn = DBCONNECT.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, artistId);
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
     * Delete song
     */
    public static boolean deleteSong(int songId) throws SQLException {
        String sql = "DELETE FROM songs WHERE id = ?";
        
        try (Connection conn = DBCONNECT.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, songId);
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;
        }
    }
}
