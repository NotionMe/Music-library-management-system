package ua.notion.musiclibrary.test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import ua.notion.musiclibrary.domain.enums.Role;
import ua.notion.musiclibrary.domain.model.*;
import ua.notion.musiclibrary.domain.repository.*;
import ua.notion.musiclibrary.domain.repository.json.UnitOfWork;

public class LibraryApplication {
    private final UserRepository userRepository;
    private final GenreRepository genreRepository;
    private final ArtistRepository artistRepository;
    private final AlbumRepository albumRepository;
    private final TrackRepository trackRepository;
    private final PlaylistRepository playlistRepository;
    private final FavoriteRepository favoriteRepository;
    private final ListeningHistoryRepository listeningHistoryRepository;
    private final UserCollectionRepository userCollectionRepository;

    public LibraryApplication(
            UserRepository userRepository,
            GenreRepository genreRepository,
            ArtistRepository artistRepository,
            AlbumRepository albumRepository,
            TrackRepository trackRepository,
            PlaylistRepository playlistRepository,
            FavoriteRepository favoriteRepository,
            ListeningHistoryRepository listeningHistoryRepository,
            UserCollectionRepository userCollectionRepository) {
        this.userRepository = userRepository;
        this.genreRepository = genreRepository;
        this.artistRepository = artistRepository;
        this.albumRepository = albumRepository;
        this.trackRepository = trackRepository;
        this.playlistRepository = playlistRepository;
        this.favoriteRepository = favoriteRepository;
        this.listeningHistoryRepository = listeningHistoryRepository;
        this.userCollectionRepository = userCollectionRepository;
    }

    public void run() {
        System.out.println("=== ПОЧАТОК РОБОТИ ===");

        createSampleData();

        demonstrateCapabilities();

        // 3. Демонстрація UnitOfWork
        demonstrateUnitOfWork();

        System.out.println("=== ЗАВЕРШЕННЯ РОБОТИ ===");
    }

    private void createSampleData() {
        createUsers();
        createGenres();
        createArtists();
        createAlbums();
        createTracks();
        createPlaylists();
        createFavorites();
        createHistory();
        createCollections();
    }

    private void demonstrateCapabilities() {
        demonstrateUserCapabilities();
        demonstrateGenreCapabilities();
        demonstrateArtistCapabilities();
        demonstrateAlbumCapabilities();
        demonstrateTrackCapabilities();
        demonstratePlaylistCapabilities();
        demonstrateFavoriteCapabilities();
        demonstrateHistoryCapabilities();
        demonstrateCollectionCapabilities();
    }

    // --- USERS ---
    private void createUsers() {
        System.out.println("\n--- Створення користувачів (через UnitOfWork) ---");
        if (userRepository.count() == 0) {
            UnitOfWork<User, UUID> uow = new UnitOfWork<>(userRepository, User::getID);
            
            uow.registerNew(new User("john_doe", "john@example.com", "password123", Role.USER));
            uow.registerNew(new User("admin_jane", "jane@example.com", "adminpass", Role.ADMIN));
            
            System.out.println("Стан перед комітом: " + uow.getChangesSummary());
            uow.commit();
            System.out.println("Збережено користувачів.");
        }
    }

    private void demonstrateUserCapabilities() {
        System.out.println("\n=== User Repository Capabilities ===");
        userRepository.findByUsername("john_doe").ifPresent(u -> System.out.println("Found by username: " + u));
        System.out.println("Admin users: " + userRepository.findByRole(Role.ADMIN).size());
    }

    // --- GENRES ---
    private void createGenres() {
        System.out.println("\n--- Створення жанрів (через UnitOfWork) ---");
        if (genreRepository.count() == 0) {
            UnitOfWork<Genre, UUID> uow = new UnitOfWork<>(genreRepository, Genre::getID);
            
            uow.registerNew(new Genre("Rock"));
            uow.registerNew(new Genre("Pop"));
            uow.registerNew(new Genre("Jazz"));
            
            System.out.println("Стан перед комітом: " + uow.getChangesSummary());
            uow.commit();
            System.out.println("Збережено жанри.");
        }
    }

    private void demonstrateGenreCapabilities() {
        System.out.println("\n=== Genre Repository Capabilities ===");
        genreRepository.findByName("Rock").forEach(g -> System.out.println("Found genre: " + g.getName()));
    }

