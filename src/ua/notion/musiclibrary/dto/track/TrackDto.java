package ua.notion.musiclibrary.dto.track;

import java.util.UUID;

import ua.notion.musiclibrary.domain.enums.AudioFormat;

public record TrackDto(
        UUID id,
        String title,
        String duration,
        String artistName,
        String albumName,
        String filePath,
        AudioFormat audioFormat,
        String fileSize,
        Integer bitrate) {

    @Override
    public String toString() {
        String fileInfo = audioFormat != null ? String.format(" [%s, %s]", audioFormat, fileSize) : "";
        return String.format("%s - %s [%s] (Album: %s)%s", artistName, title, duration, albumName, fileInfo);
    }
}
