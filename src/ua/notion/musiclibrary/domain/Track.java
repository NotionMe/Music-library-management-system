package ua.notion.musiclibrary.domain;

import java.time.Duration;
import java.util.UUID;

import ua.notion.musiclibrary.utils.EntityValidationException;
import ua.notion.musiclibrary.utils.ValidationError;

public class Track extends BaseEntity {

  private String title;
  private Duration duration;
  private UUID albumId;
  private UUID genreId;

  private Track() {
    super();
  }

  public Track(String title, Duration duration, UUID albumId, UUID genreId) {
    this();
    setTitle(title);
    setDuration(duration);
    setAlbumId(albumId);
    setGenreId(genreId);

    if (!isValid()) {
      throw new EntityValidationException(getErrors());
    }
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    clearError(DomainFieldNames.Common.TITLE);

    if (title == null || title.trim().isEmpty()) {
      addError(DomainFieldNames.Common.TITLE, ValidationError.EMPTY_TITLE.getMessage());
    }

    if (title.length() < 1 || title.length() > 100) {
      addError(DomainFieldNames.Common.TITLE, ValidationError.INVALID_TITLE_LENGTH.getMessage());
    }

    this.title = title;
  }

  public Duration getDuration() {
    return duration;
  }

  public void setDuration(Duration duration) {
    clearError(DomainFieldNames.Track.DURATION);

    if (duration == null) {
      addError(DomainFieldNames.Track.DURATION, ValidationError.EMPTY_DURATION.getMessage());
    }

    if (duration.isZero() || duration.isNegative()) {
      addError(DomainFieldNames.Track.DURATION, ValidationError.INVALID_DURATION_FORMAT.getMessage());
    }

    this.duration = duration;
  }

  public UUID getAlbumId() {
    return albumId;
  }

  public void setAlbumId(UUID albumId) {
    if (albumId == null) {
      addError(DomainFieldNames.Common.ALBUM_ID, ValidationError.EMPTY_ALBUM_ID.getMessage());
    }

    this.albumId = albumId;
  }

  public UUID getGenreId() {
    return genreId;
  }

  public void setGenreId(UUID genreId) {
    if (genreId == null) {
      addError(DomainFieldNames.Common.GENRE_ID, ValidationError.EMPTY_GENRE_ID.getMessage());
    }

    this.genreId = genreId;
  }

  @Override
  public String toString() {
    return "Track [title=" + title + ", duration=" + duration + "]";
  }
}
