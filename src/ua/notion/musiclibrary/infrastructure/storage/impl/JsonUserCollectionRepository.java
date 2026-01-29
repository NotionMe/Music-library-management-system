package ua.notion.musiclibrary.infrastructure.storage.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.google.gson.reflect.TypeToken;

import ua.notion.musiclibrary.domain.impl.UserCollection;
import ua.notion.musiclibrary.infrastructure.storage.contract.UserCollectionRepository;

class JsonUserCollectionRepository extends CachedJsonRepository<UserCollection>
        implements UserCollectionRepository {

    public JsonUserCollectionRepository(String filename) {
        super(
                filename,
                new TypeToken<ArrayList<UserCollection>>() {
                }.getType());
    }

    @Override
    public Optional<UserCollection> findByUserId(UUID userId) {
        return findFirstBy(collection -> collection.getUserId().equals(userId));
    }

    @Override
    public List<UserCollection> findByNameContainingIgnoreCase(String name) {
        return findBy(collection -> collection.getName().toLowerCase().contains(name.toLowerCase()));
    }

    @Override
    public List<UserCollection> findByDescriptionContainingIgnoreCase(String keyword) {
        return findBy(collection -> collection.getDescription() != null
                && collection.getDescription().toLowerCase().contains(keyword.toLowerCase()));
    }

    @Override
    public List<UserCollection> findByGroupsContaining(String groupName) {
        return findBy(collection -> collection.getGroups().contains(groupName));
    }

    @Override
    public List<UserCollection> findByPlaylistIdsContaining(UUID playlistId) {
        return findBy(collection -> collection.getPlaylistIds().contains(playlistId));
    }

    @Override
    public boolean existsByUserId(UUID userId) {
        return findFirstBy(collection -> collection.getUserId().equals(userId)).isPresent();
    }
}
