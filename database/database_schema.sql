-- PostgreSQL Music Streaming Platform Database Schema
-- Created for Online Music Streaming Platform Project

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';
SET default_table_access_method = heap;

-- ============================================
-- USERS TABLE
-- ============================================

CREATE TABLE IF NOT EXISTS public.users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email TEXT NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    subscription_type VARCHAR(20) DEFAULT 'free',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================
-- ARTISTS TABLE
-- ============================================

CREATE TABLE IF NOT EXISTS public.artists (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    genre VARCHAR(50),
    bio TEXT,
    image_url VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================
-- ALBUMS TABLE
-- ============================================

CREATE TABLE IF NOT EXISTS public.albums (
    id SERIAL PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    artist_id INTEGER NOT NULL REFERENCES public.artists(id),
    release_date DATE,
    cover_image_url VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================
-- SONGS TABLE
-- ============================================

CREATE TABLE IF NOT EXISTS public.songs (
    id SERIAL PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    artist_id INTEGER NOT NULL REFERENCES public.artists(id),
    album_id INTEGER REFERENCES public.albums(id),
    duration INTEGER,
    genre VARCHAR(50),
    file_path VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================
-- TRACKS TABLE (Similar to Songs, can be used for extended metadata)
-- ============================================

CREATE TABLE IF NOT EXISTS public.tracks (
    id SERIAL PRIMARY KEY,
    song_id INTEGER NOT NULL REFERENCES public.songs(id),
    track_number INTEGER,
    duration INTEGER,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================
-- PLAYLISTS TABLE
-- ============================================

CREATE TABLE IF NOT EXISTS public.playlists (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL REFERENCES public.users(id),
    title VARCHAR(100) NOT NULL,
    description TEXT,
    is_public BOOLEAN DEFAULT false,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================
-- PLAYLIST_SONGS JUNCTION TABLE
-- ============================================

CREATE TABLE IF NOT EXISTS public.playlist_songs (
    id SERIAL PRIMARY KEY,
    playlist_id INTEGER NOT NULL REFERENCES public.playlists(id) ON DELETE CASCADE,
    song_id INTEGER NOT NULL REFERENCES public.songs(id) ON DELETE CASCADE,
    added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(playlist_id, song_id)
);

-- ============================================
-- SUBSCRIPTIONS TABLE
-- ============================================

CREATE TABLE IF NOT EXISTS public.subscriptions (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL REFERENCES public.users(id),
    subscription_type VARCHAR(20) NOT NULL,
    start_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    end_date TIMESTAMP,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================
-- RECOMMENDATIONS TABLE
-- ============================================

CREATE TABLE IF NOT EXISTS public.recommendations (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL REFERENCES public.users(id),
    song_id INTEGER NOT NULL REFERENCES public.songs(id),
    recommendation_type VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================
-- USER_FAVORITES TABLE
-- ============================================

CREATE TABLE IF NOT EXISTS public.user_favorites (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL REFERENCES public.users(id),
    song_id INTEGER NOT NULL REFERENCES public.songs(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(user_id, song_id)
);

-- ============================================
-- PLAYBACK_HISTORY TABLE
-- ============================================

CREATE TABLE IF NOT EXISTS public.playback_history (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL REFERENCES public.users(id),
    song_id INTEGER NOT NULL REFERENCES public.songs(id),
    played_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    duration_played INTEGER
);

-- ============================================
-- CREATE INDEXES FOR PERFORMANCE
-- ============================================

CREATE INDEX idx_songs_artist_id ON public.songs(artist_id);
CREATE INDEX idx_songs_album_id ON public.songs(album_id);
CREATE INDEX idx_albums_artist_id ON public.albums(artist_id);
CREATE INDEX idx_playlists_user_id ON public.playlists(user_id);
CREATE INDEX idx_playlist_songs_playlist_id ON public.playlist_songs(playlist_id);
CREATE INDEX idx_playlist_songs_song_id ON public.playlist_songs(song_id);
CREATE INDEX idx_subscriptions_user_id ON public.subscriptions(user_id);
CREATE INDEX idx_recommendations_user_id ON public.recommendations(user_id);
CREATE INDEX idx_user_favorites_user_id ON public.user_favorites(user_id);
CREATE INDEX idx_playback_history_user_id ON public.playback_history(user_id);

-- ============================================
-- SAMPLE DATA (Optional - for testing)
-- ============================================

-- Insert sample artists
INSERT INTO public.artists (name, genre, bio) VALUES
('The Beatles', 'Rock', 'The most influential band of the 20th century'),
('Taylor Swift', 'Pop', 'Contemporary pop artist'),
('Ed Sheeran', 'Pop/Folk', 'Singer-songwriter extraordinaire')
ON CONFLICT DO NOTHING;

-- Insert sample albums
INSERT INTO public.albums (title, artist_id, release_date, cover_image_url) VALUES
('Abbey Road', 1, '1969-09-26', '/images/abbey_road.jpg'),
('Folklore', 2, '2020-07-24', '/images/folklore.jpg'),
('÷ (Divide)', 3, '2017-03-03', '/images/divide.jpg')
ON CONFLICT DO NOTHING;

-- Insert sample songs
INSERT INTO public.songs (title, artist_id, album_id, duration, genre, file_path) VALUES
('Come Together', 1, 1, 259, 'Rock', '/songs/come_together.mp3'),
('Cardigan', 2, 2, 237, 'Pop', '/songs/cardigan.mp3'),
('Shape of You', 3, 3, 234, 'Pop', '/songs/shape_of_you.mp3')
ON CONFLICT DO NOTHING;
