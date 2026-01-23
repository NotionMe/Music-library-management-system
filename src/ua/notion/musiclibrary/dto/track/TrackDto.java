package ua.notion.musiclibrary.dto.track;

import java.util.UUID;

public record TrackDto(
        UUID id,
        String title,
        String duration,
        String artistName,
        String albumName) {

    @Override
    public String toString() {
        return String.format("%s - %s [%s] (Album: %s)", artistName, title, duration, albumName);
    }
}
