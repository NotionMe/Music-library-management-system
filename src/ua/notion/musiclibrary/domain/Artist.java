package ua.notion.musiclibrary.domain;

import ua.notion.musiclibrary.utils.EntityValidationException;
import ua.notion.musiclibrary.utils.ValidationError;

public class Artist extends BaseEntity {

  private static final String STAGE_NAME = "stageName";
  private static final String BIO = "bio";

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
    clearError(STAGE_NAME);

    if (stageName == null || stageName.trim().isEmpty()) {
      addError(STAGE_NAME, ValidationError.EMPTY_ARTIST.getMessage());
    }

    if (stageName.length() < 1 || stageName.length() > 50) {
      addError(STAGE_NAME, ValidationError.INVALID_ARTIST_LENGTH.getMessage());
    }

    this.stageName = stageName;
  }

  public String getBio() {
    return bio;
  }

  public void setBio(String bio) {
    clearError(BIO);

    if (bio == null || bio.trim().isEmpty()) {
      addError(BIO, "Біографія не може бути пустою!");
    }

    if (bio.length() > 500) {
      addError(BIO, "Біографія не може бути більше 500 символів!");
    }

    this.bio = bio;
  }

  @Override
  public String toString() {
    return "Artist [stageName=" + stageName + ", bio=" + bio + "]";
  }
}