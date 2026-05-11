package com.musicstreaming.service;

import com.musicstreaming.dao.*;
import java.sql.SQLException;
import java.util.*;

/**
 * MusicService handles music library operations
 */
public class MusicService {
    
    /**
     * Search for songs
     */
    public static List<Map<String, Object>> searchSongs(String query) throws SQLException {
        return SongDAO.searchSongs(query);
    }
    
    /**
     * Get all songs
     */
    public static List<Map<String, Object>> getAllSongs() throws SQLException {
        return SongDAO.getAllSongs();
    }
    
    /**
     * Get song details
     */
    public static Map<String, Object> getSongDetails(int songId) throws SQLException {
        return SongDAO.getSongById(songId);
    }
    
    /**
     * Get songs by artist
     */
    public static List<Map<String, Object>> getSongsByArtist(int artistId) throws SQLException {
        return SongDAO.getSongsByArtist(artistId);
    }
    
    /**
     * Get all artists
     */
    public static List<Map<String, Object>> getAllArtists() throws SQLException {
        return ArtistDAO.getAllArtists();
    }
    
    /**
     * Get artist details
     */
    public static Map<String, Object> getArtistDetails(int artistId) throws SQLException {
        return ArtistDAO.getArtistById(artistId);
    }
    
    /**
     * Search for artists
     */
    public static List<Map<String, Object>> searchArtists(String query) throws SQLException {
        return ArtistDAO.searchArtists(query);
    }
    
    /**
     * Get artists by genre
     */
    public static List<Map<String, Object>> getArtistsByGenre(String genre) throws SQLException {
        return ArtistDAO.getArtistsByGenre(genre);
    }
    
    /**
     * Get all albums
     */
    public static List<Map<String, Object>> getAllAlbums() throws SQLException {
        return AlbumDAO.getAllAlbums();
    }
    
    /**
     * Get album details with songs
     */
    public static Map<String, Object> getAlbumDetails(int albumId) throws SQLException {
        Map<String, Object> album = AlbumDAO.getAlbumById(albumId);
        if (!album.isEmpty()) {
            album.put("songs", AlbumDAO.getAlbumSongs(albumId));
        }
        return album;
    }
    
    /**
     * Get albums by artist
     */
    public static List<Map<String, Object>> getAlbumsByArtist(int artistId) throws SQLException {
        return AlbumDAO.getAlbumsByArtist(artistId);
    }
    
    /**
     * Add a new song
     */
    public static int addSong(String title, int artistId, Integer albumId, int duration, 
                              String genre, String filePath) throws SQLException {
        return SongDAO.createSong(title, artistId, albumId, duration, genre, filePath);
    }
    
    /**
     * Add a new artist
     */
    public static int addArtist(String name, String genre, String bio, String imageUrl) 
            throws SQLException {
        return ArtistDAO.createArtist(name, genre, bio, imageUrl);
    }
    
    /**
     * Add a new album
     */
    public static int addAlbum(String title, int artistId, String releaseDate, String coverImageUrl) 
            throws SQLException {
        return AlbumDAO.createAlbum(title, artistId, releaseDate, coverImageUrl);
    }
    
    /**
     * Get trending songs (most recommended)
     */
    public static List<Map<String, Object>> getTrendingSongs() throws SQLException {
        return RecommendationDAO.getPopularSongs();
    }
}
