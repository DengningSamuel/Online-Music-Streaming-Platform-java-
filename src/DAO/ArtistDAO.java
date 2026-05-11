import java.sql.*;
import java.util.*;

public class ArtistDAO {
    
    /**
     * Create a new artist
     */
    public static int createArtist(String name, String genre, String bio, String imageUrl) throws SQLException {
        String sql = "INSERT INTO artists (name, genre, bio, image_url) VALUES (?, ?, ?, ?) RETURNING id";
        
        try (Connection conn = DBCONNECT.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, name);
            stmt.setString(2, genre);
            stmt.setString(3, bio);
            stmt.setString(4, imageUrl);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        }
        return -1;
    }
    
    /**
     * Get artist by ID
     */
    public static Map<String, Object> getArtistById(int artistId) throws SQLException {
        String sql = "SELECT * FROM artists WHERE id = ?";
        Map<String, Object> artist = new HashMap<>();
        
        try (Connection conn = DBCONNECT.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, artistId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                artist.put("id", rs.getInt("id"));
                artist.put("name", rs.getString("name"));
                artist.put("genre", rs.getString("genre"));
                artist.put("bio", rs.getString("bio"));
                artist.put("image_url", rs.getString("image_url"));
            }
        }
        return artist;
    }
    
    /**
     * Get all artists
     */
    public static List<Map<String, Object>> getAllArtists() throws SQLException {
        String sql = "SELECT * FROM artists ORDER BY name";
        List<Map<String, Object>> artists = new ArrayList<>();
        
        try (Connection conn = DBCONNECT.getConnection();
             Statement stmt = conn.createStatement()) {
            
            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                Map<String, Object> artist = new HashMap<>();
                artist.put("id", rs.getInt("id"));
                artist.put("name", rs.getString("name"));
                artist.put("genre", rs.getString("genre"));
                artist.put("bio", rs.getString("bio"));
                artists.add(artist);
            }
        }
        return artists;
    }
    
    /**
     * Search artists by name
     */
    public static List<Map<String, Object>> searchArtists(String query) throws SQLException {
        String sql = "SELECT * FROM artists WHERE LOWER(name) LIKE LOWER(?) ORDER BY name";
        List<Map<String, Object>> artists = new ArrayList<>();
        
        try (Connection conn = DBCONNECT.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + query + "%");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Map<String, Object> artist = new HashMap<>();
                artist.put("id", rs.getInt("id"));
                artist.put("name", rs.getString("name"));
                artist.put("genre", rs.getString("genre"));
                artists.add(artist);
            }
        }
        return artists;
    }
    
    /**
     * Get artists by genre
     */
    public static List<Map<String, Object>> getArtistsByGenre(String genre) throws SQLException {
        String sql = "SELECT * FROM artists WHERE genre = ? ORDER BY name";
        List<Map<String, Object>> artists = new ArrayList<>();
        
        try (Connection conn = DBCONNECT.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, genre);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Map<String, Object> artist = new HashMap<>();
                artist.put("id", rs.getInt("id"));
                artist.put("name", rs.getString("name"));
                artists.add(artist);
            }
        }
        return artists;
    }
    
    /**
     * Delete artist
     */
    public static boolean deleteArtist(int artistId) throws SQLException {
        String sql = "DELETE FROM artists WHERE id = ?";
        
        try (Connection conn = DBCONNECT.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, artistId);
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;
        }
    }
}
