package ua.notion.musiclibrary.infrastructure.storage.impl;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import ua.notion.musiclibrary.domain.impl.Entity;
import ua.notion.musiclibrary.infrastructure.storage.JsonFilePath;
import ua.notion.musiclibrary.infrastructure.storage.Repository;
import ua.notion.musiclibrary.infrastructure.storage.contract.AlbumRepository;
import ua.notion.musiclibrary.infrastructure.storage.contract.ArtistRepository;
import ua.notion.musiclibrary.infrastructure.storage.contract.FavoriteRepository;
import ua.notion.musiclibrary.infrastructure.storage.contract.GenreRepository;
import ua.notion.musiclibrary.infrastructure.storage.contract.ListeningHistoryRepository;
import ua.notion.musiclibrary.infrastructure.storage.contract.PlaylistRepository;
import ua.notion.musiclibrary.infrastructure.storage.contract.TrackRepository;
import ua.notion.musiclibrary.infrastructure.storage.contract.UserCollectionRepository;
import ua.notion.musiclibrary.infrastructure.storage.contract.UserRepository;

public class DataContext {

    private static class Holder {
        private static final DataContext INSTANCE = new DataContext();
    }

    private final UserRepository userRepository;
    private final GenreRepository genreRepository;
    private final ArtistRepository artistRepository;
    private final AlbumRepository albumRepository;
    private final TrackRepository trackRepository;
    private final PlaylistRepository playlistRepository;
    private final FavoriteRepository favoriteRepository;
    private final ListeningHistoryRepository listeningHistoryRepository;
    private final UserCollectionRepository userCollectionRepository;

    private final Set<Entity> newEntities = new LinkedHashSet<>();
    private final Set<Entity> dirtyEntities = new LinkedHashSet<>();
    private final Map<Repository<? extends Entity>, Set<UUID>> deletedIdsMap = new HashMap<>();

    private DataContext() {
        this.userRepository = new JsonUserRepository(JsonFilePath.USERS.getPath());
        this.genreRepository = new JsonGenreRepository(JsonFilePath.GENRES.getPath());
        this.artistRepository = new JsonArtistRepository(JsonFilePath.ARTISTS.getPath());
        this.albumRepository = new JsonAlbumRepository(JsonFilePath.ALBUMS.getPath());
        this.trackRepository = new JsonTrackRepository(JsonFilePath.TRACKS.getPath());
        this.playlistRepository = new JsonPlaylistRepository(JsonFilePath.PLAYLISTS.getPath());
        this.favoriteRepository = new JsonFavoriteRepository(JsonFilePath.FAVORITES.getPath());
        this.listeningHistoryRepository = new JsonListeningHistoryRepository(JsonFilePath.LISTENING_HISTORY.getPath());
        this.userCollectionRepository = new JsonUserCollectionRepository(JsonFilePath.USER_COLLECTIONS.getPath());
    }

    public static DataContext getInstance() {
        return Holder.INSTANCE;
    }

    /**
     * Повертає репозиторій для роботи з користувачами.
     */
    public UserRepository users() {
        return userRepository;
    }

    /**
     * Повертає репозиторій для роботи з жанрами.
     */
    public GenreRepository genres() {
        return genreRepository;
    }

    /**
     * Повертає репозиторій для роботи з артистами.
     */
    public ArtistRepository artists() {
        return artistRepository;
    }

    /**
     * Повертає репозиторій для роботи з альбомами.
     */
    public AlbumRepository albums() {
        return albumRepository;
    }

    /**
     * Повертає репозиторій для роботи з треками.
     */
    public TrackRepository tracks() {
        return trackRepository;
    }

    /**
     * Повертає репозиторій для роботи з плейлистами.
     */
    public PlaylistRepository playlists() {
        return playlistRepository;
    }

    /**
     * Повертає репозиторій для роботи з улюбленими треками.
     */
    public FavoriteRepository favorites() {
        return favoriteRepository;
    }

    /**
     * Повертає репозиторій для роботи з історією прослуховувань.
     */
    public ListeningHistoryRepository listeningHistory() {
        return listeningHistoryRepository;
    }

    /**
     * Повертає репозиторій для роботи з колекціями користувачів.
     */
    public UserCollectionRepository userCollections() {
        return userCollectionRepository;
    }

    /**
     * Реєструє нову сутність для вставки.
     */
    public <T extends Entity> void registerNew(T entity) {
        removeFromDeleted(entity);
        dirtyEntities.remove(entity);
        newEntities.add(entity);
    }

    /**
     * Реєструє змінену сутність для оновлення.
     */
    public <T extends Entity> void registerDirty(T entity) {
        if (!newEntities.contains(entity) && !isDeleted(entity)) {
            dirtyEntities.add(entity);
        }
    }

