package ua.notion.musiclibrary.domain.repository.json;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.google.gson.reflect.TypeToken;

import ua.notion.musiclibrary.domain.model.Album;
import ua.notion.musiclibrary.domain.repository.AlbumRepository;

public class JsonAlbumRepository extends CachedJsonRepository<Album, UUID> implements AlbumRepository {

    public JsonAlbumRepository(String filename) {
        super(
                filename,
                new TypeToken<ArrayList<Album>>() {
                }.getType(),
                Album::getID);
    }

    @Override
    public List<Album> findByTitleContaining(String title) {
        return findBy(album -> album.getTitle().contains(title));
    }

    @Override
    public List<Album> findByArtistId(UUID artistId) {
        return findBy(album -> album.getArtistId().equals(artistId));
    }

    @Override
    public List<Album> findByReleaseDate(LocalDate date) {
        return findBy(album -> album.getReleaseDate().equals(date));
    }

    @Override
    public List<Album> findByReleaseDateBetween(LocalDate startDate, LocalDate endDate) {
        return findBy(album -> !album.getReleaseDate().isBefore(startDate) && !album.getReleaseDate().isAfter(endDate));
    }
}
