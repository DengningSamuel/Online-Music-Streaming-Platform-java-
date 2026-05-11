import java.sql.SQLException;
import java.util.*;

/**
 * PlaylistService handles playlist operations
 */
public class PlaylistService {
    
    /**
     * Create a new playlist
     */
    public static Map<String, Object> createPlaylist(int userId, String title, String description) 
            throws SQLException {
        Map<String, Object> response = new HashMap<>();
        
        try {
            int playlistId = PlaylistDAO.createPlaylist(userId, title, description, false);
            
            if (playlistId > 0) {
                response.put("success", true);
                response.put("message", "Playlist created successfully");
                response.put("playlistId", playlistId);
            } else {
                response.put("success", false);
                response.put("message", "Failed to create playlist");
            }
        } catch (SQLException e) {
            response.put("success", false);
            response.put("message", "Database error: " + e.getMessage());
        }
        
        return response;
    }
    
    /**
     * Get user's playlists
     */
    public static List<Map<String, Object>> getUserPlaylists(int userId) throws SQLException {
        return PlaylistDAO.getUserPlaylists(userId);
    }
    
    /**
     * Get playlist with songs
     */
    public static Map<String, Object> getPlaylistDetails(int playlistId) throws SQLException {
        Map<String, Object> playlist = PlaylistDAO.getPlaylistById(playlistId);
        if (!playlist.isEmpty()) {
            playlist.put("songs", PlaylistDAO.getPlaylistSongs(playlistId));
        }
        return playlist;
    }
    
    /**
     * Add song to playlist
     */
    public static Map<String, Object> addSongToPlaylist(int playlistId, int songId) 
            throws SQLException {
        Map<String, Object> response = new HashMap<>();
        
        try {
            boolean added = PlaylistDAO.addSongToPlaylist(playlistId, songId);
            
            if (added) {
                response.put("success", true);
                response.put("message", "Song added to playlist");
            } else {
                response.put("success", false);
                response.put("message", "Song already in playlist or addition failed");
            }
        } catch (SQLException e) {
            response.put("success", false);
            response.put("message", "Database error: " + e.getMessage());
        }
        
        return response;
    }
    
    /**
     * Remove song from playlist
     */
    public static Map<String, Object> removeSongFromPlaylist(int playlistId, int songId) 
            throws SQLException {
        Map<String, Object> response = new HashMap<>();
        
        try {
            boolean removed = PlaylistDAO.removeSongFromPlaylist(playlistId, songId);
            
            if (removed) {
                response.put("success", true);
                response.put("message", "Song removed from playlist");
            } else {
                response.put("success", false);
                response.put("message", "Song not found in playlist");
            }
        } catch (SQLException e) {
            response.put("success", false);
            response.put("message", "Database error: " + e.getMessage());
        }
        
        return response;
    }
    
    /**
     * Update playlist
     */
    public static Map<String, Object> updatePlaylist(int playlistId, String title, String description) 
            throws SQLException {
        Map<String, Object> response = new HashMap<>();
        
        try {
            boolean updated = PlaylistDAO.updatePlaylist(playlistId, title, description);
            
            if (updated) {
                response.put("success", true);
                response.put("message", "Playlist updated successfully");
            } else {
                response.put("success", false);
                response.put("message", "Failed to update playlist");
            }
        } catch (SQLException e) {
            response.put("success", false);
            response.put("message", "Database error: " + e.getMessage());
        }
        
        return response;
    }
    
    /**
     * Delete playlist
     */
    public static Map<String, Object> deletePlaylist(int playlistId) throws SQLException {
        Map<String, Object> response = new HashMap<>();
        
        try {
            boolean deleted = PlaylistDAO.deletePlaylist(playlistId);
            
            if (deleted) {
                response.put("success", true);
                response.put("message", "Playlist deleted successfully");
            } else {
                response.put("success", false);
                response.put("message", "Failed to delete playlist");
            }
        } catch (SQLException e) {
            response.put("success", false);
            response.put("message", "Database error: " + e.getMessage());
        }
        
        return response;
    }
}
