package ua.notion.musiclibrary.dto.playlist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import ua.notion.musiclibrary.domain.exception.EntityValidationException;

public record PlaylistUpdateDto(UUID playlistId, String newName, boolean isPrivate) {
    public PlaylistUpdateDto {
        Map<String, List<String>> errors = new HashMap<>();

        if (playlistId == null) {
            addError(errors, "playlistId", "ID плейлиста обов'язковий!");
        }

        if (newName == null || newName.isBlank()) {
            addError(errors, "newName", "Нова назва обов'язкова!");
        }

        if (!errors.isEmpty()) {
            throw new EntityValidationException(errors);
        }
    }

    private void addError(Map<String, List<String>> errors, String field, String message) {
        errors.computeIfAbsent(field, k -> new ArrayList<>()).add(message);
    }
}