    /**
     * Реєструє сутність для видалення.
     */
    public <T extends Entity> void registerDeleted(T entity) {
        if (newEntities.remove(entity)) {
            return;
        }
        dirtyEntities.remove(entity);
        addToDeleted(entity);
    }

    /**
     * Реєструє сутність для видалення за ID та репозиторієм.
     */
    public <T extends Entity> void registerDeletedById(Repository<T> repository, UUID id) {
        deletedIdsMap.computeIfAbsent(repository, k -> new LinkedHashSet<>()).add(id);
    }

    /**
     * Фіксує всі зміни в репозиторіях.
     */
    public void commit() {
        // Спочатку вставляємо нові
        for (Entity entity : newEntities) {
            Repository<Entity> repo = getRepositoryForEntity(entity);
            if (repo != null) {
                repo.save(entity);
            }
        }

        // Потім оновлюємо змінені
        for (Entity entity : dirtyEntities) {
            Repository<Entity> repo = getRepositoryForEntity(entity);
            if (repo != null) {
                repo.save(entity);
            }
        }

        // Нарешті видаляємо
        for (Map.Entry<Repository<? extends Entity>, Set<UUID>> entry : deletedIdsMap.entrySet()) {
            @SuppressWarnings("unchecked")
            Repository<Entity> repo = (Repository<Entity>) entry.getKey();
            for (UUID id : entry.getValue()) {
                repo.deleteById(id);
            }
        }

        // Очищаємо черги
        clear();
    }

    /**
     * Відкочує всі незбережені зміни.
     */
    public void rollback() {
        clear();
    }

    /**
     * Очищає всі черги.
     */
    public void clear() {
        newEntities.clear();
        dirtyEntities.clear();
        deletedIdsMap.clear();
    }

    /**
     * Перевіряє, чи є незбережені зміни.
     */
    public boolean hasChanges() {
        return !newEntities.isEmpty()
                || !dirtyEntities.isEmpty()
                || !deletedIdsMap.isEmpty();
    }

    /**
     * Повертає статистику змін.
     */
    public String getChangesSummary() {
        int deletedCount = deletedIdsMap.values().stream()
                .mapToInt(Set::size)
                .sum();

        return String.format(
                "New: %d, Dirty: %d, Deleted: %d",
                newEntities.size(),
                dirtyEntities.size(),
                deletedCount);
    }

    public void clearAllCaches() {
        clearCache(userRepository);
        clearCache(genreRepository);
        clearCache(artistRepository);
        clearCache(albumRepository);
        clearCache(trackRepository);
        clearCache(playlistRepository);
        clearCache(favoriteRepository);
        clearCache(listeningHistoryRepository);
        clearCache(userCollectionRepository);
    }

    private void clearCache(Repository<?> repository) {
        if (repository instanceof CachedJsonRepository) {
            ((CachedJsonRepository<?>) repository).clearCache();
        }
    }

    @SuppressWarnings("unchecked")
    private <T extends Entity> Repository<T> getRepositoryForEntity(T entity) {
        String className = entity.getClass().getSimpleName();

        return switch (className) {
            case "User" -> (Repository<T>) userRepository;
            case "Genre" -> (Repository<T>) genreRepository;
            case "Artist" -> (Repository<T>) artistRepository;
            case "Album" -> (Repository<T>) albumRepository;
            case "Track" -> (Repository<T>) trackRepository;
            case "Playlist" -> (Repository<T>) playlistRepository;
            case "Favorite" -> (Repository<T>) favoriteRepository;
            case "ListeningHistory" -> (Repository<T>) listeningHistoryRepository;
            case "UserCollection" -> (Repository<T>) userCollectionRepository;
            default -> null;
        };
    }

    private void addToDeleted(Entity entity) {
        Repository<? extends Entity> repo = getRepositoryForEntity(entity);
        if (repo != null) {
            deletedIdsMap.computeIfAbsent(repo, k -> new LinkedHashSet<>())
                    .add(entity.getID());
        }
    }

    private void removeFromDeleted(Entity entity) {
        Repository<? extends Entity> repo = getRepositoryForEntity(entity);
        if (repo != null) {
            Set<UUID> ids = deletedIdsMap.get(repo);
            if (ids != null) {
                ids.remove(entity.getID());
            }
        }
    }

    private boolean isDeleted(Entity entity) {
        Repository<? extends Entity> repo = getRepositoryForEntity(entity);
        if (repo != null) {
            Set<UUID> ids = deletedIdsMap.get(repo);
            return ids != null && ids.contains(entity.getID());
        }
        return false;
    }
}
