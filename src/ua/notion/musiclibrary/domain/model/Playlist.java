package ua.notion.musiclibrary.domain.model;

import static ua.notion.musiclibrary.utils.DomainFieldNames.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import ua.notion.musiclibrary.domain.exception.EntityValidationException;
import ua.notion.musiclibrary.utils.ValidationError;

public class Playlist extends BaseEntity {

  private String name;
  private boolean isPrivate;
  private UUID userId;
  private Set<UUID> trackIds;

  private Playlist() {
    super();
    this.trackIds = new HashSet<>();
  }

  public Playlist(String name, boolean isPrivate, UUID userId) {
    this();
    setName(name);
    setPrivate(isPrivate);
    setUserId(userId);

    if (!isValid()) {
      throw new EntityValidationException(getErrors());
    }
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    clearError(Common.NAME);

    if (name == null || name.trim().isEmpty()) {
      addError(Common.NAME, ValidationError.EMPTY_TITLE.getMessage());
    }

    if (name.length() < 1 || name.length() > 100) {
      addError(Common.NAME, ValidationError.INVALID_TITLE_LENGTH.getMessage());
    }

    this.name = name;
  }

  public boolean isPrivate() {
    return isPrivate;
  }

  public void setPrivate(boolean isPrivate) {
    this.isPrivate = isPrivate;
  }

  public UUID getUserId() {
    return userId;
  }

  public void setUserId(UUID userId) {
    if (userId == null) {
      addError(Common.USER_ID, ValidationError.EMPTY_USER_ID.getMessage());
    }

    this.userId = userId;
  }

  public Set<UUID> getTrackIds() {
    return new HashSet<>(trackIds);
  }

  public void addTrack(UUID trackId) {
    if (trackId != null) {
      this.trackIds.add(trackId);
    }
  }

  public void removeTrack(UUID trackId) {
    this.trackIds.remove(trackId);
  }

  public void clearTracks() {
    this.trackIds.clear();
  }

  @Override
  public String toString() {
    return "Playlist [name=" + name + ", isPrivate=" + isPrivate + ", trackCount=" + trackIds.size() + "]";
  }
}
