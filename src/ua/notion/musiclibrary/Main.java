package ua.notion.musiclibrary;

import ua.notion.musiclibrary.domain.repository.AlbumRepository;
import ua.notion.musiclibrary.domain.repository.ArtistRepository;
import ua.notion.musiclibrary.domain.repository.FavoriteRepository;
import ua.notion.musiclibrary.domain.repository.GenreRepository;
import ua.notion.musiclibrary.domain.repository.ListeningHistoryRepository;
import ua.notion.musiclibrary.domain.repository.PlaylistRepository;
import ua.notion.musiclibrary.domain.repository.TrackRepository;
import ua.notion.musiclibrary.domain.repository.UserCollectionRepository;
import ua.notion.musiclibrary.domain.repository.UserRepository;
import ua.notion.musiclibrary.domain.repository.json.JsonAlbumRepository;
import ua.notion.musiclibrary.domain.repository.json.JsonArtistRepository;
import ua.notion.musiclibrary.domain.repository.json.JsonFavoriteRepository;
import ua.notion.musiclibrary.domain.repository.json.JsonGenreRepository;
import ua.notion.musiclibrary.domain.repository.json.JsonListeningHistoryRepository;
import ua.notion.musiclibrary.domain.repository.json.JsonPlaylistRepository;
import ua.notion.musiclibrary.domain.repository.json.JsonTrackRepository;
import ua.notion.musiclibrary.domain.repository.json.JsonUserCollectionRepository;
import ua.notion.musiclibrary.domain.repository.json.JsonUserRepository;
import ua.notion.musiclibrary.test.LibraryApplication;

public class Main {
  public static void main(String[] args) {
    UserRepository userRepository = new JsonUserRepository("data/users.json");
    GenreRepository genreRepository = new JsonGenreRepository("data/genres.json");
    ArtistRepository artistRepository = new JsonArtistRepository("data/artists.json");
    AlbumRepository albumRepository = new JsonAlbumRepository("data/albums.json");
    TrackRepository trackRepository = new JsonTrackRepository("data/tracks.json");
    PlaylistRepository playlistRepository = new JsonPlaylistRepository("data/playlists.json");
    FavoriteRepository favoriteRepository = new JsonFavoriteRepository("data/favorites.json");
    ListeningHistoryRepository listeningHistoryRepository = new JsonListeningHistoryRepository("data/history.json");
    UserCollectionRepository userCollectionRepository = new JsonUserCollectionRepository("data/collections.json");

    LibraryApplication libraryApplication = new LibraryApplication(
        userRepository,
        genreRepository,
        artistRepository,
        albumRepository,
        trackRepository,
        playlistRepository,
        favoriteRepository,
        listeningHistoryRepository,
        userCollectionRepository);
    libraryApplication.run();

  }
}
