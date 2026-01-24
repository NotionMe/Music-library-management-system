package ua.notion.musiclibrary.test;

import java.time.Duration;
import java.time.ZoneId;
import java.util.List;
import java.util.concurrent.TimeUnit;

import net.datafaker.Faker;
import ua.notion.musiclibrary.domain.enums.Role;
import ua.notion.musiclibrary.domain.impl.Album;
import ua.notion.musiclibrary.domain.impl.Artist;
import ua.notion.musiclibrary.domain.impl.Genre;
import ua.notion.musiclibrary.domain.impl.ListeningHistory;
import ua.notion.musiclibrary.domain.impl.Playlist;
import ua.notion.musiclibrary.domain.impl.Track;
import ua.notion.musiclibrary.domain.impl.User;
import ua.notion.musiclibrary.infrastructure.storage.impl.DataContext;

public class LibraryApplication {
    private final DataContext context;
    private final Faker faker;

    public LibraryApplication(DataContext context) {
        this.context = context;
        this.faker = new Faker();
    }

    public void run() {
        System.out.println("=== ПОЧАТОК РОБОТИ ===");

        createSampleData();
        demonstrateCapabilities();

        System.out.println("=== ЗАВЕРШЕННЯ РОБОТИ ===");
    }

    private void createSampleData() {
        createUsers();
        createGenres();
        context.commit(); // Commit to ensure artists can find users and genres

        createArtists();
        context.commit(); // Commit to ensure albums can find artists

        createAlbums();
        context.commit(); // Commit to ensure tracks can find albums

        createTracks();
        context.commit(); // Commit for playlists and history

        createPlaylists();
        createFavorites();
        createHistory();
        createCollections();
        context.commit();

        System.out.println("\nСтатус змін: " + context.getChangesSummary());
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
        System.out.println("\n--- Створення користувачів ---");
        if (context.users().count() == 0) {
            for (int i = 0; i < 5; i++) {
                User user = new User(
                        "testuser" + i,
                        faker.internet().emailAddress(),
                        "StrongPass123!",
                        i == 0 ? Role.ADMIN : Role.USER);
                context.registerNew(user);
            }
            System.out.println("Зареєстровано нових користувачів.");
        }
    }

    private void demonstrateUserCapabilities() {
        System.out.println("\n=== User Repository Capabilities ===");
        context.users().findAll().stream().findFirst().ifPresent(u -> {
            System.out.println("First user: " + u.getUsername());
            context.users().findByUsername(u.getUsername())
                    .ifPresent(found -> System.out.println("Found by username: " + found));
        });
        System.out.println("Admin users: " + context.users().findByRole(Role.ADMIN).size());
    }

    // --- GENRES ---
    private void createGenres() {
        System.out.println("\n--- Створення жанрів ---");
        if (context.genres().count() == 0) {
            for (int i = 0; i < 5; i++) {
                context.registerNew(new Genre(faker.music().genre()));

            }
            System.out.println("Зареєстровано нові жанри.");
        }
    }

    private void demonstrateGenreCapabilities() {
        System.out.println("\n=== Genre Repository Capabilities ===");
        context.genres().findAll().stream().findFirst().ifPresent(g -> {
            System.out.println("Sample genre: " + g.getName());
            context.genres().findByName(g.getName())
                    .forEach(found -> System.out.println("Found genre: " + found.getName()));
        });
    }

    // --- ARTISTS ---
    private void createArtists() {
        System.out.println("\n--- Створення артистів ---");
        if (context.artists().count() == 0) {
            List<User> users = context.users().findAll();
            if (!users.isEmpty()) {
                for (int i = 0; i < 3; i++) {
                    User user = users.get(faker.random().nextInt(users.size()));
                    Artist artist = new Artist(
                            faker.artist().name(),
                            faker.lorem().sentence(),
                            user.getID());
                    context.registerNew(artist);
                }
                System.out.println("Зареєстровано нових артистів.");
            }
        }
    }

