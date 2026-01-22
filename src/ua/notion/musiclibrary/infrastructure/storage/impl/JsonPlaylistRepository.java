package ua.notion.musiclibrary.infrastructure.storage.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.google.gson.reflect.TypeToken;

import ua.notion.musiclibrary.domain.impl.Playlist;
import ua.notion.musiclibrary.infrastructure.storage.contract.PlaylistRepository;

class JsonPlaylistRepository extends CachedJsonRepository<Playlist> implements PlaylistRepository {

    public JsonPlaylistRepository(String filename) {
        super(
                filename,
                new TypeToken<ArrayList<Playlist>>() {
                }.getType());
    }

    @Override
    public List<Playlist> findByUserId(UUID userId) {
        return findBy(playlist -> playlist.getUserId().equals(userId));
    }

    @Override
    public List<Playlist> findByNameContainingIgnoreCase(String name) {
        return findBy(playlist -> playlist.getName().toLowerCase().contains(name.toLowerCase()));
    }

    @Override
    public List<Playlist> findByIsPrivateFalse() {
        return findBy(playlist -> !playlist.isPrivate());
    }

    @Override
    public List<Playlist> findByUserIdAndIsPrivateFalse(UUID userId) {
        return findBy(playlist -> playlist.getUserId().equals(userId) && !playlist.isPrivate());
    }

    @Override
    public List<Playlist> findByTrackIdsContaining(UUID trackId) {
        return findBy(playlist -> playlist.getTrackIds().contains(trackId));
    }
}
