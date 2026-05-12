package com.musicstreaming.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.*;
import com.musicstreaming.service.*;

@RestController
@RequestMapping("/api/music")
public class MusicController {
    
    // Upload directory path
    private static final String UPLOAD_DIR = "uploads/music/";
    
    // ===== SONGS =====
    
    @GetMapping("/songs")
    public List<Map<String, Object>> getAllSongs() {
        try {
            return MusicService.getAllSongs();
        } catch (SQLException e) {
            List<Map<String, Object>> error = new ArrayList<>();
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("error", "Failed to fetch songs");
            error.add(errorMap);
            return error;
        }
    }
    
    @GetMapping("/songs/{songId}")
    public Map<String, Object> getSongDetails(@PathVariable int songId) {
        try {
            return MusicService.getSongDetails(songId);
        } catch (SQLException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to fetch song");
            return error;
        }
    }
    
    @GetMapping("/search/songs")
    public List<Map<String, Object>> searchSongs(@RequestParam String query) {
        try {
            return MusicService.searchSongs(query);
        } catch (SQLException e) {
            List<Map<String, Object>> error = new ArrayList<>();
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("error", "Search failed");
            error.add(errorMap);
            return error;
        }
    }
    
    // ===== ARTISTS =====
    
    @GetMapping("/artists")
    public List<Map<String, Object>> getAllArtists() {
        try {
            return MusicService.getAllArtists();
        } catch (SQLException e) {
            List<Map<String, Object>> error = new ArrayList<>();
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("error", "Failed to fetch artists");
            error.add(errorMap);
            return error;
        }
    }
    
    @GetMapping("/artists/{artistId}")
    public Map<String, Object> getArtistDetails(@PathVariable int artistId) {
        try {
            Map<String, Object> artist = MusicService.getArtistDetails(artistId);
            if (!artist.isEmpty()) {
                artist.put("songs", MusicService.getSongsByArtist(artistId));
            }
            return artist;
        } catch (SQLException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to fetch artist");
            return error;
        }
    }
    
    @GetMapping("/search/artists")
    public List<Map<String, Object>> searchArtists(@RequestParam String query) {
        try {
            return MusicService.searchArtists(query);
        } catch (SQLException e) {
            List<Map<String, Object>> error = new ArrayList<>();
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("error", "Search failed");
            error.add(errorMap);
            return error;
        }
    }
    
    @GetMapping("/artists/genre/{genre}")
    public List<Map<String, Object>> getArtistsByGenre(@PathVariable String genre) {
        try {
            return MusicService.getArtistsByGenre(genre);
        } catch (SQLException e) {
            List<Map<String, Object>> error = new ArrayList<>();
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("error", "Failed to fetch artists");
            error.add(errorMap);
            return error;
        }
    }
    
    // ===== ALBUMS =====
    
    @GetMapping("/albums")
    public List<Map<String, Object>> getAllAlbums() {
        try {
            return MusicService.getAllAlbums();
        } catch (SQLException e) {
            List<Map<String, Object>> error = new ArrayList<>();
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("error", "Failed to fetch albums");
            error.add(errorMap);
            return error;
        }
    }
    
    @GetMapping("/albums/{albumId}")
    public Map<String, Object> getAlbumDetails(@PathVariable int albumId) {
        try {
            return MusicService.getAlbumDetails(albumId);
        } catch (SQLException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to fetch album");
            return error;
        }
    }
    
    @GetMapping("/artists/{artistId}/albums")
    public List<Map<String, Object>> getArtistAlbums(@PathVariable int artistId) {
        try {
            return MusicService.getAlbumsByArtist(artistId);
        } catch (SQLException e) {
            List<Map<String, Object>> error = new ArrayList<>();
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("error", "Failed to fetch albums");
            error.add(errorMap);
            return error;
        }
    }
    
    // ===== TRENDING =====
    
    @GetMapping("/trending")
    public List<Map<String, Object>> getTrendingSongs() {
        try {
            return MusicService.getTrendingSongs();
        } catch (SQLException e) {
            List<Map<String, Object>> error = new ArrayList<>();
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("error", "Failed to fetch trending songs");
            error.add(errorMap);
            return error;
        }
    }
    
    // ===== FILE UPLOAD =====
    
    @PostMapping("/upload")
    public Map<String, Object> uploadSong(
            @RequestParam("title") String title,
            @RequestParam("artistName") String artistName,
            @RequestParam("albumTitle") String albumTitle,
            @RequestParam("genre") String genre,
            @RequestParam("userId") int userId,
            @RequestParam("songFile") MultipartFile songFile,
            @RequestParam("imageFile") MultipartFile imageFile) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Create upload directory if it doesn't exist
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            
            // Save song file
            String songFilename = System.currentTimeMillis() + "_" + songFile.getOriginalFilename();
            String songPath = UPLOAD_DIR + "songs/" + songFilename;
            new File(UPLOAD_DIR + "songs/").mkdirs();
            Files.write(Paths.get(songPath), songFile.getBytes());
            
            // Save image file
            String imageFilename = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
            String imagePath = UPLOAD_DIR + "images/" + imageFilename;
            new File(UPLOAD_DIR + "images/").mkdirs();
            Files.write(Paths.get(imagePath), imageFile.getBytes());
            
            // Add artist to database, tied to the uploading user
            int artistId = MusicService.addOrGetArtist(artistName, genre, userId);
            
            // Add album to database
            int albumId = MusicService.addOrGetAlbum(albumTitle, artistId, "/uploads/music/images/" + imageFilename);
            
            // Add song to database
            int songId = MusicService.addSong(title, artistId, albumId, 0, genre, "/uploads/music/songs/" + songFilename);
            
            response.put("success", true);
            response.put("message", "Song uploaded successfully!");
            response.put("songId", songId);
            
        } catch (IOException e) {
            response.put("success", false);
            response.put("message", "Failed to save file: " + e.getMessage());
        } catch (SQLException e) {
            response.put("success", false);
            response.put("message", "Database error: " + e.getMessage());
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Upload error: " + e.getMessage());
        }
        
        return response;
    }
    
    @GetMapping("/user/{userId}")
    public List<Map<String, Object>> getUserSongs(@PathVariable int userId) {
        try {
            return MusicService.getSongsByUser(userId);
        } catch (SQLException e) {
            List<Map<String, Object>> error = new ArrayList<>();
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("error", "Failed to fetch user songs");
            error.add(errorMap);
            return error;
        }
    }
    
    @DeleteMapping("/songs/{songId}")
    public Map<String, Object> deleteSong(@PathVariable int songId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            boolean deleted = MusicService.deleteSong(songId);
            if (deleted) {
                response.put("success", true);
                response.put("message", "Song deleted successfully");
            } else {
                response.put("success", false);
                response.put("message", "Failed to delete song");
            }
        } catch (SQLException e) {
            response.put("success", false);
            response.put("message", "Database error: " + e.getMessage());
        }
        
        return response;
    }
}