    private void demonstrateArtistCapabilities() {
        System.out.println("\n=== Artist Repository Capabilities ===");
        context.artists().findAll().stream().findFirst().ifPresent(a -> {
            System.out.println("Sample artist: " + a.getStageName());
            context.artists().findByStageName(a.getStageName())
                    .forEach(found -> System.out.println("Found artist: " + found.getStageName()));
        });
    }

    // --- ALBUMS ---
    private void createAlbums() {
        System.out.println("\n--- Створення альбомів ---");
        if (context.albums().count() == 0) {
            List<Artist> artists = context.artists().findAll();
            for (Artist artist : artists) {
                for (int i = 0; i < 2; i++) {
                    Album album = new Album(
                            faker.expression("#{music.genre} Album #{code.asin}"),
                            faker.date().past(3650, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault())
                                    .toLocalDate(),
                            artist.getID());
                    context.registerNew(album);
                }
            }
            System.out.println("Зареєстровано нові альбоми.");
        }
    }

    private void demonstrateAlbumCapabilities() {
        System.out.println("\n=== Album Repository Capabilities ===");
        context.albums().findAll().stream().findFirst().ifPresent(a -> {
            String partialTitle = a.getTitle().substring(0, Math.min(a.getTitle().length(), 4));
            System.out.println("Searching for albums containing: " + partialTitle);
            context.albums().findByTitleContaining(partialTitle)
                    .forEach(found -> System.out.println("Found album: " + found.getTitle()));
        });
    }

    // --- TRACKS ---
    private void createTracks() {
        System.out.println("\n--- Створення треків ---");
        if (context.tracks().count() == 0) {
            List<Album> albums = context.albums().findAll();
            List<Genre> genres = context.genres().findAll();
            List<Artist> artists = context.artists().findAll();

            if (!albums.isEmpty() && !genres.isEmpty() && !artists.isEmpty()) {
                for (Album album : albums) {
                    for (int i = 0; i < 5; i++) {
                        String fakeFilePath = String.format("data/audio/%s.mp3",
                                faker.internet().uuid());
                        Track track = new Track(
                                faker.expression("#{music.genre} Track #{code.asin}"),
                                Duration.ofSeconds(faker.random().nextInt(120, 300)),
                                album.getID(),
                                fakeFilePath);
                        track.addGenre(genres.get(faker.random().nextInt(genres.size())).getID());
                        track.addArtist(artists.get(faker.random().nextInt(artists.size())).getID());
                        context.registerNew(track);
                    }
                }
                System.out.println("Зареєстровано нові треки.");
            }
        }
    }

    private void demonstrateTrackCapabilities() {
        System.out.println("\n=== Track Repository Capabilities ===");
        context.tracks().findAll().stream().findFirst().ifPresent(t -> {
            String partialTitle = t.getTitle().substring(0, Math.min(t.getTitle().length(), 3));
            System.out.println("Searching for tracks containing: " + partialTitle);
            context.tracks().findByTitleContainingIgnoreCase(partialTitle)
                    .forEach(found -> System.out.println("Found track: " + found.getTitle()));
        });
    }

    // --- PLAYLISTS ---
    private void createPlaylists() {
        System.out.println("\n--- Створення плейлистів ---");
        if (context.playlists().count() == 0) {
            List<User> users = context.users().findAll();
            List<Track> tracks = context.tracks().findAll();

            if (!users.isEmpty() && !tracks.isEmpty()) {
                for (User user : users) {
                    Playlist playlist = new Playlist(
                            faker.music().genre() + " Mix",
                            faker.bool().bool(),
                            user.getID());
                    // Додаємо 3 випадкові треки
                    for (int i = 0; i < 3; i++) {
                        playlist.addTrack(tracks.get(faker.random().nextInt(tracks.size())).getID());
                    }
                    context.registerNew(playlist);
                }
                System.out.println("Зареєстровано нові плейлисти.");
            }
        }
    }

