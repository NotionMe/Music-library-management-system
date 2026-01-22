package ua.notion.musiclibrary.domain.impl;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

import ua.notion.musiclibrary.domain.enums.Role;
import ua.notion.musiclibrary.domain.exception.EntityValidationException;
import ua.notion.musiclibrary.domain.util.DomainFieldNames;
import ua.notion.musiclibrary.domain.util.ValidationError;

public class User extends BaseEntity {

  private static final Pattern EMAIL_PATTERN = Pattern.compile(
      "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

  private String username;
  private String email;
  private String password;
  private Role role;

  private Set<UUID> followedUserIds;
  private Set<UUID> followedArtistIds;
  private Set<UUID> followedPlaylistIds;
  private Set<UUID> likedTrackIds;
  private Set<UUID> savedAlbumIds;

  private User() {
    super();
    this.followedUserIds = new HashSet<>();
    this.followedArtistIds = new HashSet<>();
    this.followedPlaylistIds = new HashSet<>();
    this.likedTrackIds = new HashSet<>();
    this.savedAlbumIds = new HashSet<>();
  }

  public User(String username, String email, String password, Role role) {
    this();
    setUsername(username);
    setEmail(email);
    setPassword(password);
    setRole(role);

    if (!isValid()) {
      throw new EntityValidationException(getErrors());
    }
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    if (username == null || username.trim().isEmpty()) {
      addError(DomainFieldNames.User.USERNAME, ValidationError.EMPTY_NAME.getMessage());
    } else if (username.length() < 3 || username.length() > 50) {
      addError(DomainFieldNames.User.USERNAME, ValidationError.USERNAME_LENGTH_INVALID.getMessage());
    }

    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    if (email == null || email.trim().isEmpty()) {
      addError(DomainFieldNames.User.EMAIL, ValidationError.EMPTY_EMAIL.getMessage());
    } else if (!EMAIL_PATTERN.matcher(email).matches()) {
      addError(DomainFieldNames.User.EMAIL, ValidationError.INVALID_EMAIL_FORMAT.getMessage());
    }

    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    if (password == null || password.length() < 6) {
      addError(DomainFieldNames.User.PASSWORD, ValidationError.PASSWORD_TOO_SHORT.getMessage());
    }

    this.password = password;
  }

  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    if (role == null) {
      addError(DomainFieldNames.User.ROLE, ValidationError.EMPTY_ROLE.getMessage());
    }

    this.role = role;
  }

  public void followUser(UUID userId) {
    if (userId != null && !userId.equals(getID())) {
      followedUserIds.add(userId);
    }
  }

  public void followArtist(UUID artistId) {
    if (artistId != null) {
      followedArtistIds.add(artistId);
    }
  }

  public void followPlaylist(UUID playlistId) {
    if (playlistId != null) {
      followedPlaylistIds.add(playlistId);
    }
  }

  public void likeTrack(UUID trackId) {
    if (trackId != null) {
      likedTrackIds.add(trackId);
    }
  }

  public void saveAlbum(UUID albumId) {
    if (albumId != null) {
      savedAlbumIds.add(albumId);
    }
  }

  public Set<UUID> getFollowedUserIds() {
    return new HashSet<>(followedUserIds);
  }

  public Set<UUID> getFollowedArtistIds() {
    return new HashSet<>(followedArtistIds);
  }

  public Set<UUID> getFollowedPlaylistIds() {
    return new HashSet<>(followedPlaylistIds);
  }

  public Set<UUID> getLikedTrackIds() {
    return new HashSet<>(likedTrackIds);
  }

  public Set<UUID> getSavedAlbumIds() {
    return new HashSet<>(savedAlbumIds);
  }

  @Override
  public String toString() {
    return "User [username=" + username + ", email=" + email + ", role=" + role + "]";
  }
}
