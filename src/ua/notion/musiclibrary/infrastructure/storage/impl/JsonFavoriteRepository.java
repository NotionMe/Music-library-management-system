package ua.notion.musiclibrary.infrastructure.storage.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.google.gson.reflect.TypeToken;

import ua.notion.musiclibrary.domain.impl.Favorite;
import ua.notion.musiclibrary.infrastructure.storage.contract.FavoriteRepository;

class JsonFavoriteRepository extends CachedJsonRepository<Favorite> implements FavoriteRepository {

    public JsonFavoriteRepository(String filename) {
        super(
                filename,
                new TypeToken<ArrayList<Favorite>>() {
                }.getType());
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
