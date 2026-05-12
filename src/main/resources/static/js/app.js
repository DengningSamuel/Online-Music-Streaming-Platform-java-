// ==================== CONFIGURATION ====================
const API_BASE_URL = 'http://localhost:9090/api';
const currentUser = JSON.parse(localStorage.getItem('currentUser')) || null;
let currentSong = null;
let isPlaying = false;
let currentPlaylist = [];
let currentPlaylistIndex = 0;

// ==================== INITIALIZATION ====================
document.addEventListener('DOMContentLoaded', function() {
    initializeApp();
    setupEventListeners();
});

function initializeApp() {
    // Load theme preference
    const savedTheme = localStorage.getItem('theme') || 'dark';
    document.body.classList.toggle('light-mode', savedTheme === 'light');
    
    // Check if user is logged in
    if (currentUser) {
        showApp();
        loadDashboard();
    } else {
        showAuthPage();
    }
}

function setupEventListeners() {
    // Auth Events
    document.getElementById('loginForm').addEventListener('submit', handleLogin);
    document.getElementById('signupForm').addEventListener('submit', handleSignup);
    document.getElementById('logoutBtn').addEventListener('click', handleLogout);
    
    // Navigation Events
    document.querySelectorAll('.nav-item').forEach(item => {
        item.addEventListener('click', function(e) {
            e.preventDefault();
            const page = this.dataset.page;
            showPage(page);
        });
    });
    
    // Search Events
    document.getElementById('searchInput').addEventListener('input', handleSearch);
    document.getElementById('searchType').addEventListener('change', handleSearch);
    document.getElementById('libraryFilter').addEventListener('change', handleLibraryFilter);
    
    // Player Events
    document.getElementById('playerPlayBtn').addEventListener('click', togglePlay);
    document.getElementById('playerPrevBtn').addEventListener('click', playPrevious);
    document.getElementById('playerNextBtn').addEventListener('click', playNext);
    document.getElementById('playerProgressInput').addEventListener('change', seek);
    document.getElementById('playerVolume').addEventListener('input', setVolume);
    
    // Audio Player Events
    const audioPlayer = document.getElementById('audioPlayer');
    audioPlayer.addEventListener('timeupdate', updatePlayerProgress);
    audioPlayer.addEventListener('loadedmetadata', updatePlayerDuration);
    audioPlayer.addEventListener('ended', playNext);
    
    // Playlist Events
    document.getElementById('createPlaylistBtn').addEventListener('click', openPlaylistModal);
    
    // Artist Events
    document.getElementById('uploadSongBtn').addEventListener('click', openUploadModal);
    document.getElementById('uploadSongForm').addEventListener('submit', function(e) {
        e.preventDefault();
    });
    
    // Theme Toggle
    document.getElementById('themeToggle').addEventListener('click', toggleTheme);
}

// ==================== AUTH FUNCTIONS ====================
function toggleAuthForm() {
    document.getElementById('loginForm').classList.toggle('hidden');
    document.getElementById('signupForm').classList.toggle('hidden');
}

