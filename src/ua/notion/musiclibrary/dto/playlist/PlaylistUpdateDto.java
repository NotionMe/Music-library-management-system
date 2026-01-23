package ua.notion.musiclibrary.dto.playlist;

import java.util.UUID;

public record PlaylistUpdateDto(UUID playlistId, String newName, boolean isPrivate) {
}