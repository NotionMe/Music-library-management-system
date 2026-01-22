package ua.notion.musiclibrary.domain.model;

import ua.notion.musiclibrary.utils.DomainFieldNames;
import static ua.notion.musiclibrary.utils.DomainFieldNames.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import ua.notion.musiclibrary.domain.exception.EntityValidationException;
import ua.notion.musiclibrary.utils.ValidationError;

public class UserCollection extends BaseEntity {

  private UUID userId;
  private String name;
  private String description;
  private String groups;
  private Set<UUID> playlistIds;

  private UserCollection() {
    super();
    this.playlistIds = new HashSet<>();
  }

  public UserCollection(UUID userId, String name, String description, String groups) {
    this();
    setUserId(userId);
    setName(name);
    setDescription(description);
    setGroups(groups);

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

  public String getName() {
    return name;
  }

  public void setName(String name) {
    clearError(DomainFieldNames.UserCollection.COLLECTION_NAME);

    if (name == null || name.trim().isEmpty()) {
      addError(DomainFieldNames.UserCollection.COLLECTION_NAME, ValidationError.EMPTY_TITLE.getMessage());
    }

    if (name.length() < 1 || name.length() > 100) {
      addError(DomainFieldNames.UserCollection.COLLECTION_NAME, ValidationError.INVALID_TITLE_LENGTH.getMessage());
    }

    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getGroups() {
    return groups;
  }

  public void setGroups(String groups) {
    this.groups = groups;
  }

  public Set<UUID> getPlaylistIds() {
    return new HashSet<>(playlistIds);
  }

  public void addPlaylist(UUID playlistId) {
    if (playlistId != null) {
      this.playlistIds.add(playlistId);
    }
  }

  public void removePlaylist(UUID playlistId) {
    this.playlistIds.remove(playlistId);
  }

  public void clearPlaylists() {
    this.playlistIds.clear();
  }

  @Override
  public String toString() {
    return "UserCollection [name=" + name + ", description=" + description +
        ", playlistCount=" + playlistIds.size() + "]";
  }
}
