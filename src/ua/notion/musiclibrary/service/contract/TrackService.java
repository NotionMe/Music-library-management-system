package ua.notion.musiclibrary.service.contract;

import ua.notion.musiclibrary.dto.track.TrackCreateDto;
import ua.notion.musiclibrary.dto.track.TrackDto;
import ua.notion.musiclibrary.dto.track.TrackSearchDto;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface TrackService {

    List<TrackDto> searchTracks(TrackSearchDto searchDto);

    TrackDto getTrackDetails(UUID trackId);

    TrackDto createTrack(TrackCreateDto createDto) throws IOException;

    TrackDto createTrackWithFile(TrackCreateDto createDto, File audioFile) throws IOException;

    boolean deleteTrack(UUID trackId);
}
