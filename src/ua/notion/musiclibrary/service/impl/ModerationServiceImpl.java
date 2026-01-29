package ua.notion.musiclibrary.service.impl;

import java.util.List;
import java.util.UUID;

import ua.notion.musiclibrary.domain.enums.PublicationStatus;
import ua.notion.musiclibrary.domain.impl.Track;
import ua.notion.musiclibrary.dto.track.TrackDto;
import ua.notion.musiclibrary.infrastructure.storage.impl.DataContext;
import ua.notion.musiclibrary.mapper.TrackMapper;
import ua.notion.musiclibrary.service.contract.ModerationService;

public class ModerationServiceImpl implements ModerationService {

    private final DataContext dataContext;

    public ModerationServiceImpl(DataContext dataContext) {
        this.dataContext = dataContext;
    }

    @Override
    public List<TrackDto> getPendingTracks() {
        return dataContext.tracks().findAll().stream()
                .filter(track -> track.getPublicationStatus() == PublicationStatus.PENDING_REVIEW)
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public void approveTrack(UUID trackId, UUID adminId) {
        Track track = dataContext.tracks().findById(trackId)
                .orElseThrow(() -> new IllegalArgumentException("Track not found"));

        track.setPublicationStatus(PublicationStatus.PUBLISHED);
        track.setRejectionReason(null);

        dataContext.tracks().save(track);
        dataContext.commit();
    }

    @Override
    public void rejectTrack(UUID trackId, UUID adminId, String reason) {
        Track track = dataContext.tracks().findById(trackId)
                .orElseThrow(() -> new IllegalArgumentException("Track not found"));

        track.setPublicationStatus(PublicationStatus.REJECTED);
        track.setRejectionReason(reason);

        dataContext.tracks().save(track);
        dataContext.commit();
    }

    @Override
    public List<TrackDto> getUserTracks(UUID userId) {
        return dataContext.tracks().findAll().stream()
                .filter(track -> userId.equals(track.getUploadedBy()))
                .map(this::mapToDto)
                .toList();
    }

    private TrackDto mapToDto(Track track) {
        String artistName = track.getArtistIds().stream()
                .findFirst()
                .flatMap(artistId -> dataContext.artists().findById(artistId))
                .map(artist -> artist.getStageName())
                .orElse("Unknown Artist");

        String albumName = dataContext.albums().findById(track.getAlbumId())
                .map(album -> album.getTitle())
                .orElse("Unknown Album");

        return TrackMapper.toDto(track, artistName, albumName);
    }
}
