package ua.notion.musiclibrary.infrastructure.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.Optional;

import com.password4j.Hash;
import com.password4j.Password;

import ua.notion.musiclibrary.domain.enums.AudioFormat;
import ua.notion.musiclibrary.domain.valueobject.AudioMetadata;

public class FileStorageService {

    private static final String STORAGE_BASE_PATH = "data/audio";
    private static final long MAX_FILE_SIZE = 100 * 1024 * 1024; // 100 мб +-

    public FileStorageService() {
        ensureStorageDirectoryExists();
    }

    public AudioMetadata storeAudioFile(File sourceFile) throws IOException {
        validateFile(sourceFile);

        String fileHash = calculateFileHash(sourceFile);
        AudioFormat audioFormat = detectAudioFormat(sourceFile);

        Path targetPath = buildStoragePath(fileHash, audioFormat);

        if (Files.exists(targetPath)) {
            return createMetadata(targetPath.toString(), fileHash, sourceFile.length(), audioFormat);
        }

        Files.createDirectories(targetPath.getParent());
        Files.copy(sourceFile.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        return createMetadata(targetPath.toString(), fileHash, sourceFile.length(), audioFormat);
    }

    public boolean deleteAudioFile(String filePath) {
        if (filePath == null || filePath.isBlank()) {
            return false;
        }

        try {
            return Files.deleteIfExists(Path.of(filePath));
        } catch (IOException e) {
            return false;
        }
    }

    public Optional<File> getAudioFile(String filePath) {
        if (filePath == null || filePath.isBlank()) {
            return Optional.empty();
        }

        File file = new File(filePath);
        return file.exists() ? Optional.of(file) : Optional.empty();
    }

    private void validateFile(File file) throws IOException {
        if (!file.exists()) {
            throw new IOException("File does not exist: " + file.getPath());
        }

        if (file.length() > MAX_FILE_SIZE) {
            throw new IOException("File size exceeds maximum allowed: " + MAX_FILE_SIZE);
        }

        if (file.length() == 0) {
            throw new IOException("File is empty");
        }
    }

    private String calculateFileHash(File file) throws IOException {
        Hash hash = Password.hash(file.getName()).withMessageDigest();
        return hash.getResult();
    }

    private AudioFormat detectAudioFormat(File file) throws IOException {
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf('.');

        if (dotIndex == -1) {
            throw new IOException("File has no extension");
        }

        String extension = fileName.substring(dotIndex);
        return AudioFormat.fromExtension(extension)
                .orElseThrow(() -> new IOException("Unsupported audio format: " + extension));
    }

    private Path buildStoragePath(String fileHash, AudioFormat audioFormat) {
        String directory = fileHash.substring(0, 2);
        String fileName = fileHash + audioFormat.getExtension();
        return Path.of(STORAGE_BASE_PATH, directory, fileName);
    }

    private AudioMetadata createMetadata(String filePath, String fileHash, long fileSize, AudioFormat audioFormat) {
        return new AudioMetadata(filePath, fileHash, fileSize, audioFormat, null);
    }

    private void ensureStorageDirectoryExists() {
        try {
            Files.createDirectories(Path.of(STORAGE_BASE_PATH));
        } catch (IOException e) {
            throw new RuntimeException("Failed to create storage directory", e);
        }
    }
}