    private void demonstratePlaylistCapabilities() {
        System.out.println("\n=== Playlist Repository Capabilities ===");
        context.playlists().findAll().stream().findFirst().ifPresent(p -> {
            System.out.println("Sample playlist: " + p.getName());
            context.playlists().findByNameContainingIgnoreCase(p.getName())
                    .forEach(found -> System.out.println("Found playlist: " + found.getName()));
        });
    }

    // --- FAVORITES (LIKED SONGS) ---
    private void createFavorites() {
        System.out.println("\n--- Лайки ---");
        List<User> users = context.users().findAll();
        List<Track> tracks = context.tracks().findAll();

        if (!users.isEmpty() && !tracks.isEmpty()) {
            for (User user : users) {
                if (user.getLikedTrackIds().isEmpty()) {
                    Track track = tracks.get(faker.random().nextInt(tracks.size()));
                    user.likeTrack(track.getID());
                    context.registerDirty(user);
                }
            }
            System.out.println("Оновлено вподобання користувачів.");
        }
    }

    private void demonstrateFavoriteCapabilities() {
        System.out.println("\n=== Liked Songs Capabilities ===");
        context.users().findAll().stream().findFirst().ifPresent(u -> System.out
                .println("User " + u.getUsername() + " liked songs count: " + u.getLikedTrackIds().size()));
    }

    // --- HISTORY ---
    private void createHistory() {
        System.out.println("\n--- Створення історії ---");
        if (context.listeningHistory().count() == 0) {
            List<User> users = context.users().findAll();
            List<Track> tracks = context.tracks().findAll();

            if (!users.isEmpty() && !tracks.isEmpty()) {
                for (int i = 0; i < 10; i++) {
                    User user = users.get(faker.random().nextInt(users.size()));
                    Track track = tracks.get(faker.random().nextInt(tracks.size()));
                    ListeningHistory history = new ListeningHistory(
                            user.getID(),
                            track.getID(),
                            faker.date().past(30, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault())
                                    .toLocalDateTime());
                    context.registerNew(history);
                }
                System.out.println("Зареєстровано записи історії.");
            }
        }
    }

    private void demonstrateHistoryCapabilities() {
        System.out.println("\n=== History Repository Capabilities ===");
        context.users().findAll().stream().findFirst()
                .ifPresent(u -> System.out.println("History records for user " + u.getUsername() + ": " +
                        context.listeningHistory().findByUserId(u.getID()).size()));
    }

    // --- COLLECTIONS (USER LIBRARY) ---
    private void createCollections() {
        System.out.println("\n--- Бібліотека користувача ---");
        List<User> users = context.users().findAll();
        List<Artist> artists = context.artists().findAll();
        List<Playlist> playlists = context.playlists().findAll();

        if (!users.isEmpty()) {
            User user = users.get(0);
            if (!artists.isEmpty()) {
                Artist artist = artists.get(faker.random().nextInt(artists.size()));
                user.followArtist(artist.getID());
                System.out.println("User followed artist: " + artist.getStageName());
            }
            if (!playlists.isEmpty()) {
                Playlist playlist = playlists.get(faker.random().nextInt(playlists.size()));
                user.followPlaylist(playlist.getID());
                System.out.println("User followed playlist: " + playlist.getName());
            }
            context.registerDirty(user);
            System.out.println("Зміни профілю користувача зареєстровано.");
        }
    }

    private void demonstrateCollectionCapabilities() {
        System.out.println("\n=== User Library Capabilities ===");
        context.users().findAll().stream().findFirst().ifPresent(u -> {
            System.out.println("User: " + u.getUsername());
            System.out.println("Followed artists: " + u.getFollowedArtistIds().size());
            System.out.println("Followed playlists: " + u.getFollowedPlaylistIds().size());
        });
    }

}
