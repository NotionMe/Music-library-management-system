package ua.notion.musiclibrary.domain.repository.json;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.google.gson.reflect.TypeToken;

import ua.notion.musiclibrary.domain.model.Artist;
import ua.notion.musiclibrary.domain.repository.ArtistRepository;

public class JsonArtistRepository extends CachedJsonRepository<Artist, UUID> implements ArtistRepository {

    public JsonArtistRepository(String filename) {
        super(
                filename,
                new TypeToken<ArrayList<Artist>>() {
                }.getType(),
                Artist::getID);
    }

    @Override
    public Optional<Artist> findByUserId(UUID userId) {
        return findFirstBy(artist -> artist.getUserId().equals(userId));
    }

    @Override
    public List<Artist> findByStageName(String stageName) {
        return findBy(artist -> artist.getStageName().equals(stageName));
    }

    @Override
    public List<Artist> findByBio(String bio) {
        return findBy(artist -> artist.getBio().contains(bio));
    }
}
