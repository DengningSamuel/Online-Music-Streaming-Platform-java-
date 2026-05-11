package com.musicstreaming.controller;

import org.springframework.web.bind.annotation.*;
import java.sql.SQLException;
import java.util.*;
import com.musicstreaming.service.*;

@RestController
@RequestMapping("/api/music")
public class MusicController {
    
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
}
