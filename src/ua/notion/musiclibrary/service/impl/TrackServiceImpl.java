package ua.notion.musiclibrary.service.impl;

import java.util.List;
import java.util.UUID;

import ua.notion.musiclibrary.domain.impl.Track;
import ua.notion.musiclibrary.dto.track.TrackDto;
import ua.notion.musiclibrary.dto.track.TrackSearchDto;
import ua.notion.musiclibrary.infrastructure.storage.impl.DataContext;
import ua.notion.musiclibrary.mapper.TrackMapper;
import ua.notion.musiclibrary.service.contract.TrackService;

public class TrackServiceImpl implements TrackService {

    private final DataContext dataContext;

    public TrackServiceImpl(DataContext dataContext) {
        this.dataContext = dataContext;
    }

    @Override
    public List<TrackDto> searchTracks(TrackSearchDto searchDto) {
        List<Track> tracks = dataContext.tracks().findAll();

        return tracks.stream()
                .filter(track -> matchesSearchCriteria(track, searchDto))
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public TrackDto getTrackDetails(UUID trackId) {
        Track track = dataContext.tracks().findById(trackId)
                .orElseThrow(() -> new IllegalArgumentException("Track not found"));

        return mapToDto(track);
    }

    private boolean matchesSearchCriteria(Track track, TrackSearchDto searchDto) {
        if (searchDto.query() != null && !searchDto.query().isBlank()) {
            if (!track.getTitle().toLowerCase().contains(searchDto.query().toLowerCase())) {
                return false;
            }
        }

        if (searchDto.genreFilter() != null && !searchDto.genreFilter().isBlank()) {
            boolean hasGenre = dataContext.genres().findAll().stream()
                    .filter(genre -> genre.getName().equalsIgnoreCase(searchDto.genreFilter()))
                    .anyMatch(genre -> track.getGenreIds().contains(genre.getID()));
            if (!hasGenre) {
                return false;
            }
        }

        if (searchDto.artistFilter() != null && !searchDto.artistFilter().isBlank()) {
            boolean hasArtist = dataContext.artists().findAll().stream()
                    .filter(artist -> artist.getStageName().equalsIgnoreCase(searchDto.artistFilter()))
                    .anyMatch(artist -> track.getArtistIds().contains(artist.getID()));
            if (!hasArtist) {
                return false;
            }
        }

        return true;
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
