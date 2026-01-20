package ua.notion.musiclibrary.domain;

import ua.notion.musiclibrary.utils.EntityValidationException;
import java.time.LocalDateTime;
import java.util.UUID;

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
      addError("userId", "ID користувача не може бути пустим!");
    }

    this.userId = userId;
  }

  public UUID getTrackId() {
    return trackId;
  }

  public void setTrackId(UUID trackId) {
    if (trackId == null) {
      addError("trackId", "ID треку не може бути пустим!");
    }

    this.trackId = trackId;
  }

  public LocalDateTime getPlayedAt() {
    return playedAt;
  }

  public void setPlayedAt(LocalDateTime playedAt) {
    if (playedAt == null) {
      addError("playedAt", "Час прослуховування не може бути пустим!");
    }

    if (playedAt.isAfter(LocalDateTime.now())) {
      addError("playedAt", "Час прослуховування не може бути в майбутньому!");
    }

    this.playedAt = playedAt;
  }

  @Override
  public String toString() {
    return "ListeningHistory [userId=" + userId + ", trackId=" + trackId + ", playedAt=" + playedAt + "]";
  }
}