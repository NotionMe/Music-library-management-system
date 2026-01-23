package ua.notion.musiclibrary.dto.playlist;

import java.util.UUID;

public record PlaylistCreateDto(String name, boolean isPrivate, UUID userId) {
}