async function handleLogin(e) {
    e.preventDefault();
    
    const email = document.getElementById('loginEmail').value;
    const password = document.getElementById('loginPassword').value;
    
    try {
        const response = await fetch(`${API_BASE_URL}/auth/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email, password })
        });
        
        // Log response status
        console.log('Login response status:', response.status);
        
        // Check if response is ok
        if (!response.ok) {
            const errorText = await response.text();
            console.error('Login error response:', errorText);
            showToast('Login failed: ' + (response.status === 500 ? 'Server error' : 'Invalid credentials'), 'error');
            return;
        }
        
        const data = await response.json();
        
        if (data.success) {
            localStorage.setItem('currentUser', JSON.stringify(data.user));
            location.reload();
        } else {
            showToast(data.message, 'error');
        }
    } catch (error) {
        console.error('Login fetch error:', error);
        showToast('Login failed: ' + error.message, 'error');
    }
}

async function handleSignup(e) {
    e.preventDefault();
    
    const username = document.getElementById('signupUsername').value;
    const email = document.getElementById('signupEmail').value;
    const password = document.getElementById('signupPassword').value;
    const confirmPassword = document.getElementById('signupPasswordConfirm').value;
    const userType = document.querySelector('input[name="userType"]:checked').value;
    
    if (password !== confirmPassword) {
        showToast('Passwords do not match', 'error');
        return;
    }
    
    try {
        const response = await fetch(`${API_BASE_URL}/auth/register`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, email, password, userType })
        });
        
        // Log response status
        console.log('Signup response status:', response.status);
        
        // Check if response is ok
        if (!response.ok) {
            const errorText = await response.text();
            console.error('Signup error response:', errorText);
            showToast('Signup failed: ' + (response.status === 500 ? 'Server error' : 'Registration error'), 'error');
            return;
        }
        
        const data = await response.json();
        
        if (data.success) {
            showToast('Account created successfully!', 'success');
            document.getElementById('signupForm').reset();
            toggleAuthForm();
        } else {
            showToast(data.message, 'error');
            console.log('Signup failed:', data);
        }
    } catch (error) {
        console.error('Signup fetch error:', error);
        showToast('Signup failed: ' + error.message, 'error');
    }
}

function handleLogout() {
    localStorage.removeItem('currentUser');
    location.reload();
}

// ==================== PAGE MANAGEMENT ====================
function showAuthPage() {
    document.getElementById('authContainer').classList.remove('hidden');
    document.getElementById('appContainer').classList.add('hidden');
}

function showApp() {
    document.getElementById('authContainer').classList.add('hidden');
    document.getElementById('appContainer').classList.remove('hidden');
    updateUserInfo();
    updateArtistNavigation();
    updateDashboardHeader();
}

function showPage(pageName) {
    // Hide all pages
    document.querySelectorAll('.page').forEach(page => {
        page.classList.remove('active');
    });
    
    // Remove active from all nav items
    document.querySelectorAll('.nav-item').forEach(item => {
        item.classList.remove('active');
    });
    
    if (pageName === 'artist' && currentUser?.user_type !== 'artist') {
        showToast('Artist dashboard is only available for artist accounts', 'error');
        pageName = 'dashboard';
    }
    
    // Show selected page
    const pageId = pageName + 'Page';
    const page = document.getElementById(pageId);
    if (page) {
        page.classList.add('active');
    }
    
    // Mark nav item as active
    document.querySelector(`[data-page="${pageName}"]`)?.classList.add('active');
    
    // Load page content
    switch(pageName) {
        case 'dashboard':
            loadDashboard();
            break;
        case 'search':
            // Search is handled by input
            break;
        case 'library':
            loadLibrary();
            break;
        case 'favorites':
            loadFavorites();
            break;
        case 'playlists':
            loadPlaylists();
            break;
        case 'trending':
            loadTrending();
            break;
        case 'artist':
            loadArtistDashboard();
            break;
    }
}

// ==================== DASHBOARD ====================
async function loadDashboard() {
    try {
        let trendingData, recentData;
        
        if (currentUser?.user_type === 'artist') {
            // For artists: Show their songs as "My Music" and recent global songs
            const mySongsResponse = await fetch(`${API_BASE_URL}/music/user/${currentUser.id}`);
            const mySongs = await mySongsResponse.json();
            if (mySongs.error) throw new Error(mySongs.error);
            
            const recentResponse = await fetch(`${API_BASE_URL}/music/songs`);
            recentData = await recentResponse.json();
            if (recentData.error) throw new Error(recentData.error);
            
            trendingData = mySongs;
            
            // Update section headers
            const firstHeader = document.querySelector('#dashboardPage .section:nth-child(1) h3');
            if (firstHeader) firstHeader.textContent = 'My Music';
            const secondHeader = document.querySelector('#dashboardPage .section:nth-child(2) h3');
            if (secondHeader) secondHeader.textContent = 'Recently Added';
        } else {
            // For listeners: Show trending and recent
            const trendingResponse = await fetch(`${API_BASE_URL}/music/trending`);
            trendingData = await trendingResponse.json();
            if (trendingData.error) throw new Error(trendingData.error);
            
            const recentResponse = await fetch(`${API_BASE_URL}/music/songs`);
            recentData = await recentResponse.json();
            if (recentData.error) throw new Error(recentData.error);
            
            // Update section headers
            const firstHeader = document.querySelector('#dashboardPage .section:nth-child(1) h3');
            if (firstHeader) firstHeader.textContent = 'Trending Now';
            const secondHeader = document.querySelector('#dashboardPage .section:nth-child(2) h3');
            if (secondHeader) secondHeader.textContent = 'Recently Added';
        }
        
        renderSongGrid(trendingData.slice(0, 6), 'trendingContainer');
        renderSongGrid(recentData.slice(0, 6), 'recentContainer');
    } catch (error) {
        console.error('Error loading dashboard:', error);
        showToast('Failed to load dashboard: ' + error.message, 'error');
    }
}

// ==================== SEARCH ====================
async function handleSearch() {
    const query = document.getElementById('searchInput').value;
    const searchType = document.getElementById('searchType').value;
    
    if (query.length < 2) {
        document.getElementById('searchResults').innerHTML = '';
        return;
    }
    
    try {
        const endpoint = `${API_BASE_URL}/music/search/${searchType}?query=${encodeURIComponent(query)}`;
        const results = await fetch(endpoint).then(r => r.json());
        
        if (searchType === 'songs') {
            renderSongGrid(results, 'searchResults');
        } else if (searchType === 'artists') {
            renderArtistGrid(results, 'searchResults');
        }
    } catch (error) {
        console.error('Search error:', error);
        showToast('Search failed', 'error');
    }
}

// ==================== LIBRARY ====================
async function loadLibrary() {
    const filter = document.getElementById('libraryFilter').value;
    
    try {
        let data;
        if (filter === 'songs') {
            data = await fetch(`${API_BASE_URL}/music/songs`).then(r => r.json());
            renderSongGrid(data, 'libraryContainer');
        } else if (filter === 'artists') {
            data = await fetch(`${API_BASE_URL}/music/artists`).then(r => r.json());
            renderArtistGrid(data, 'libraryContainer');
        } else if (filter === 'albums') {
            data = await fetch(`${API_BASE_URL}/music/albums`).then(r => r.json());
            renderAlbumGrid(data, 'libraryContainer');
        }
    } catch (error) {
        console.error('Error loading library:', error);
        showToast('Failed to load library', 'error');
    }
}

async function handleLibraryFilter() {
    loadLibrary();
}

// ==================== FAVORITES ====================
async function loadFavorites() {
    try {
        const favorites = await fetch(`${API_BASE_URL}/recommendations/favorites/${currentUser.id}`).then(r => r.json());
        renderSongList(favorites, 'favoritesContainer');
    } catch (error) {
        console.error('Error loading favorites:', error);
        showToast('Failed to load favorites', 'error');
    }
}

async function toggleFavorite(songId, e) {
    e.stopPropagation();
    
    try {
        const isFav = await fetch(`${API_BASE_URL}/recommendations/favorites/${currentUser.id}/${songId}`).then(r => r.json());
        
        const endpoint = isFav.isFavorite ? 'DELETE' : 'POST';
        const response = await fetch(`${API_BASE_URL}/recommendations/favorites${isFav.isFavorite ? `/${currentUser.id}/${songId}` : ''}`, {
            method: endpoint,
            headers: { 'Content-Type': 'application/json' },
            body: endpoint === 'POST' ? JSON.stringify({ userId: currentUser.id, songId }) : undefined
        });
        
        const result = await response.json();
        if (result.success) {
            showToast(result.message, 'success');
            if (document.getElementById('favoritesPage').classList.contains('active')) {
                loadFavorites();
            }
        }
    } catch (error) {
        console.error('Error toggling favorite:', error);
        showToast('Failed to update favorite', 'error');
    }
}

// ==================== PLAYLISTS ====================
async function loadPlaylists() {
    try {
        const playlists = await fetch(`${API_BASE_URL}/playlists/user/${currentUser.id}`).then(r => r.json());
        renderPlaylistGrid(playlists, 'playlistsContainer');
    } catch (error) {
        console.error('Error loading playlists:', error);
        showToast('Failed to load playlists', 'error');
    }
}

function openPlaylistModal() {
    document.getElementById('playlistModal').classList.remove('hidden');
}

function closePlaylistModal() {
    document.getElementById('playlistModal').classList.add('hidden');
    document.getElementById('playlistName').value = '';
    document.getElementById('playlistDescription').value = '';
}

async function createPlaylist() {
    const title = document.getElementById('playlistName').value;
    const description = document.getElementById('playlistDescription').value;
    
    if (!title.trim()) {
        showToast('Please enter a playlist name', 'error');
        return;
    }
    
    try {
        const response = await fetch(`${API_BASE_URL}/playlists`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ userId: currentUser.id, title, description })
        });
        
        const result = await response.json();
        if (result.success) {
            showToast('Playlist created successfully!', 'success');
            closePlaylistModal();
            loadPlaylists();
        } else {
            showToast(result.message, 'error');
        }
    } catch (error) {
        console.error('Error creating playlist:', error);
        showToast('Failed to create playlist', 'error');
    }
}

// ==================== TRENDING ====================
async function loadTrending() {
    try {
        const trending = await fetch(`${API_BASE_URL}/music/trending`).then(r => r.json());
        renderSongList(trending, 'trendingFullContainer');
    } catch (error) {
        console.error('Error loading trending:', error);
        showToast('Failed to load trending songs', 'error');
    }
}

// ==================== RENDERING FUNCTIONS ====================
function renderSongGrid(songs, containerId) {
    const container = document.getElementById(containerId);
    if (!songs || songs.length === 0) {
        container.innerHTML = '<p style="grid-column: 1/-1; text-align: center; color: var(--text-secondary);">No songs found</p>';
        return;
    }
    
    container.innerHTML = songs.map(song => `
        <div class="song-card" onclick="playSong(${song.id})">
            <div class="song-image">
                <img src="${song.cover_image_url || '/images/default-album.svg'}" alt="${song.title}" onerror="this.src='/images/default-album.svg'">
            </div>
            <div class="song-title" title="${song.title}">${song.title}</div>
            <div class="song-artist" title="${song.artist_name}">${song.artist_name || 'Unknown Artist'}</div>
            <div class="song-actions">
                <button class="icon-btn" onclick="toggleFavorite(${song.id}, event)" title="Add to favorites">
                    <i class="far fa-heart"></i>
                </button>
            </div>
        </div>
    `).join('');
}

function renderSongList(songs, containerId) {
    const container = document.getElementById(containerId);
    if (!songs || songs.length === 0) {
        container.innerHTML = '<p style="text-align: center; color: var(--text-secondary);">No songs found</p>';
        return;
    }
    
    container.innerHTML = songs.map(song => `
        <div class="song-item" onclick="playSong(${song.id})">
            <div class="song-item-info">
                <div class="song-item-image">
                    <img src="${song.cover_image_url || '/images/default-album.svg'}" alt="${song.title}" onerror="this.src='/images/default-album.svg'">
                </div>
                <div class="song-item-details">
                    <div class="song-item-title">${song.title}</div>
                    <div class="song-item-artist">${song.artist_name || 'Unknown Artist'}</div>
                </div>
            </div>
            <div class="song-item-actions">
                <div class="song-item-duration">${formatDuration(song.duration)}</div>
                <button class="icon-btn" onclick="toggleFavorite(${song.id}, event)">
                    <i class="far fa-heart"></i>
                </button>
                <button class="icon-btn" onclick="playNow(${song.id}, event)">
                    <i class="fas fa-play"></i>
                </button>
            </div>
        </div>
    `).join('');
}

function renderArtistGrid(artists, containerId) {
    const container = document.getElementById(containerId);
    if (!artists || artists.length === 0) {
        container.innerHTML = '<p style="grid-column: 1/-1; text-align: center; color: var(--text-secondary);">No artists found</p>';
        return;
    }
    
    container.innerHTML = artists.map(artist => `
        <div class="artist-card">
            <div class="artist-image">
                <img src="/images/default-artist.svg" alt="${artist.name}">
            </div>
            <div class="artist-name" title="${artist.name}">${artist.name}</div>
            <div class="song-artist">${artist.genre || 'Unknown Genre'}</div>
        </div>
    `).join('');
}

function renderAlbumGrid(albums, containerId) {
    const container = document.getElementById(containerId);
    if (!albums || albums.length === 0) {
        container.innerHTML = '<p style="grid-column: 1/-1; text-align: center; color: var(--text-secondary);">No albums found</p>';
        return;
    }
    
    container.innerHTML = albums.map(album => `
        <div class="album-card">
            <div class="album-image">
                <img src="/images/default-album.svg" alt="${album.title}">
            </div>
            <div class="album-title" title="${album.title}">${album.title}</div>
            <div class="album-artist" title="${album.artist_name}">${album.artist_name || 'Unknown Artist'}</div>
        </div>
    `).join('');
}

function renderPlaylistGrid(playlists, containerId) {
    const container = document.getElementById(containerId);
    if (!playlists || playlists.length === 0) {
        container.innerHTML = '<p style="grid-column: 1/-1; text-align: center; color: var(--text-secondary);">No playlists yet. Create one!</p>';
        return;
    }
    
    container.innerHTML = playlists.map(playlist => `
        <div class="playlist-card">
            <div class="playlist-image">
                <img src="/images/default-playlist.svg" alt="${playlist.title}">
            </div>
            <div class="playlist-title" title="${playlist.title}">${playlist.title}</div>
            <div class="song-artist">${playlist.description || 'No description'}</div>
        </div>
    `).join('');
}

// ==================== PLAYER FUNCTIONS ====================
async function playSong(songId) {
    try {
        const song = await fetch(`${API_BASE_URL}/music/songs/${songId}`).then(r => r.json());
        currentSong = song;
        currentPlaylist = [song];
        currentPlaylistIndex = 0;
        
        updatePlayerUI();
        play();
    } catch (error) {
        console.error('Error playing song:', error);
        showToast('Failed to play song', 'error');
    }
}

async function playNow(songId, e) {
    e.stopPropagation();
    await playSong(songId);
}

function togglePlay() {
    if (currentSong) {
        isPlaying ? pause() : play();
    }
}

function play() {
    if (!currentSong) return;
    
    const audio = document.getElementById('audioPlayer');
    audio.src = currentSong.file_path || currentSong.filePath;
    audio.play().catch(err => {
        console.error('Error playing audio:', err);
        showToast('Could not play song', 'error');
    });
    
    isPlaying = true;
    document.getElementById('playerPlayBtn').innerHTML = '<i class="fas fa-pause"></i>';
}

function pause() {
    const audio = document.getElementById('audioPlayer');
    audio.pause();
    
    isPlaying = false;
    document.getElementById('playerPlayBtn').innerHTML = '<i class="fas fa-play"></i>';
}

function playNext() {
    if (currentPlaylist.length > 1) {
        currentPlaylistIndex = (currentPlaylistIndex + 1) % currentPlaylist.length;
        currentSong = currentPlaylist[currentPlaylistIndex];
        updatePlayerUI();
        play();
    }
}

function playPrevious() {
    if (currentPlaylist.length > 1) {
        currentPlaylistIndex = (currentPlaylistIndex - 1 + currentPlaylist.length) % currentPlaylist.length;
        currentSong = currentPlaylist[currentPlaylistIndex];
        updatePlayerUI();
        play();
    }
}

function seek(e) {
    const audio = document.getElementById('audioPlayer');
    const progressInput = document.getElementById('playerProgressInput');
    const fillPercentage = (progressInput.value / 100) * 100;
    document.getElementById('playerProgressFill').style.width = fillPercentage + '%';
    
    if (audio.duration) {
        audio.currentTime = (progressInput.value / 100) * audio.duration;
    }
}

function setVolume(e) {
    const audio = document.getElementById('audioPlayer');
    const volume = e.target.value;
    audio.volume = volume / 100;
}

function updatePlayerProgress() {
    const audio = document.getElementById('audioPlayer');
    const progressInput = document.getElementById('playerProgressInput');
    const progressFill = document.getElementById('playerProgressFill');
    const currentTimeEl = document.getElementById('playerCurrentTime');
    
    if (audio.duration) {
        const percentage = (audio.currentTime / audio.duration) * 100;
        progressInput.value = percentage;
        progressFill.style.width = percentage + '%';
        currentTimeEl.textContent = formatDuration(Math.floor(audio.currentTime));
    }
}

function updatePlayerDuration() {
    const audio = document.getElementById('audioPlayer');
    const durationEl = document.getElementById('playerDuration');
    
    if (audio.duration) {
        durationEl.textContent = formatDuration(Math.floor(audio.duration));
    }
}

function updatePlayerUI() {
    if (!currentSong) return;
    
    document.getElementById('playerTrackTitle').textContent = currentSong.title;
    document.getElementById('playerTrackArtist').textContent = currentSong.artist_name || 'Unknown Artist';
    document.getElementById('playerDuration').textContent = formatDuration(currentSong.duration);
    
    // Update album art
    const albumArt = document.getElementById('playerAlbumArt');
    const imageUrl = currentSong.cover_image_url || currentSong.albumImage || '/images/default-album.svg';
    albumArt.src = imageUrl;
    albumArt.onerror = function() {
        this.src = '/images/default-album.svg';
    };
    
    // Reset progress
    document.getElementById('playerProgressInput').value = 0;
    document.getElementById('playerProgressFill').style.width = '0%';
    document.getElementById('playerCurrentTime').textContent = '0:00';
}

// ==================== UTILITY FUNCTIONS ====================
function updateUserInfo() {
    if (currentUser) {
        document.getElementById('userInfo').innerHTML = `
            <div style="font-weight: 600; margin-bottom: var(--spacing-sm);">${currentUser.username}</div>
            <div style="font-size: 0.75rem; color: var(--text-secondary);">${currentUser.user_type === 'artist' ? 'Artist' : 'Listener'}</div>
        `;
    }
}

function updateArtistNavigation() {
    const artistNavItem = document.querySelector('[data-page="artist"]');
    if (!currentUser || currentUser.user_type !== 'artist') {
        artistNavItem?.classList.add('hidden');
    } else {
        artistNavItem?.classList.remove('hidden');
    }
}

function updateDashboardHeader() {
    const header = document.querySelector('#dashboardPage .page-header h2');
    const subtitle = document.querySelector('#dashboardPage .page-header p');
    
    if (currentUser) {
        if (header) header.textContent = `Welcome back, ${currentUser.username}!`;
        if (subtitle) subtitle.textContent = currentUser.user_type === 'artist' 
            ? 'Manage your music and connect with fans' 
            : 'Discover new music and enjoy your favorites';
    } else {
        if (header) header.textContent = 'Welcome to MusicFlow';
        if (subtitle) subtitle.textContent = 'Your favorite music is just a click away';
    }
}

function formatDuration(seconds) {
    if (!seconds) return '0:00';
    const mins = Math.floor(seconds / 60);
    const secs = seconds % 60;
    return `${mins}:${secs.toString().padStart(2, '0')}`;
}

function showToast(message, type = 'success') {
    const toast = document.getElementById('toast');
    toast.textContent = message;
    toast.className = `toast ${type} show`;
    
    setTimeout(() => {
        toast.classList.remove('show');
    }, 3000);
}

function toggleTheme() {
    const isDarkMode = !document.body.classList.contains('light-mode');
    document.body.classList.toggle('light-mode', isDarkMode);
    localStorage.setItem('theme', isDarkMode ? 'light' : 'dark');
    
    const icon = document.querySelector('#themeToggle i');
    icon.className = isDarkMode ? 'fas fa-sun' : 'fas fa-moon';
}

function closeSongModal() {
    document.getElementById('songModal').classList.add('hidden');
}

// ==================== ARTIST FUNCTIONS ====================
async function loadArtistDashboard() {
    try {
        const songs = await fetch(`${API_BASE_URL}/music/user/${currentUser.id}`).then(r => r.json());
        renderArtistSongs(songs, 'artistSongsContainer');
    } catch (error) {
        console.error('Error loading artist dashboard:', error);
        showToast('Failed to load your songs', 'error');
    }
}

function openUploadModal() {
    if (currentUser?.user_type !== 'artist') {
        showToast('Only artists can upload songs', 'error');
        return;
    }
    document.getElementById('uploadSongModal').classList.remove('hidden');
}

function closeUploadModal() {
    document.getElementById('uploadSongModal').classList.add('hidden');
    document.getElementById('uploadSongForm').reset();
}

async function uploadSong() {
    const title = document.getElementById('uploadSongTitle').value;
    const artistName = document.getElementById('uploadArtistName').value;
    const albumTitle = document.getElementById('uploadAlbumTitle').value;
    const genre = document.getElementById('uploadGenre').value;
    const songFile = document.getElementById('uploadSongFile').files[0];
    const imageFile = document.getElementById('uploadAlbumImage').files[0];
    
    if (!title || !artistName || !albumTitle || !genre || !songFile || !imageFile) {
        showToast('Please fill all fields', 'error');
        return;
    }
    
    try {
        const formData = new FormData();
        formData.append('title', title);
        formData.append('artistName', artistName);
        formData.append('albumTitle', albumTitle);
        formData.append('genre', genre);
        formData.append('userId', currentUser.id);
        formData.append('songFile', songFile);
        formData.append('imageFile', imageFile);
        
        const response = await fetch(`${API_BASE_URL}/music/upload`, {
            method: 'POST',
            body: formData
        });
        
        const result = await response.json();
        
        if (result.success) {
            showToast('Song uploaded successfully!', 'success');
            closeUploadModal();
            loadArtistDashboard();
        } else {
            showToast(result.message || 'Upload failed', 'error');
        }
    } catch (error) {
        console.error('Error uploading song:', error);
        showToast('Upload failed: ' + error.message, 'error');
    }
}

function renderArtistSongs(songs, containerId) {
    const container = document.getElementById(containerId);
    if (!songs || songs.length === 0) {
        container.innerHTML = '<p style="text-align: center; color: var(--text-secondary);">No songs uploaded yet. Start by uploading your first song!</p>';
        return;
    }
    
    container.innerHTML = songs.map(song => `
        <div class="song-item">
            <div class="song-item-info">
                <div class="song-item-image">
                    <img src="${song.albumImage || '/images/default-album.svg'}" alt="${song.albumTitle}">
                </div>
                <div class="song-item-details">
                    <div class="song-item-title">${song.title}</div>
                    <div class="song-item-artist">${song.artistName}</div>
                    <div class="song-item-artist" style="font-size: 0.8rem; color: var(--text-secondary);">${song.albumTitle}</div>
                </div>
            </div>
            <div class="song-item-actions">
                <button class="icon-btn" onclick="deleteSong(${song.id}, event)" title="Delete">
                    <i class="fas fa-trash"></i>
                </button>
            </div>
        </div>
    `).join('');
}

async function deleteSong(songId, e) {
    e.stopPropagation();
    
    if (!confirm('Are you sure you want to delete this song?')) {
        return;
    }
    
    try {
        const response = await fetch(`${API_BASE_URL}/music/songs/${songId}`, {
            method: 'DELETE'
        });
        
        const result = await response.json();
        if (result.success) {
            showToast('Song deleted successfully', 'success');
            loadArtistDashboard();
        } else {
            showToast(result.message || 'Delete failed', 'error');
        }
    } catch (error) {
        console.error('Error deleting song:', error);
        showToast('Delete failed: ' + error.message, 'error');
    }
}

// Close modals when clicking outside
document.addEventListener('click', function(e) {
    const playlistModal = document.getElementById('playlistModal');
    const songModal = document.getElementById('songModal');
    const uploadModal = document.getElementById('uploadSongModal');
    
    if (e.target === playlistModal) {
        closePlaylistModal();
    }
    if (e.target === songModal) {
        closeSongModal();
    }
    if (e.target === uploadModal) {
        closeUploadModal();
    }
});

// Keyboard shortcuts
document.addEventListener('keydown', function(e) {
    if (e.code === 'Space') {
        e.preventDefault();
        togglePlay();
    }
});
