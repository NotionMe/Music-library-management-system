package ua.notion.musiclibrary.service.contract;

import ua.notion.musiclibrary.dto.track.TrackDto;
import ua.notion.musiclibrary.dto.track.TrackSearchDto;
import java.util.List;
import java.util.UUID;

public interface TrackService {

    List<TrackDto> searchTracks(TrackSearchDto searchDto);

    TrackDto getTrackDetails(UUID trackId);
}
