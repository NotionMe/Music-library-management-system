package ua.notion.musiclibrary.domain.impl;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import ua.notion.musiclibrary.domain.exception.EntityValidationException;
import ua.notion.musiclibrary.domain.util.DomainFieldNames;
import ua.notion.musiclibrary.domain.util.DomainFieldNames.Common;
import ua.notion.musiclibrary.domain.util.ValidationError;

public class Track extends BaseEntity {

  private String title;
  private Duration duration;
  private UUID albumId;
  private Set<UUID> genreIds;
  private Set<UUID> artistIds;

  private Track() {
    super();
    this.genreIds = new HashSet<>();
    this.artistIds = new HashSet<>();
  }

  public Track(String title, Duration duration, UUID albumId) {
    this();
    setTitle(title);
    setDuration(duration);
    setAlbumId(albumId);

    if (!isValid()) {
      throw new EntityValidationException(getErrors());
    }
  }

  public void addGenre(UUID genreId) {
    if (genreId != null) {
      this.genreIds.add(genreId);
    }
  }

  public void addArtist(UUID artistId) {
    if (artistId != null) {
      this.artistIds.add(artistId);
    }
  }

  public Set<UUID> getGenreIds() {
    return new HashSet<>(genreIds);
  }

  public Set<UUID> getArtistIds() {
    return new HashSet<>(artistIds);
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    clearError(Common.TITLE);

    if (title == null || title.trim().isEmpty()) {
      addError(Common.TITLE, ValidationError.EMPTY_TITLE.getMessage());
    }

    if (title.length() < 1 || title.length() > 100) {
      addError(Common.TITLE, ValidationError.INVALID_TITLE_LENGTH.getMessage());
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
      addError(Common.ALBUM_ID, ValidationError.EMPTY_ALBUM_ID.getMessage());
    }

    this.albumId = albumId;
  }

  @Override
  public String toString() {
    return "Track [title=" + title + ", duration=" + duration + "]";
  }
}
