package ua.notion.musiclibrary.infrastructure.storage.impl;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class IdentityMap<ID, T> {

    private final Map<ID, T> cache = new HashMap<>();

    public Optional<T> get(ID id) {
        return Optional.ofNullable(cache.get(id));
    }

    public void put(ID id, T entity) {
        cache.put(id, entity);
    }

    public void remove(ID id) {
        cache.remove(id);
    }

    public void clear() {
        cache.clear();
    }

    public boolean contains(ID id) {
        return cache.containsKey(id);
    }

    public int size() {
        return cache.size();
    }
}