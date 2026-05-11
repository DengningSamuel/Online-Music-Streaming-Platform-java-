import java.sql.SQLException;
import java.util.*;

/**
 * RecommendationService handles user recommendations and favorites
 */
public class RecommendationService {
    
    /**
     * Add a song to user's favorites
     */
    public static Map<String, Object> addFavorite(int userId, int songId) 
            throws SQLException {
        Map<String, Object> response = new HashMap<>();
        
        try {
            boolean added = FavoriteDAO.addFavorite(userId, songId);
            
            if (added) {
                response.put("success", true);
                response.put("message", "Added to favorites");
            } else {
                response.put("success", false);
                response.put("message", "Already in favorites");
            }
        } catch (SQLException e) {
            response.put("success", false);
            response.put("message", "Database error: " + e.getMessage());
        }
        
        return response;
    }
    
    /**
     * Remove a song from user's favorites
     */
    public static Map<String, Object> removeFavorite(int userId, int songId) 
            throws SQLException {
        Map<String, Object> response = new HashMap<>();
        
        try {
            boolean removed = FavoriteDAO.removeFavorite(userId, songId);
            
            if (removed) {
                response.put("success", true);
                response.put("message", "Removed from favorites");
            } else {
                response.put("success", false);
                response.put("message", "Not in favorites");
            }
        } catch (SQLException e) {
            response.put("success", false);
            response.put("message", "Database error: " + e.getMessage());
        }
        
        return response;
    }
    
    /**
     * Get user's favorite songs
     */
    public static List<Map<String, Object>> getUserFavorites(int userId) throws SQLException {
        return FavoriteDAO.getUserFavorites(userId);
    }
    
    /**
     * Check if song is a favorite
     */
    public static boolean isFavorite(int userId, int songId) throws SQLException {
        return FavoriteDAO.isFavorite(userId, songId);
    }
    
    /**
     * Add a recommendation for a song
     */
    public static Map<String, Object> addRecommendation(int userId, int songId, String type) 
            throws SQLException {
        Map<String, Object> response = new HashMap<>();
        
        try {
            int recId = RecommendationDAO.addRecommendation(userId, songId, type);
            
            if (recId > 0) {
                response.put("success", true);
                response.put("message", "Recommendation added");
                response.put("recommendationId", recId);
            } else {
                response.put("success", false);
                response.put("message", "Failed to add recommendation");
            }
        } catch (SQLException e) {
            response.put("success", false);
            response.put("message", "Database error: " + e.getMessage());
        }
        
        return response;
    }
    
    /**
     * Get user recommendations
     */
    public static List<Map<String, Object>> getUserRecommendations(int userId) 
            throws SQLException {
        return RecommendationDAO.getUserRecommendations(userId);
    }
    
    /**
     * Get trending/popular songs
     */
    public static List<Map<String, Object>> getTrendingSongs() throws SQLException {
        return RecommendationDAO.getPopularSongs();
    }
    
    /**
     * Generate smart recommendations based on user's favorites
     */
    public static List<Map<String, Object>> generateSmartRecommendations(int userId) 
            throws SQLException {
        // Get user's favorites
        List<Map<String, Object>> favorites = FavoriteDAO.getUserFavorites(userId);
        
        if (favorites.isEmpty()) {
            // If no favorites, return trending songs
            return RecommendationDAO.getPopularSongs();
        }
        
        // In a real system, this would implement collaborative filtering or content-based recommendations
        // For now, return trending songs as recommendations
        return RecommendationDAO.getPopularSongs();
    }
}
