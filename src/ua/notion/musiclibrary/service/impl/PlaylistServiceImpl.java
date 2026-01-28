package ua.notion.musiclibrary.service.impl;

import java.util.List;
import java.util.UUID;

import ua.notion.musiclibrary.domain.impl.Playlist;
import ua.notion.musiclibrary.dto.playlist.PlaylistCreateDto;
import ua.notion.musiclibrary.dto.playlist.PlaylistUpdateDto;
import ua.notion.musiclibrary.infrastructure.storage.impl.DataContext;
import ua.notion.musiclibrary.service.contract.PlaylistService;

public class PlaylistServiceImpl implements PlaylistService {

    private final DataContext dataContext;

    public PlaylistServiceImpl(DataContext dataContext) {
        this.dataContext = dataContext;

    }

    @Override
    public Playlist createPlayList(PlaylistCreateDto dto) {
        Playlist newPlaylist = new Playlist(dto.name(), dto.isPrivate(), dto.userId());
        dataContext.registerNew(newPlaylist);
        dataContext.commit();
        return newPlaylist;
    }

    @Override
    public Playlist updatePlaylist(PlaylistUpdateDto dto) {
        Playlist playlist = dataContext.playlists().findById(dto.playlistId())
                .orElseThrow(() -> new IllegalArgumentException("Playlist not found"));

        playlist.setName(dto.newName());
        playlist.setPrivate(dto.isPrivate());

        dataContext.registerDirty(playlist);
        dataContext.commit();
        return playlist;
    }

    @Override
    public boolean deletePlaylist(UUID playlistId) {
        if (!dataContext.playlists().findById(playlistId).isPresent()) {
            return false;
        }

        dataContext.registerDeletedById(dataContext.playlists(), playlistId);
        dataContext.commit();
        return true;
    }

    @Override
    public boolean addTrackToPlaylist(UUID playlistId, UUID trackId) {
        Playlist playlist = dataContext.playlists().findById(playlistId)
                .orElseThrow(() -> new IllegalArgumentException("Playlist not found"));

        if (!dataContext.tracks().findById(trackId).isPresent()) {
            throw new IllegalArgumentException("Track not found");
        }

        playlist.addTrack(trackId);
        dataContext.registerDirty(playlist);
        dataContext.commit();
        return true;
    }

    @Override
    public List<Playlist> getMyPlaylists(UUID userId) {
        return dataContext.playlists().findAll().stream()
                .filter(playlist -> playlist.getUserId().equals(userId))
                .toList();
    }

}
