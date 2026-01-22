package ua.notion.musiclibrary.domain.repository.json;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.google.gson.reflect.TypeToken;

import ua.notion.musiclibrary.domain.model.Favorite;
import ua.notion.musiclibrary.domain.repository.FavoriteRepository;

public class JsonFavoriteRepository extends CachedJsonRepository<Favorite, UUID> implements FavoriteRepository {

    public JsonFavoriteRepository(String filename) {
        super(
                filename,
                new TypeToken<ArrayList<Favorite>>() {
                }.getType(),
                Favorite::getID);
    }

    @Override
    public List<Favorite> findByUserId(UUID userId) {
        return findBy(favorite -> favorite.getUserId().equals(userId));
    }

    @Override
    public List<Favorite> findByTrackId(UUID trackId) {
        return findBy(favorite -> favorite.getTrackId().equals(trackId));
    }

    @Override
    public List<Favorite> findByAddedAt(LocalDateTime addedAt) {
        return findBy(favorite -> favorite.getAddedAt().equals(addedAt));
    }
}
