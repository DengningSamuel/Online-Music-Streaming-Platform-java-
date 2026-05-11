# Online Music Streaming Platform - Complete Project

A full-featured music streaming platform built with Java backend and HTML/CSS/JavaScript frontend.

## Project Overview

This project is a complete music streaming application with:
- **Backend**: Spring Boot REST API with PostgreSQL database
- **Frontend**: Single-page application (SPA) with modern UI
- **Features**: User authentication, playlist management, song search, favorites, trending songs

## Tech Stack

### Backend
- **Framework**: Spring Boot 3.1.5
- **Database**: PostgreSQL 17.9
- **Language**: Java 17
- **Build Tool**: Maven

### Frontend
- **HTML5**
- **CSS3** (with custom variables and responsive design)
- **JavaScript (ES6+)**
- **Fetch API for REST calls**

## Project Structure

```
Online-Music-Streaming-Platform-java/
├── src/
│   ├── main/
│   │   ├── java/com/musicstreaming/
│   │   │   ├── MusicStreamingApplication.java (Main Spring Boot App)
│   │   │   └── controller/
│   │   │       ├── UserController.java
│   │   │       ├── MusicController.java
│   │   │       ├── PlaylistController.java
│   │   │       └── RecommendationController.java
│   │   ├── resources/
│   │   │   ├── application.properties
│   │   │   ├── static/
│   │   │   │   ├── index.html
│   │   │   │   ├── css/styles.css
│   │   │   │   ├── js/app.js
│   │   │   │   └── images/
│   │   │   └── templates/
│   ├── DAO/
│   │   ├── DBCONNECT.java
│   │   ├── UserDAO.java
│   │   ├── SongDAO.java
│   │   ├── ArtistDAO.java
│   │   ├── AlbumDAO.java
│   │   ├── PlaylistDAO.java
│   │   ├── FavoriteDAO.java
│   │   └── RecommendationDAO.java
│   ├── MODEL/
│   │   ├── User.java
│   │   ├── Song.java
│   │   ├── Artist.java
│   │   ├── Album.java
│   │   ├── Playlist.java
│   │   ├── Track.java
│   │   ├── Subscription.java
│   │   └── Recommendation.java
│   ├── SERVICES/
│   │   ├── UserService.java
│   │   ├── MusicService.java
│   │   ├── PlaylistService.java
│   │   └── RecommendationService.java
│   └── Main.java (Legacy CLI version)
├── database/
│   ├── database.sql (Legacy)
│   └── database_schema.sql (Complete schema)
├── pom.xml
└── README.md
```

## API Endpoints

### Authentication
- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - Login user
- `GET /api/auth/profile/{userId}` - Get user profile

### Music Library
- `GET /api/music/songs` - Get all songs
- `GET /api/music/songs/{songId}` - Get song details
- `GET /api/music/search/songs?query=...` - Search songs
- `GET /api/music/artists` - Get all artists
- `GET /api/music/artists/{artistId}` - Get artist details
- `GET /api/music/artists/genre/{genre}` - Get artists by genre
- `GET /api/music/albums` - Get all albums
- `GET /api/music/albums/{albumId}` - Get album details
- `GET /api/music/trending` - Get trending songs

### Playlists
- `GET /api/playlists/user/{userId}` - Get user playlists
- `POST /api/playlists` - Create playlist
- `GET /api/playlists/{playlistId}` - Get playlist details
- `POST /api/playlists/{playlistId}/songs` - Add song to playlist
- `DELETE /api/playlists/{playlistId}/songs/{songId}` - Remove song from playlist
- `PUT /api/playlists/{playlistId}` - Update playlist
- `DELETE /api/playlists/{playlistId}` - Delete playlist

### Recommendations & Favorites
- `POST /api/recommendations/favorites` - Add favorite
- `DELETE /api/recommendations/favorites/{userId}/{songId}` - Remove favorite
- `GET /api/recommendations/favorites/{userId}` - Get user favorites
- `GET /api/recommendations/{userId}` - Get recommendations
- `GET /api/recommendations/smart/{userId}` - Get smart recommendations
- `GET /api/recommendations/trending` - Get trending songs

## Getting Started

### Prerequisites
- Java 17 or higher
- PostgreSQL 17.9
- Maven 3.6+
- Node.js (optional, for frontend build tools)

### Installation Steps

1. **Clone/Download the project**
   ```bash
   cd Online-Music-Streaming-Platform-java-
   ```

2. **Setup PostgreSQL Database**
   ```bash
   # Create database
   createdb users
   
   # Run schema initialization
   psql -U postgres -d users -f database/database_schema.sql
   ```

