package ua.notion.musiclibrary.domain;

import ua.notion.musiclibrary.domain.exception.EntityValidationException;
import ua.notion.musiclibrary.utils.ValidationError;

public class Artist extends BaseEntity {

  private String stageName;
  private String bio;

  private Artist() {
    super();
  }

  public Artist(String stageName, String bio) {
    this();
    setStageName(stageName);
    setBio(bio);

    if (!isValid()) {
      throw new EntityValidationException(getErrors());
    }
  }

  public String getStageName() {
    return stageName;
  }

  public void setStageName(String stageName) {
    clearError(DomainFieldNames.Artist.STAGE_NAME);

    if (stageName == null || stageName.trim().isEmpty()) {
      addError(DomainFieldNames.Artist.STAGE_NAME, ValidationError.EMPTY_ARTIST.getMessage());
    }

    if (stageName.length() < 1 || stageName.length() > 50) {
      addError(DomainFieldNames.Artist.STAGE_NAME, ValidationError.INVALID_ARTIST_LENGTH.getMessage());
    }

    this.stageName = stageName;
  }

  public String getBio() {
    return bio;
  }

  public void setBio(String bio) {
    clearError(DomainFieldNames.Artist.BIO);

    if (bio == null || bio.trim().isEmpty()) {
      addError(DomainFieldNames.Artist.BIO, ValidationError.EMPTY_BIO.getMessage());
    }

    if (bio.length() > 500) {
      addError(DomainFieldNames.Artist.BIO, ValidationError.INVALID_BIO_LENGTH.getMessage());
    }

    this.bio = bio;
  }

  @Override
  public String toString() {
    return "Artist [stageName=" + stageName + ", bio=" + bio + "]";
  }
}
