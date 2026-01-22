package ua.notion.musiclibrary.infrastructure.storage.impl;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.google.gson.reflect.TypeToken;

import ua.notion.musiclibrary.domain.impl.Track;
import ua.notion.musiclibrary.infrastructure.storage.contract.TrackRepository;

class JsonTrackRepository extends CachedJsonRepository<Track> implements TrackRepository {

    public JsonTrackRepository(String filename) {
        super(
                filename,
                new TypeToken<ArrayList<Track>>() {
                }.getType());
    }

    @Override
    public List<Track> findByAlbumId(UUID albumId) {
        return findBy(track -> track.getAlbumId().equals(albumId));
    }

    @Override
    public List<Track> findByGenreId(UUID genreId) {
        return findBy(track -> track.getGenreIds().contains(genreId));
    }

    @Override
    public List<Track> findByArtistId(UUID artistId) {
        return findBy(track -> track.getArtistIds().contains(artistId));
    }

    @Override
    public List<Track> findByTitleContainingIgnoreCase(String title) {
        return findBy(track -> track.getTitle().toLowerCase().contains(title.toLowerCase()));
    }

    @Override
    public List<Track> findByDurationGreaterThan(Duration duration) {
        return findBy(track -> track.getDuration().toNanos() > duration.toNanos());
    }

    @Override
    public List<Track> findByDurationBetween(Duration start, Duration end) {
        return findBy(track -> {
            long nanos = track.getDuration().toNanos();
            return nanos >= start.toNanos() && nanos <= end.toNanos();
        });
    }

    @Override
    public List<Track> findByAlbumIdAndGenreId(UUID albumId, UUID genreId) {
        return findBy(track -> albumId.equals(track.getAlbumId()) &&
                track.getGenreIds().contains(genreId));
    }
}