3. **Update Database Configuration**
   Edit `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/users
   spring.datasource.username=postgres
   spring.datasource.password=your_password
   ```

4. **Build the Project**
   ```bash
   mvn clean install
   ```

5. **Run the Application**
   ```bash
   mvn spring-boot:run
   ```
   Or after building:
   ```bash
   java -jar target/music-streaming-platform-1.0.0.jar
   ```

6. **Access the Application**
   Open your browser and navigate to: `http://localhost:8080`

## Features

### User Management
- User registration and login
- User profiles
- Subscription management

### Music Library
- Browse all songs, artists, and albums
- Search functionality
- Advanced filtering

### Playlist Management
- Create custom playlists
- Add/remove songs from playlists
- Edit playlist details
- Delete playlists

### Music Discovery
- Trending songs section
- Personalized recommendations
- Genre-based browsing

### Favorites
- Mark songs as favorites
- View favorite collection
- Quick access to liked songs

### Music Player
- Play, pause, skip controls
- Progress tracking
- Volume control
- Current song display

### User Experience
- Dark/Light theme toggle
- Responsive design
- Toast notifications
- Keyboard shortcuts (Space to play/pause)

## Database Schema

### Main Tables
- **users** - User accounts and authentication
- **artists** - Artist information
- **albums** - Album collections
- **songs** - Song library
- **tracks** - Track metadata
- **playlists** - User playlists
- **playlist_songs** - Playlist-song relationships
- **subscriptions** - User subscriptions
- **recommendations** - Personalized recommendations
- **user_favorites** - Favorite songs
- **playback_history** - User listening history

## Sample Data

The database schema includes sample data for testing:
- 3 Sample Artists (The Beatles, Taylor Swift, Ed Sheeran)
- 3 Sample Albums
- 3 Sample Songs

## Configuration

### Environment Variables
No environment variables required. All configuration is in `application.properties`.

### Backend Configuration
```properties
# Server
server.port=8080

# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/users
spring.datasource.username=postgres
spring.datasource.password=qwertyuiop

# JPA
spring.jpa.hibernate.ddl-auto=update
```

## API Request Examples

### Register User
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "email": "john@example.com",
    "password": "password123"
  }'
```

### Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@example.com",
    "password": "password123"
  }'
```

### Search Songs
```bash
curl http://localhost:8080/api/music/search/songs?query=Beatles
```

### Create Playlist
```bash
curl -X POST http://localhost:8080/api/playlists \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "title": "My Favorites",
    "description": "My favorite songs"
  }'
```

## Frontend Features

### Pages
1. **Dashboard** - Home page with trending and recent songs
2. **Search** - Search for songs, artists, and albums
3. **Library** - Browse full music collection
4. **Favorites** - Access favorite songs
5. **Playlists** - Manage user playlists
6. **Trending** - View popular songs

### UI Components
- Responsive sidebar navigation
- Top search bar
- Music player with controls
- Song/album/artist cards
- Modal dialogs
- Toast notifications
- Theme toggle

## Development

### Building from Source
```bash
# Clean build
mvn clean compile

# With tests
mvn clean test

# Package JAR
mvn clean package

# Run tests
mvn test
```

### Debugging
Enable debug logging in `application.properties`:
```properties
logging.level.com.musicstreaming=DEBUG
logging.level.org.springframework.web=DEBUG
```

## Troubleshooting

### Database Connection Issues
- Verify PostgreSQL is running
- Check connection credentials in `application.properties`
- Ensure database exists: `psql -U postgres -l | grep users`

### Port Already in Use
```bash
# Change port in application.properties
server.port=8081
```

### Build Failures
```bash
# Clear Maven cache
mvn clean install -U
```

## Future Enhancements

- Audio file upload and streaming
- Social features (follow users, share playlists)
- Advanced recommendation algorithm
- Real-time notifications
- Mobile app
- Analytics and statistics
- Collaborative playlists
- Voice search
- Lyrics display
- Integration with external music providers

## Contributing

1. Fork the repository
2. Create feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit changes (`git commit -m 'Add AmazingFeature'`)
4. Push to branch (`git push origin feature/AmazingFeature`)
5. Open Pull Request

## License

This project is licensed under the MIT License - see LICENSE file for details.

## Authors

- Jeff Clinton - Initial project development

## Support

For issues and questions, please create an issue in the project repository.

## Acknowledgments

- Spring Boot team for excellent framework
- PostgreSQL for reliable database
- Contributor community

## Changelog

### Version 1.0.0 (Current)
- Initial release
- Core functionality implemented
- REST API endpoints
- Frontend SPA
- Database schema and DAOs
- Service layer

---

**Last Updated**: May 11, 2026
**Status**: Active Development