    // --- ARTISTS ---
    private void createArtists() {
        System.out.println("\n--- Створення артистів (через UnitOfWork) ---");
        if (artistRepository.count() == 0) {
            UnitOfWork<Artist, UUID> uow = new UnitOfWork<>(artistRepository, Artist::getID);
            User user = userRepository.findByRole(Role.USER).stream().findFirst().orElseThrow();
            
            Artist artist = new Artist("The Beatles", "Legendary British rock band", user.getID());
            uow.registerNew(artist);
            
            uow.commit();
            System.out.println("Збережено артиста: " + artist.getStageName());
        }
    }

    private void demonstrateArtistCapabilities() {
        System.out.println("\n=== Artist Repository Capabilities ===");
        artistRepository.findByStageName("The Beatles")
                .forEach(a -> System.out.println("Found artist: " + a.getStageName()));
    }

    // --- ALBUMS ---
    private void createAlbums() {
        System.out.println("\n--- Створення альбомів (через UnitOfWork) ---");
        if (albumRepository.count() == 0) {
            UnitOfWork<Album, UUID> uow = new UnitOfWork<>(albumRepository, Album::getID);
            
            artistRepository.findAll().stream().findFirst().ifPresent(artist -> {
                Album album = new Album("Abbey Road", LocalDate.of(1969, 9, 26), artist.getID());
                uow.registerNew(album);
                System.out.println("Альбом додано в UoW: " + album.getTitle());
            });
            
            uow.commit();
            System.out.println("Зміни збережено.");
        }
    }

    private void demonstrateAlbumCapabilities() {
        System.out.println("\n=== Album Repository Capabilities ===");
        albumRepository.findByTitleContaining("Road").forEach(a -> System.out.println("Found album: " + a.getTitle()));
    }

    // --- TRACKS ---
    private void createTracks() {
        System.out.println("\n--- Створення треків (через UnitOfWork) ---");
        if (trackRepository.count() == 0) {
            UnitOfWork<Track, UUID> uow = new UnitOfWork<>(trackRepository, Track::getID);
            
            Album album = albumRepository.findAll().stream().findFirst().orElseThrow();
            Genre genre = genreRepository.findAll().stream().findFirst().orElseThrow();
            Artist artist = artistRepository.findAll().stream().findFirst().orElseThrow();
            
            Track track1 = new Track("Come Together", Duration.ofMinutes(4).plusSeconds(19), album.getID());
            track1.addGenre(genre.getID());
            track1.addArtist(artist.getID());
            
            Track track2 = new Track("Something", Duration.ofMinutes(3), album.getID());
            track2.addGenre(genre.getID());
            track2.addArtist(artist.getID());

            uow.registerNew(track1);
            uow.registerNew(track2);
            
            System.out.println("Треки підготовлені: " + uow.getChangesSummary());
            uow.commit();
            System.out.println("Треки збережені.");
        }
    }

    private void demonstrateTrackCapabilities() {
        System.out.println("\n=== Track Repository Capabilities ===");
        trackRepository.findByTitleContainingIgnoreCase("Come")
                .forEach(t -> System.out.println("Found track: " + t.getTitle()));
    }

    // --- PLAYLISTS ---
    private void createPlaylists() {
        System.out.println("\n--- Створення плейлистів (через UnitOfWork) ---");
        if (playlistRepository.count() == 0) {
            UnitOfWork<Playlist, UUID> uow = new UnitOfWork<>(playlistRepository, Playlist::getID);
            
            User user = userRepository.findAll().stream().findFirst().orElseThrow();
            Track track = trackRepository.findAll().stream().findFirst().orElseThrow();

            Playlist playlist = new Playlist("My Favorites", false, user.getID());
            playlist.addTrack(track.getID());
            
            uow.registerNew(playlist);
            uow.commit();
            System.out.println("Збережено плейлист: " + playlist.getName());
        }
    }

    private void demonstratePlaylistCapabilities() {
        System.out.println("\n=== Playlist Repository Capabilities ===");
        playlistRepository.findByNameContainingIgnoreCase("Favorites")
                .forEach(p -> System.out.println("Found playlist: " + p.getName()));
    }

    // --- FAVORITES (LIKED SONGS) ---
    private void createFavorites() {
        System.out.println("\n--- Лайки (через UnitOfWork) ---");
        User user = userRepository.findAll().stream().findFirst().orElseThrow();
        if (user.getLikedTrackIds().isEmpty()) {
            UnitOfWork<User, UUID> uow = new UnitOfWork<>(userRepository, User::getID);
            Track track = trackRepository.findAll().stream().findFirst().orElseThrow();
            
            user.likeTrack(track.getID());
            uow.registerDirty(user);
            
            uow.commit();
            System.out.println("Збережено лайк користувача: " + user.getUsername());
        }
    }

