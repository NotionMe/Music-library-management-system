package ua.notion.musiclibrary.service.contract;

import java.util.List;
import java.util.UUID;

import ua.notion.musiclibrary.dto.track.TrackDto;

public interface ModerationService {

    List<TrackDto> getPendingTracks();

    void approveTrack(UUID trackId, UUID adminId);

    void rejectTrack(UUID trackId, UUID adminId, String reason);

    List<TrackDto> getUserTracks(UUID userId);
}
