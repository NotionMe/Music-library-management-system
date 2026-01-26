package ua.notion.musiclibrary.service.contract;

import java.util.Optional;
import java.util.UUID;

public interface VerificationCodeStore {

    void store(UUID userId, String code);

    Optional<String> retrieve(UUID userId);

    void remove(UUID userId);

    boolean verify(UUID userId, String code);
}
