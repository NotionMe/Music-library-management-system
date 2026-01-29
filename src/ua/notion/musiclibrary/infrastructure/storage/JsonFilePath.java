package ua.notion.musiclibrary.infrastructure.storage;

public enum JsonFilePath {
    USERS("data/users.json"),
    GENRES("data/genres.json"),
    ARTISTS("data/artists.json"),
    ALBUMS("data/albums.json"),
    TRACKS("data/tracks.json"),
    PLAYLISTS("data/playlists.json"),
    FAVORITES("data/favorites.json"),
    LISTENING_HISTORY("data/history.json"),
    USER_COLLECTIONS("data/collections.json");

    private final String path;

    JsonFilePath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
