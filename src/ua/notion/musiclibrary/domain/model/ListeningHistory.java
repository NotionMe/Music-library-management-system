package ua.notion.musiclibrary.domain.model;

import ua.notion.musiclibrary.utils.DomainFieldNames;
import static ua.notion.musiclibrary.utils.DomainFieldNames.*;
import java.time.LocalDateTime;
import java.util.UUID;

import ua.notion.musiclibrary.domain.exception.EntityValidationException;
import ua.notion.musiclibrary.utils.ValidationError;

public class ListeningHistory extends BaseEntity {

  private UUID userId;
  private UUID trackId;
  private LocalDateTime playedAt;

  private ListeningHistory() {
    super();
  }

  public ListeningHistory(UUID userId, UUID trackId, LocalDateTime playedAt) {
    this();
    setUserId(userId);
    setTrackId(trackId);
    setPlayedAt(playedAt);

    if (!isValid()) {
      throw new EntityValidationException(getErrors());
    }
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

  public UUID getTrackId() {
    return trackId;
  }

  public void setTrackId(UUID trackId) {
    if (trackId == null) {
      addError(Common.TRACK_ID, ValidationError.EMPTY_TRACK_ID.getMessage());
    }

    this.trackId = trackId;
  }

  public LocalDateTime getPlayedAt() {
    return playedAt;
  }

  public void setPlayedAt(LocalDateTime playedAt) {
    if (playedAt == null) {
      addError(DomainFieldNames.ListeningHistory.PLAYED_AT, ValidationError.EMPTY_PLAYED_AT.getMessage());
    }

    if (playedAt.isAfter(LocalDateTime.now())) {
      addError(DomainFieldNames.ListeningHistory.PLAYED_AT, ValidationError.INVALID_PLAYED_AT_IN_FUTURE.getMessage());
    }

    this.playedAt = playedAt;
  }

  @Override
  public String toString() {
    return "ListeningHistory [userId=" + userId + ", trackId=" + trackId + ", playedAt=" + playedAt + "]";
  }
}
