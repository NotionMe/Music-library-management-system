package ua.notion.musiclibrary.domain.valueobject;

import ua.notion.musiclibrary.domain.enums.AudioFormat;

public record AudioMetadata(
        String filePath,
        String fileHash,
        Long fileSizeBytes,
        AudioFormat audioFormat,
        Integer bitrate) {

    public AudioMetadata {
        if (filePath == null || filePath.isBlank()) {
            throw new IllegalArgumentException("File path cannot be empty");
        }
        if (fileHash == null || fileHash.isBlank()) {
            throw new IllegalArgumentException("File hash cannot be empty");
        }
        if (fileSizeBytes == null || fileSizeBytes <= 0) {
            throw new IllegalArgumentException("File size must be positive");
        }
        if (audioFormat == null) {
            throw new IllegalArgumentException("Audio format cannot be null");
        }
        if (bitrate != null && bitrate <= 0) {
            throw new IllegalArgumentException("Bitrate must be positive");
        }
    }

    public String getFormattedFileSize() {
        if (fileSizeBytes < 1024) {
            return fileSizeBytes + " B";
        }
        if (fileSizeBytes < 1024 * 1024) {
            return String.format("%.2f KB", fileSizeBytes / 1024.0);
        }
        return String.format("%.2f MB", fileSizeBytes / (1024.0 * 1024.0));
    }
}
