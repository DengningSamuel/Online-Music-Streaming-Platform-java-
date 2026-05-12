package com.musicstreaming.dao;

import java.sql.*;
import java.util.*;

public class AlbumDAO {
    
    /**
     * Create a new album
     */
    public static int createAlbum(String title, int artistId, String releaseDate, String coverImageUrl) throws SQLException {
        String sql = "INSERT INTO albums (title, artist_id, release_date, cover_image_url) VALUES (?, ?, ?, ?) RETURNING id";
        
        try (Connection conn = DBCONNECT.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, title);
            stmt.setInt(2, artistId);
            
            // Handle null or empty release date
            if (releaseDate == null || releaseDate.isEmpty()) {
                stmt.setNull(3, java.sql.Types.DATE);
            } else {
                stmt.setDate(3, java.sql.Date.valueOf(releaseDate));
            }
            
            stmt.setString(4, coverImageUrl);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        }
        return -1;
    }
    
    /**
     * Get album by ID
     */
    public static Map<String, Object> getAlbumById(int albumId) throws SQLException {
        String sql = "SELECT al.*, a.name as artist_name FROM albums al " +
                     "LEFT JOIN artists a ON al.artist_id = a.id " +
                     "WHERE al.id = ?";
        Map<String, Object> album = new HashMap<>();
        
        try (Connection conn = DBCONNECT.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, albumId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                album.put("id", rs.getInt("id"));
                album.put("title", rs.getString("title"));
                album.put("artist_id", rs.getInt("artist_id"));
                album.put("artist_name", rs.getString("artist_name"));
                album.put("release_date", rs.getDate("release_date"));
                album.put("cover_image_url", rs.getString("cover_image_url"));
            }
        }
        return album;
    }
    
    /**
     * Get all albums
     */
    public static List<Map<String, Object>> getAllAlbums() throws SQLException {
        String sql = "SELECT al.*, a.name as artist_name FROM albums al " +
                     "LEFT JOIN artists a ON al.artist_id = a.id " +
                     "ORDER BY al.title";
        List<Map<String, Object>> albums = new ArrayList<>();
        
        try (Connection conn = DBCONNECT.getConnection();
             Statement stmt = conn.createStatement()) {
            
            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                Map<String, Object> album = new HashMap<>();
                album.put("id", rs.getInt("id"));
                album.put("title", rs.getString("title"));
                album.put("artist_id", rs.getInt("artist_id"));
                album.put("artist_name", rs.getString("artist_name"));
                albums.add(album);
            }
        }
        return albums;
    }
    
    /**
     * Get albums by artist
     */
    public static List<Map<String, Object>> getAlbumsByArtist(int artistId) throws SQLException {
        String sql = "SELECT al.*, a.name as artist_name FROM albums al " +
                     "LEFT JOIN artists a ON al.artist_id = a.id " +
                     "WHERE al.artist_id = ? ORDER BY al.title";
        List<Map<String, Object>> albums = new ArrayList<>();
        
        try (Connection conn = DBCONNECT.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, artistId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Map<String, Object> album = new HashMap<>();
                album.put("id", rs.getInt("id"));
                album.put("title", rs.getString("title"));
                album.put("artist_name", rs.getString("artist_name"));
                albums.add(album);
            }
        }
        return albums;
    }
    
    /**
     * Get songs in an album
     */
    public static List<Map<String, Object>> getAlbumSongs(int albumId) throws SQLException {
        String sql = "SELECT * FROM songs WHERE album_id = ? ORDER BY title";
        List<Map<String, Object>> songs = new ArrayList<>();
        
        try (Connection conn = DBCONNECT.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, albumId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Map<String, Object> song = new HashMap<>();
                song.put("id", rs.getInt("id"));
                song.put("title", rs.getString("title"));
                song.put("duration", rs.getInt("duration"));
                songs.add(song);
            }
        }
        return songs;
    }
    
    /**
     * Delete album
     */
    public static boolean deleteAlbum(int albumId) throws SQLException {
        String sql = "DELETE FROM albums WHERE id = ?";
        
        try (Connection conn = DBCONNECT.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, albumId);
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;
        }
    }
}
