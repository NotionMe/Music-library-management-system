package ua.notion.musiclibrary.dto.track;

import java.time.Duration;
import java.util.Set;
import java.util.UUID;

public record TrackCreateDto(
        String title,
        Duration duration,
        UUID albumId,
        Set<UUID> genreIds,
        Set<UUID> artistIds) {
}
