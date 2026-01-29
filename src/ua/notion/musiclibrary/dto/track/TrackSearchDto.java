package ua.notion.musiclibrary.dto.track;

public record TrackSearchDto(
        String query,
        String genreFilter,
        String artistFilter) {
}
