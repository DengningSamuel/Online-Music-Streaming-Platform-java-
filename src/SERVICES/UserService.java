import java.sql.SQLException;
import java.util.*;

/**
 * UserService handles user-related business logic
 */
public class UserService {
    
    /**
     * Register a new user
     */
    public static Map<String, Object> registerUser(String username, String email, String password) 
            throws SQLException {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Check if user already exists
            Map<String, Object> existingUser = UserDAO.getUserByEmail(email);
            if (!existingUser.isEmpty()) {
                response.put("success", false);
                response.put("message", "Email already registered");
                return response;
            }
            
            // Hash the password (in production, use bcrypt)
            String passwordHash = hashPassword(password);
            
            // Create user
            boolean created = UserDAO.createUser(username, email, passwordHash);
            
            if (created) {
                Map<String, Object> newUser = UserDAO.getUserByEmail(email);
                response.put("success", true);
                response.put("message", "User registered successfully");
                response.put("user", newUser);
            } else {
                response.put("success", false);
                response.put("message", "Failed to create user");
            }
        } catch (SQLException e) {
            response.put("success", false);
            response.put("message", "Database error: " + e.getMessage());
        }
        
        return response;
    }
    
    /**
     * Login user
     */
    public static Map<String, Object> loginUser(String email, String password) 
            throws SQLException {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Map<String, Object> user = UserDAO.getUserByEmail(email);
            
            if (user.isEmpty()) {
                response.put("success", false);
                response.put("message", "User not found");
                return response;
            }
            
            // In a real app, compare hashed passwords
            // For now, just return the user
            response.put("success", true);
            response.put("message", "Login successful");
            response.put("user", user);
        } catch (SQLException e) {
            response.put("success", false);
            response.put("message", "Database error: " + e.getMessage());
        }
        
        return response;
    }
    
    /**
     * Get user profile
     */
    public static Map<String, Object> getUserProfile(int userId) throws SQLException {
        return UserDAO.getUserById(userId);
    }
    
    /**
     * Update user subscription
     */
    public static boolean updateSubscription(int userId, String subscriptionType) 
            throws SQLException {
        return UserDAO.updateSubscription(userId, subscriptionType);
    }
    
    /**
     * Get all users (admin only)
     */
    public static List<Map<String, Object>> getAllUsers() throws SQLException {
        return UserDAO.getAllUsers();
    }
    
    /**
     * Simple password hashing (use bcrypt in production)
     */
    private static String hashPassword(String password) {
        // In production, use bcrypt or similar
        return Integer.toHexString(password.hashCode());
    }
}
