package ua.notion.musiclibrary.infrastructure.service;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

import ua.notion.musiclibrary.domain.enums.AudioFormat;

public class AudioMetadataExtractor {

    public Duration extractDuration(File audioFile) throws IOException {
        validateFile(audioFile);

        long estimatedSeconds = estimateDurationFromFileSize(audioFile);
        return Duration.ofSeconds(estimatedSeconds);
    }

    public Integer extractBitrate(File audioFile) throws IOException {
        validateFile(audioFile);

        return estimateBitrateFromFileSize(audioFile);
    }

    public AudioFormat extractFormat(File audioFile) throws IOException {
        validateFile(audioFile);

        String fileName = audioFile.getName();
        int dotIndex = fileName.lastIndexOf('.');

        if (dotIndex == -1) {
            throw new IOException("File has no extension");
        }

        String extension = fileName.substring(dotIndex);
        return AudioFormat.fromExtension(extension)
                .orElseThrow(() -> new IOException("Unsupported audio format: " + extension));
    }

    private void validateFile(File file) throws IOException {
        if (!file.exists()) {
            throw new IOException("File does not exist: " + file.getPath());
        }

        if (file.length() == 0) {
            throw new IOException("File is empty");
        }
    }

    private long estimateDurationFromFileSize(File file) {
        long fileSizeBytes = file.length();
        long averageBitrate = 192_000;
        return (fileSizeBytes * 8) / averageBitrate;
    }

    private Integer estimateBitrateFromFileSize(File file) {
        long fileSizeBytes = file.length();

        if (fileSizeBytes < 1024 * 1024) {
            return 128;
        } else if (fileSizeBytes < 5 * 1024 * 1024) {
            return 192;
        } else {
            return 320;
        }
    }
}
