package ua.notion.musiclibrary.infrastructure.storage.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.google.gson.reflect.TypeToken;

import ua.notion.musiclibrary.domain.impl.ListeningHistory;
import ua.notion.musiclibrary.infrastructure.storage.contract.ListeningHistoryRepository;

class JsonListeningHistoryRepository extends CachedJsonRepository<ListeningHistory>
        implements ListeningHistoryRepository {

    public JsonListeningHistoryRepository(String filename) {
        super(
                filename,
                new TypeToken<ArrayList<ListeningHistory>>() {
                }.getType());
    }

    @Override
    public List<ListeningHistory> findByUserId(UUID userId) {
        return findBy(history -> history.getUserId().equals(userId));
    }

    @Override
    public List<ListeningHistory> findByTrackId(UUID trackId) {
        return findBy(history -> history.getTrackId().equals(trackId));
    }

    @Override
    public List<ListeningHistory> findByPlayedAt(LocalDateTime dateTime) {
        return findBy(history -> history.getPlayedAt().equals(dateTime));
    }
}
