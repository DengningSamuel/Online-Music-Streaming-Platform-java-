# MusicFlow - Quick Start Guide

Get your music streaming platform up and running in minutes!

## ⚡ Quick Start (5 Minutes)

### Step 1: Database Setup
```bash
# Using PostgreSQL
psql -U postgres
CREATE DATABASE users;
\c users
```

Then run the schema:
```bash
psql -U postgres -d users -f database/database_schema.sql
```

**If you're using the existing database with the 4 existing users, you're good to go!**

### Step 2: Configure Database Connection

Edit `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/users
spring.datasource.username=postgres
spring.datasource.password=qwertyuiop  # Your PostgreSQL password
```

### Step 3: Run the Application

**Option A: Using Maven**
```bash
cd "Online-Music-Streaming-Platform-java-"
mvn spring-boot:run
```

**Option B: Build and Run JAR**
```bash
mvn clean install
java -jar target/music-streaming-platform-1.0.0.jar
```

### Step 4: Open in Browser
Navigate to: **http://localhost:8080**

---

## 🔐 Test Login Credentials

Use any of the existing users from the database:
- Username: `Jeff Clinton TC` / Email: `jeffclinton@gmail.com`
- Username: `JCTC` / Email: `JCTC@gmail.com`
- Username: `JEFF` / Email: `JEFF@234.com`
- Username: `jj Thompson` / Email: `jjthompson@gmail.co`

**Password**: Any password works for testing (basic auth for demo)

---

## 📱 Key Features to Try

1. **Search** - Type in the search bar to find songs/artists
2. **Play Songs** - Click any song to play it in the player
3. **Create Playlist** - Use the "New Playlist" button in Playlists
4. **Add to Favorites** - Click the heart icon on any song
5. **Theme Toggle** - Click the moon icon in top right
6. **Browse** - Check out Library, Trending, and Favorites sections

---

## 🛠 Project Structure Overview

```
├── src/main/java/com/musicstreaming/
│   └── MusicStreamingApplication.java     # Main Spring Boot app
│       └── controller/                     # REST API Controllers
├── src/main/resources/
│   ├── static/
│   │   ├── index.html                      # Frontend UI
│   │   ├── css/styles.css                  # Styling
│   │   └── js/app.js                       # Frontend logic
│   └── application.properties              # Configuration
├── src/DAO/                                # Data access layer
├── src/SERVICES/                           # Business logic layer
├── src/MODEL/                              # Domain models
└── database/
    └── database_schema.sql                 # Complete DB schema
```

---

## 🎯 API Endpoints Summary

**Base URL**: `http://localhost:8080/api`

### Auth
- `POST /auth/register` - Create account
- `POST /auth/login` - Login

### Music
- `GET /music/songs` - All songs
- `GET /music/search/songs?query=...` - Search
- `GET /music/artists` - All artists
- `GET /music/albums` - All albums
- `GET /music/trending` - Popular songs

### Playlists
- `GET /playlists/user/{userId}` - User playlists
- `POST /playlists` - Create playlist
- `POST /playlists/{playlistId}/songs` - Add song
- `DELETE /playlists/{playlistId}` - Delete

### Recommendations
- `POST /recommendations/favorites` - Add favorite
- `GET /recommendations/favorites/{userId}` - Get favorites
- `GET /recommendations/trending` - Trending

---

## 🐛 Troubleshooting

| Issue | Solution |
|-------|----------|
| Port 8080 in use | Change `server.port` in `application.properties` |
| Database connection error | Check PostgreSQL is running and credentials match |
| Can't find songs after login | Run `database_schema.sql` to populate sample data |
| Frontend not loading | Clear browser cache and refresh |
| CORS errors | Check `CorsConfig` in `MusicStreamingApplication.java` |

---

## 📊 Sample Data

The database includes:
- **3 Artists**: The Beatles, Taylor Swift, Ed Sheeran
- **3 Albums**: Abbey Road, Folklore, ÷
- **3 Songs**: Come Together, Cardigan, Shape of You
- **4 Users**: Pre-created test accounts

---

## 🚀 Next Steps

1. **Add More Data** - Use the admin endpoints or directly insert into PostgreSQL
2. **Upload Songs** - Implement file upload in the backend
3. **Stream Audio** - Integrate audio player library
4. **Deploy** - Use Docker or cloud platforms (AWS, Heroku, etc.)

---

## 📚 Documentation

- **Full Setup Guide**: See `SETUP_GUIDE.md`
- **Database Schema**: See `database/database_schema.sql`
- **API Documentation**: Available in controllers

---

## 💡 Tips

- **Keyboard Shortcut**: Press `Space` to play/pause
- **Theme**: Click moon icon (top right) to switch themes
- **Search**: Works for songs, artists, and albums simultaneously
- **Local Storage**: Your login is saved in browser

---

## ⚙️ System Requirements

- **Java**: 17+
- **PostgreSQL**: 12+
- **Browser**: Modern browser (Chrome, Firefox, Safari, Edge)
- **RAM**: 512MB minimum
- **Disk**: 100MB free space

---

## 🔗 Technology Stack

- **Backend**: Spring Boot 3.1.5 + PostgreSQL
- **Frontend**: HTML5 + CSS3 + Vanilla JavaScript
- **Build**: Maven
- **Database**: PostgreSQL 17.9

---

## 📝 License

MIT License - Free to use and modify

---

**Stuck?** Check the full `SETUP_GUIDE.md` for detailed instructions!

**Happy Streaming! 🎵**
