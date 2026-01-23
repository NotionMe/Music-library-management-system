package ua.notion.musiclibrary.mapper;

import java.time.Duration;

import ua.notion.musiclibrary.domain.impl.Track;
import ua.notion.musiclibrary.dto.track.TrackCreateDto;
import ua.notion.musiclibrary.dto.track.TrackDto;

public final class TrackMapper {

    private TrackMapper() {
    }

    public static Track toDomain(TrackCreateDto dto) {
        Track track = new Track(
                dto.title(),
                dto.duration(),
                dto.albumId());

        if (dto.genreIds() != null) {
            dto.genreIds().forEach(track::addGenre);
        }

        if (dto.artistIds() != null) {
            dto.artistIds().forEach(track::addArtist);
        }

        return track;
    }

    public static TrackDto toDto(Track track, String artistName, String albumName) {
        return new TrackDto(
                track.getID(),
                track.getTitle(),
                formatDuration(track.getDuration()),
                artistName,
                albumName);
    }

    private static String formatDuration(Duration duration) {
        if (duration == null) {
            return "00:00";
        }
        long minutes = duration.toMinutes();
        long seconds = duration.minusMinutes(minutes).getSeconds();
        return String.format("%02d:%02d", minutes, seconds);
    }
}
