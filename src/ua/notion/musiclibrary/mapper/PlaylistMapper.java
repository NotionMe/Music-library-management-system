package ua.notion.musiclibrary.mapper;

import ua.notion.musiclibrary.domain.impl.Playlist;
import ua.notion.musiclibrary.dto.playlist.PlaylistCreateDto;

public final class PlaylistMapper {

    private PlaylistMapper() {
    }

    public static Playlist toDomain(PlaylistCreateDto dto) {
        return new Playlist(
                dto.name(),
                dto.isPrivate(),
                dto.userId());
    }
}
