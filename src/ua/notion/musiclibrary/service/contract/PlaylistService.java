package ua.notion.musiclibrary.service.contract;

import ua.notion.musiclibrary.domain.impl.Playlist;
import ua.notion.musiclibrary.dto.playlist.PlaylistCreateDto;
import ua.notion.musiclibrary.dto.playlist.PlaylistUpdateDto;
import java.util.UUID;
import java.util.List;

public interface PlaylistService {

    Playlist createPlayList(PlaylistCreateDto dto);

    Playlist updatePlaylist(PlaylistUpdateDto dto);

    boolean deletePlaylist(UUID playlistId);

    boolean addTrackToPlaylist(UUID playlistId, UUID trackId);

    List<Playlist> getMyPlaylists(UUID userId);
}
