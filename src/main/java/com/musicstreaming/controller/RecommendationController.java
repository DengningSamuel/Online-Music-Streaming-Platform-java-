package com.musicstreaming.controller;

import org.springframework.web.bind.annotation.*;
import java.sql.SQLException;
import java.util.*;
import com.musicstreaming.service.*;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {
    
    // ===== FAVORITES =====
    
    @PostMapping("/favorites")
    public Map<String, Object> addFavorite(@RequestBody Map<String, Integer> request) {
        try {
            int userId = request.get("userId");
            int songId = request.get("songId");
            
            return RecommendationService.addFavorite(userId, songId);
        } catch (SQLException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Database error: " + e.getMessage());
            return error;
        }
    }
    
    @DeleteMapping("/favorites/{userId}/{songId}")
    public Map<String, Object> removeFavorite(@PathVariable int userId,
                                              @PathVariable int songId) {
        try {
            return RecommendationService.removeFavorite(userId, songId);
        } catch (SQLException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Database error: " + e.getMessage());
            return error;
        }
    }
    
    @GetMapping("/favorites/{userId}")
    public List<Map<String, Object>> getUserFavorites(@PathVariable int userId) {
        try {
            return RecommendationService.getUserFavorites(userId);
        } catch (SQLException e) {
            List<Map<String, Object>> error = new ArrayList<>();
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("error", "Failed to fetch favorites");
            error.add(errorMap);
            return error;
        }
    }
    
    @GetMapping("/favorites/{userId}/{songId}")
    public Map<String, Object> isFavorite(@PathVariable int userId,
                                          @PathVariable int songId) {
        try {
            boolean isFav = RecommendationService.isFavorite(userId, songId);
            Map<String, Object> response = new HashMap<>();
            response.put("isFavorite", isFav);
            return response;
        } catch (SQLException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to check favorite status");
            return error;
        }
    }
    
    // ===== RECOMMENDATIONS =====
    
    @GetMapping("/{userId}")
    public List<Map<String, Object>> getUserRecommendations(@PathVariable int userId) {
        try {
            return RecommendationService.getUserRecommendations(userId);
        } catch (SQLException e) {
            List<Map<String, Object>> error = new ArrayList<>();
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("error", "Failed to fetch recommendations");
            error.add(errorMap);
            return error;
        }
    }
    
    @GetMapping("/smart/{userId}")
    public List<Map<String, Object>> getSmartRecommendations(@PathVariable int userId) {
        try {
            return RecommendationService.generateSmartRecommendations(userId);
        } catch (SQLException e) {
            List<Map<String, Object>> error = new ArrayList<>();
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("error", "Failed to generate recommendations");
            error.add(errorMap);
            return error;
        }
    }
    
    @GetMapping("/trending")
    public List<Map<String, Object>> getTrendingSongs() {
        try {
            return RecommendationService.getTrendingSongs();
        } catch (SQLException e) {
            List<Map<String, Object>> error = new ArrayList<>();
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("error", "Failed to fetch trending songs");
            error.add(errorMap);
            return error;
        }
    }
    
    @PostMapping
    public Map<String, Object> addRecommendation(@RequestBody Map<String, Object> request) {
        try {
            int userId = (Integer) request.get("userId");
            int songId = (Integer) request.get("songId");
            String type = (String) request.get("type");
            
            return RecommendationService.addRecommendation(userId, songId, type);
        } catch (SQLException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Database error: " + e.getMessage());
            return error;
        }
    }
}
