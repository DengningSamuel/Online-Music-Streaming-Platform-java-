package com.musicstreaming.controller;

import com.musicstreaming.service.UserService;
import org.springframework.web.bind.annotation.*;
import java.sql.SQLException;
import java.util.*;


@RestController
@RequestMapping("/api/auth")
public class UserController {
    
    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody Map<String, String> request) {
        try {
            String username = request.get("username");
            String email = request.get("email");
            String password = request.get("password");
            String userType = request.getOrDefault("userType", "listener");
            
            return UserService.registerUser(username, email, password, userType);
        } catch (SQLException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Database error: " + e.getMessage());
            return error;
        }
    }
    
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            String password = request.get("password");
            
            return UserService.loginUser(email, password);
        } catch (SQLException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Database error: " + e.getMessage());
            return error;
        }
    }
    
    @GetMapping("/profile/{userId}")
    public Map<String, Object> getProfile(@PathVariable int userId) {
        try {
            return UserService.getUserProfile(userId);
        } catch (SQLException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to fetch profile: " + e.getMessage());
            return error;
        }
    }
    
    @GetMapping("/users")
    public List<Map<String, Object>> getAllUsers() {
        try {
            return UserService.getAllUsers();
        } catch (SQLException e) {
            List<Map<String, Object>> error = new ArrayList<>();
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("error", "Failed to fetch users");
            error.add(errorMap);
            return error;
        }
    }
}
