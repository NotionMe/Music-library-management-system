package ua.notion.musiclibrary.service.contract;

import ua.notion.musiclibrary.domain.impl.Playlist;
import ua.notion.musiclibrary.dto.playlist.PlaylistCreateDto;
import ua.notion.musiclibrary.dto.playlist.PlaylistUpdateDto;
import java.util.UUID;
import java.util.List;

public interface PlaylistService {

    // 1. Метод createPlaylist(PlaylistCreateDto dto) -> повертає Playlist

    // 2. Метод updatePlaylist(PlaylistUpdateDto dto)

    // 3. Метод deletePlaylist(UUID playlistId)

    // 4. Метод addTrackToPlaylist(UUID playlistId, UUID trackId)

    // 5. Метод getMyPlaylists(UUID userId) -> повертає List<Playlist>
}
