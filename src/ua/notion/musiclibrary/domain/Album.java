package ua.notion.musiclibrary.domain;

import ua.notion.musiclibrary.utils.EntityValidationException;
import ua.notion.musiclibrary.utils.ValidationError;
import java.time.LocalDate;
import java.util.UUID;

public class Album extends BaseEntity {

  private static final String TITLE = "title";
  private static final String RELEASE_DATE = "releaseDate";

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
    clearError(TITLE);

    if (title == null || title.trim().isEmpty()) {
      addError(TITLE, ValidationError.EMPTY_TITLE.getMessage());
    }

    if (title.length() < 1 || title.length() > 100) {
      addError(TITLE, ValidationError.INVALID_TITLE_LENGTH.getMessage());
    }

    this.title = title;
  }

  public LocalDate getReleaseDate() {
    return releaseDate;
  }

  public void setReleaseDate(LocalDate releaseDate) {
    clearError(RELEASE_DATE);

    if (releaseDate == null) {
      addError(RELEASE_DATE, "Дата релізу не може бути пустою!");
    }

    if (releaseDate.isAfter(LocalDate.now())) {
      addError(RELEASE_DATE, "Дата релізу не може бути в майбутньому!");
    }

    this.releaseDate = releaseDate;
  }

  public UUID getArtistId() {
    return artistId;
  }

  public void setArtistId(UUID artistId) {
    if (artistId == null) {
      addError("artistId", "ID виконавця не може бути пустим!");
    }

    this.artistId = artistId;
  }

  @Override
  public String toString() {
    return "Album [title=" + title + ", releaseDate=" + releaseDate + "]";
  }
}