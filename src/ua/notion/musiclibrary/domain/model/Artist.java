package ua.notion.musiclibrary.domain.model;

import java.util.UUID;

import ua.notion.musiclibrary.domain.exception.EntityValidationException;
import ua.notion.musiclibrary.utils.DomainFieldNames;
import ua.notion.musiclibrary.utils.ValidationError;

public class Artist extends BaseEntity {

  private String stageName;
  private String bio;
  private UUID userId;

  private Artist() {
    super();
  }

  public Artist(String stageName, String bio, UUID userId) {
    this();
    setStageName(stageName);
    setBio(bio);
    setUserId(userId);

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

  public UUID getUserId() {
    return userId;
  }

  public void setUserId(UUID userId) {
    if (userId == null) {
      addError(DomainFieldNames.Common.USER_ID, ValidationError.EMPTY_USER_ID.getMessage());
    }
    this.userId = userId;
  }

  @Override
  public String toString() {
    return "Artist [stageName=" + stageName + ", bio=" + bio + "]";
  }
}
