package ua.notion.musiclibrary.domain;

import ua.notion.musiclibrary.utils.EntityValidationException;
import ua.notion.musiclibrary.utils.ValidationError;
import java.time.Duration;
import java.util.UUID;

public class Track extends BaseEntity {

  private static final String TITLE = "title";
  private static final String DURATION = "duration";

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
    clearError(TITLE);

    if (title == null || title.trim().isEmpty()) {
      addError(TITLE, ValidationError.EMPTY_TITLE.getMessage());
    }

    if (title.length() < 1 || title.length() > 100) {
      addError(TITLE, ValidationError.INVALID_TITLE_LENGTH.getMessage());
    }

    this.title = title;
  }

  public Duration getDuration() {
    return duration;
  }

  public void setDuration(Duration duration) {
    clearError(DURATION);

    if (duration == null) {
      addError(DURATION, ValidationError.EMPTY_DURATION.getMessage());
    }

    if (duration.isZero() || duration.isNegative()) {
      addError(DURATION, ValidationError.INVALID_DURATION_FORMAT.getMessage());
    }

    this.duration = duration;
  }

  public UUID getAlbumId() {
    return albumId;
  }

  public void setAlbumId(UUID albumId) {
    if (albumId == null) {
      addError("albumId", "ID альбому не може бути пустим!");
    }

    this.albumId = albumId;
  }

  public UUID getGenreId() {
    return genreId;
  }

  public void setGenreId(UUID genreId) {
    if (genreId == null) {
      addError("genreId", "ID жанру не може бути пустим!");
    }

    this.genreId = genreId;
  }

  @Override
  public String toString() {
    return "Track [title=" + title + ", duration=" + duration + "]";
  }
}