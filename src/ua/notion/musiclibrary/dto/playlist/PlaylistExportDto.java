package ua.notion.musiclibrary.dto.playlist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import ua.notion.musiclibrary.domain.exception.EntityValidationException;

public record PlaylistExportDto(UUID playlistId, String formatType) {
    public PlaylistExportDto {
        Map<String, List<String>> errors = new HashMap<>();

        if (playlistId == null) {
            addError(errors, "playlistId", "ID плейлиста обов'язковий!");
        }

        if (formatType == null || formatType.isBlank()) {
            addError(errors, "formatType", "Формат експорту обов'язковий!");
        }

        if (!errors.isEmpty()) {
            throw new EntityValidationException(errors);
        }
    }

    private void addError(Map<String, List<String>> errors, String field, String message) {
        errors.computeIfAbsent(field, k -> new ArrayList<>()).add(message);
    }
}