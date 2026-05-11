package com.musicstreaming.controller;

import org.springframework.web.bind.annotation.*;
import java.sql.SQLException;
import java.util.*;
import com.musicstreaming.service.*;

@RestController
@RequestMapping("/api/playlists")
public class PlaylistController {
    
    @PostMapping
    public Map<String, Object> createPlaylist(@RequestBody Map<String, Object> request) {
        try {
            int userId = (Integer) request.get("userId");
            String title = (String) request.get("title");
            String description = (String) request.get("description");
            
            return PlaylistService.createPlaylist(userId, title, description);
        } catch (SQLException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Database error: " + e.getMessage());
            return error;
        }
    }
    
    @GetMapping("/{playlistId}")
    public Map<String, Object> getPlaylistDetails(@PathVariable int playlistId) {
        try {
            return PlaylistService.getPlaylistDetails(playlistId);
        } catch (SQLException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to fetch playlist");
            return error;
        }
    }
    
    @GetMapping("/user/{userId}")
    public List<Map<String, Object>> getUserPlaylists(@PathVariable int userId) {
        try {
            return PlaylistService.getUserPlaylists(userId);
        } catch (SQLException e) {
            List<Map<String, Object>> error = new ArrayList<>();
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("error", "Failed to fetch playlists");
            error.add(errorMap);
            return error;
        }
    }
    
    @PostMapping("/{playlistId}/songs")
    public Map<String, Object> addSongToPlaylist(@PathVariable int playlistId,
                                                  @RequestBody Map<String, Integer> request) {
        try {
            int songId = request.get("songId");
            return PlaylistService.addSongToPlaylist(playlistId, songId);
        } catch (SQLException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Database error: " + e.getMessage());
            return error;
        }
    }
    
    @DeleteMapping("/{playlistId}/songs/{songId}")
    public Map<String, Object> removeSongFromPlaylist(@PathVariable int playlistId,
                                                       @PathVariable int songId) {
        try {
            return PlaylistService.removeSongFromPlaylist(playlistId, songId);
        } catch (SQLException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Database error: " + e.getMessage());
            return error;
        }
    }
    
    @PutMapping("/{playlistId}")
    public Map<String, Object> updatePlaylist(@PathVariable int playlistId,
                                              @RequestBody Map<String, String> request) {
        try {
            String title = request.get("title");
            String description = request.get("description");
            return PlaylistService.updatePlaylist(playlistId, title, description);
        } catch (SQLException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Database error: " + e.getMessage());
            return error;
        }
    }
    
    @DeleteMapping("/{playlistId}")
    public Map<String, Object> deletePlaylist(@PathVariable int playlistId) {
        try {
            return PlaylistService.deletePlaylist(playlistId);
        } catch (SQLException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Database error: " + e.getMessage());
            return error;
        }
    }
}
