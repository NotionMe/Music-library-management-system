package ua.notion.musiclibrary.domain;

import java.time.LocalDate;
import java.util.UUID;

import ua.notion.musiclibrary.utils.EntityValidationException;
import ua.notion.musiclibrary.utils.ValidationError;

public class Album extends BaseEntity {

  private String title;
  private LocalDate releaseDate;
  private UUID artistId;

  private Album() {
    super();
  }

  public Album(String title, LocalDate releaseDate, UUID artistId) {
    this();
    setTitle(title);
    setReleaseDate(releaseDate);
    setArtistId(artistId);

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

  public LocalDate getReleaseDate() {
    return releaseDate;
  }

  public void setReleaseDate(LocalDate releaseDate) {
    clearError(DomainFieldNames.Album.RELEASE_DATE);

    if (releaseDate == null) {
      addError(DomainFieldNames.Album.RELEASE_DATE, ValidationError.EMPTY_RELEASE_DATE.getMessage());
    }

    if (releaseDate.isAfter(LocalDate.now())) {
      addError(DomainFieldNames.Album.RELEASE_DATE, ValidationError.INVALID_RELEASE_DATE_IN_FUTURE.getMessage());
    }

    this.releaseDate = releaseDate;
  }

  public UUID getArtistId() {
    return artistId;
  }

  public void setArtistId(UUID artistId) {
    if (artistId == null) {
      addError(DomainFieldNames.Common.ARTIST_ID, ValidationError.EMPTY_ARTIST_ID.getMessage());
    }

    this.artistId = artistId;
  }

  @Override
  public String toString() {
    return "Album [title=" + title + ", releaseDate=" + releaseDate + "]";
  }
}
