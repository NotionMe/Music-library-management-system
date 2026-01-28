package ua.notion.musiclibrary.service.impl;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import ua.notion.musiclibrary.service.contract.VerificationCodeStore;

public class InMemoryVerificationCodeStore implements VerificationCodeStore {

    private static final long EXPIRATION_TIME_SECONDS = 60;

    private static InMemoryVerificationCodeStore instance;
    private final Map<UUID, CodeEntry> codeStorage;

    private InMemoryVerificationCodeStore() {
        this.codeStorage = new ConcurrentHashMap<>();
    }

    public static synchronized InMemoryVerificationCodeStore getInstance() {
        if (instance == null) {
            instance = new InMemoryVerificationCodeStore();
        }
        return instance;
    }

    @Override
    public void store(UUID userId, String code) {
        codeStorage.put(userId, new CodeEntry(code, Instant.now()));
    }

    @Override
    public Optional<String> retrieve(UUID userId) {
        return Optional.ofNullable(codeStorage.get(userId))
                .filter(this::isNotExpired)
                .map(CodeEntry::code);
    }

    @Override
    public void remove(UUID userId) {
        codeStorage.remove(userId);
    }

    @Override
    public boolean verify(UUID userId, String code) {
        CodeEntry entry = codeStorage.get(userId);

        if (entry == null) {
            return false;
        }

        if (!isNotExpired(entry)) {
            remove(userId);
            return false;
        }

        return entry.code().equals(code);
    }

    private boolean isNotExpired(CodeEntry entry) {
        long secondsPassed = Instant.now().getEpochSecond() - entry.createdAt().getEpochSecond();
        return secondsPassed <= EXPIRATION_TIME_SECONDS;
    }

    private record CodeEntry(String code, Instant createdAt) {
    }
}
