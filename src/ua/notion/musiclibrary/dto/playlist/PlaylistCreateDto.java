package ua.notion.musiclibrary.dto.playlist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import ua.notion.musiclibrary.domain.exception.EntityValidationException;

public record PlaylistCreateDto(String name, boolean isPrivate, UUID userId) {
    public PlaylistCreateDto {
        Map<String, List<String>> errors = new HashMap<>();

        if (name == null || name.isBlank()) {
            addError(errors, "name", "Назва плейлиста обов'язкова!");
        }

        if (userId == null) {
            addError(errors, "userId", "ID користувача обов'язковий!");
        }

        if (!errors.isEmpty()) {
            throw new EntityValidationException(errors);
        }
    }

    private void addError(Map<String, List<String>> errors, String field, String message) {
        errors.computeIfAbsent(field, k -> new ArrayList<>()).add(message);
    }
}