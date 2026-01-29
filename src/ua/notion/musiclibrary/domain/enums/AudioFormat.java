package ua.notion.musiclibrary.domain.enums;

import java.util.Arrays;
import java.util.Optional;

public enum AudioFormat {
    MP3("audio/mpeg", ".mp3"),
    FLAC("audio/flac", ".flac"),
    WAV("audio/wav", ".wav"),
    M4A("audio/mp4", ".m4a"),
    OGG("audio/ogg", ".ogg");

    private final String mimeType;
    private final String extension;

    AudioFormat(String mimeType, String extension) {
        this.mimeType = mimeType;
        this.extension = extension;
    }

    public String getMimeType() {
        return mimeType;
    }

    public String getExtension() {
        return extension;
    }

    public static Optional<AudioFormat> fromExtension(String extension) {
        return Arrays.stream(values())
                .filter(format -> format.extension.equalsIgnoreCase(extension))
                .findFirst();
    }

    public static Optional<AudioFormat> fromMimeType(String mimeType) {
        return Arrays.stream(values())
                .filter(format -> format.mimeType.equalsIgnoreCase(mimeType))
                .findFirst();
    }
}
