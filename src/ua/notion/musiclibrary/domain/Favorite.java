package ua.notion.musiclibrary.domain;

import java.time.LocalDateTime;
import java.util.UUID;

import ua.notion.musiclibrary.utils.EntityValidationException;
import ua.notion.musiclibrary.utils.ValidationError;

public class Favorite extends BaseEntity {

  private UUID userId;
  private UUID trackId;
  private LocalDateTime addedAt;

  private Favorite() {
    super();
  }

  public Favorite(UUID userId, UUID trackId, LocalDateTime addedAt) {
    this();
    setUserId(userId);
    setTrackId(trackId);
    setAddedAt(addedAt);

    if (!isValid()) {
      throw new EntityValidationException(getErrors());
    }
  }

  public Favorite(UUID userId, UUID trackId) {
    this(userId, trackId, LocalDateTime.now());
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

  public UUID getTrackId() {
    return trackId;
  }

  public void setTrackId(UUID trackId) {
    if (trackId == null) {
      addError(DomainFieldNames.Common.TRACK_ID, ValidationError.EMPTY_TRACK_ID.getMessage());
    }

    this.trackId = trackId;
  }

  public LocalDateTime getAddedAt() {
    return addedAt;
  }

  public void setAddedAt(LocalDateTime addedAt) {
    if (addedAt == null) {
      addError(DomainFieldNames.Favorite.ADDED_AT, ValidationError.EMPTY_ADDED_AT.getMessage());
    }

    if (addedAt.isAfter(LocalDateTime.now())) {
      addError(DomainFieldNames.Favorite.ADDED_AT, ValidationError.INVALID_ADDED_AT_IN_FUTURE.getMessage());
    }

    this.addedAt = addedAt;
  }

  @Override
  public String toString() {
    return "Favorite [userId=" + userId + ", trackId=" + trackId + ", addedAt=" + addedAt + "]";
  }
}