    private void demonstrateFavoriteCapabilities() {
        System.out.println("\n=== Liked Songs Capabilities ===");
        User user = userRepository.findAll().stream().findFirst().orElseThrow();
        System.out.println("Liked songs count: " + user.getLikedTrackIds().size());
    }

    // --- HISTORY ---
    private void createHistory() {
        System.out.println("\n--- Створення історії (через UnitOfWork) ---");
        if (listeningHistoryRepository.count() == 0) {
            UnitOfWork<ListeningHistory, UUID> uow = new UnitOfWork<>(listeningHistoryRepository, ListeningHistory::getID);
            
            User user = userRepository.findAll().stream().findFirst().orElseThrow();
            Track track = trackRepository.findAll().stream().findFirst().orElseThrow();

            ListeningHistory history = new ListeningHistory(user.getID(), track.getID(), LocalDateTime.now());
            uow.registerNew(history);
            uow.commit();
            System.out.println("Збережено запис історії");
        }
    }

    private void demonstrateHistoryCapabilities() {
        System.out.println("\n=== History Repository Capabilities ===");
        User user = userRepository.findAll().stream().findFirst().orElseThrow();
        System.out.println("History records for user: " + listeningHistoryRepository.findByUserId(user.getID()).size());
    }

    // --- COLLECTIONS (USER LIBRARY) ---
    private void createCollections() {
        System.out.println("\n--- Бібліотека користувача (через UnitOfWork) ---");
        User user = userRepository.findAll().stream().findFirst().orElseThrow();
        
        UnitOfWork<User, UUID> uow = new UnitOfWork<>(userRepository, User::getID);
        
        // Follow Artist
        Artist artist = artistRepository.findAll().stream().findFirst().orElseThrow();
        user.followArtist(artist.getID());
        System.out.println("User followed artist: " + artist.getStageName());

        // Follow Playlist
        Playlist playlist = playlistRepository.findAll().stream().findFirst().orElseThrow();
        user.followPlaylist(playlist.getID());
        System.out.println("User followed playlist: " + playlist.getName());

        uow.registerDirty(user);
        uow.commit();
        System.out.println("Зміни профілю користувача збережено.");
    }

    private void demonstrateCollectionCapabilities() {
        System.out.println("\n=== User Library Capabilities ===");
        User user = userRepository.findAll().stream().findFirst().orElseThrow();
        System.out.println("Followed artists: " + user.getFollowedArtistIds().size());
        System.out.println("Followed playlists: " + user.getFollowedPlaylistIds().size());
    }

    private void demonstrateUnitOfWork() {
        System.out.println("\n=== UnitOfWork Demonstration ===");
        
        // Створюємо UnitOfWork для треків
        UnitOfWork<Track, UUID> trackUoW = new UnitOfWork<>(trackRepository, Track::getID);
        Album album = albumRepository.findAll().stream().findFirst().orElseThrow();

        // 1. Реєстрація нових об'єктів (Register New)
        Track t1 = new Track("UoW Track 1", Duration.ofMinutes(1), album.getID());
        Track t2 = new Track("UoW Track 2", Duration.ofMinutes(2), album.getID());

        trackUoW.registerNew(t1);
        trackUoW.registerNew(t2);
        System.out.println("Added 2 tracks to UoW. Summary: " + trackUoW.getChangesSummary());

        // 2. Commit (Збереження змін)
        trackUoW.commit();
        System.out.println("Committed changes.");

        // Перевірка
        long count = trackRepository.findAll().stream()
                .filter(t -> t.getTitle().startsWith("UoW Track"))
                .count();
        System.out.println("Tracks found in repository: " + count);
        
        // 3. Модифікація та видалення (Dirty & Deleted)
        t1.setTitle("UoW Track 1 Modified");
        trackUoW.registerDirty(t1);
        trackUoW.registerDeleted(t2);
        
        System.out.println("Modified 1 and Deleted 1. Summary: " + trackUoW.getChangesSummary());
        trackUoW.commit();
        System.out.println("Committed update/delete.");
        
        // Фінальна перевірка
        trackRepository.findById(t1.getID()).ifPresent(t -> System.out.println("Track 1 title: " + t.getTitle()));
        boolean t2Exists = trackRepository.existsById(t2.getID());
        System.out.println("Track 2 exists: " + t2Exists);
    }
}
