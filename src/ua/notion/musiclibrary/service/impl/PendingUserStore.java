package ua.notion.musiclibrary.service.impl;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import ua.notion.musiclibrary.dto.auth.UserRegistrationDto;

public class PendingUserStore {

    private static PendingUserStore instance;
    private final Map<UUID, UserRegistrationDto> pendingUsers;

    private PendingUserStore() {
        this.pendingUsers = new ConcurrentHashMap<>();
    }

    public static synchronized PendingUserStore getInstance() {
        if (instance == null) {
            instance = new PendingUserStore();
        }
        return instance;
    }

    public void store(UUID tempId, UserRegistrationDto dto) {
        pendingUsers.put(tempId, dto);
    }

    public Optional<UserRegistrationDto> retrieve(UUID tempId) {
        return Optional.ofNullable(pendingUsers.get(tempId));
    }

    public void remove(UUID tempId) {
        pendingUsers.remove(tempId);
    }
}
