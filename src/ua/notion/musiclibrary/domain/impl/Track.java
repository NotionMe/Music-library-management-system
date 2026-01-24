package ua.notion.musiclibrary.domain.impl;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import ua.notion.musiclibrary.domain.enums.AudioFormat;
import ua.notion.musiclibrary.domain.exception.EntityValidationException;
import ua.notion.musiclibrary.domain.util.DomainFieldNames;
import ua.notion.musiclibrary.domain.util.DomainFieldNames.Common;
import ua.notion.musiclibrary.domain.util.ValidationError;

public class Track extends BaseEntity {

  private String title;
  private Duration duration;
  private UUID albumId;
  private Set<UUID> genreIds;
  private Set<UUID> artistIds;

  private String filePath;
  private String fileHash;
  private Long fileSizeBytes;
  private AudioFormat audioFormat;
  private Integer bitrate;

  private Track() {
    super();
    this.genreIds = new HashSet<>();
    this.artistIds = new HashSet<>();
  }

  public Track(String title, Duration duration, UUID albumId, String filePath) {
    this();
    setTitle(title);
    setDuration(duration);
    setAlbumId(albumId);
    setFilePath(filePath);

    if (!isValid()) {
      throw new EntityValidationException(getErrors());
    }
  }

  public void addGenre(UUID genreId) {
    if (genreId != null) {
      this.genreIds.add(genreId);
    }
  }

  public void addArtist(UUID artistId) {
    if (artistId != null) {
      this.artistIds.add(artistId);
    }
  }

  public Set<UUID> getGenreIds() {
    return new HashSet<>(genreIds);
  }

  public Set<UUID> getArtistIds() {
    return new HashSet<>(artistIds);
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    clearError(Common.TITLE);

    if (title == null || title.trim().isEmpty()) {
      addError(Common.TITLE, ValidationError.EMPTY_TITLE.getMessage());
    }

    if (title.length() < 1 || title.length() > 100) {
      addError(Common.TITLE, ValidationError.INVALID_TITLE_LENGTH.getMessage());
    }

    this.title = title;
  }

  public Duration getDuration() {
    return duration;
  }

  public void setDuration(Duration duration) {
    clearError(DomainFieldNames.Track.DURATION);

    if (duration == null) {
      addError(DomainFieldNames.Track.DURATION, ValidationError.EMPTY_DURATION.getMessage());
    }

    if (duration.isZero() || duration.isNegative()) {
      addError(DomainFieldNames.Track.DURATION, ValidationError.INVALID_DURATION_FORMAT.getMessage());
    }

    this.duration = duration;
  }

  public UUID getAlbumId() {
    return albumId;
  }

  public void setAlbumId(UUID albumId) {
    if (albumId == null) {
      addError(Common.ALBUM_ID, ValidationError.EMPTY_ALBUM_ID.getMessage());
    }

    this.albumId = albumId;
  }

  public String getFilePath() {
    return filePath;
  }

  public void setFilePath(String filePath) {
    clearError(DomainFieldNames.Track.FILE_PATH);

    if (filePath == null || filePath.isBlank()) {
      addError(DomainFieldNames.Track.FILE_PATH, ValidationError.EMPTY_FILE_PATH.getMessage());
    }

    this.filePath = filePath;
  }

  public String getFileHash() {
    return fileHash;
  }

  public void setFileHash(String fileHash) {
    clearError(DomainFieldNames.Track.FILE_HASH);

    if (fileHash != null && fileHash.isBlank()) {
      addError(DomainFieldNames.Track.FILE_HASH, ValidationError.EMPTY_FILE_HASH.getMessage());
    }

    this.fileHash = fileHash;
  }

  public Long getFileSizeBytes() {
    return fileSizeBytes;
  }

  public void setFileSizeBytes(Long fileSizeBytes) {
    clearError(DomainFieldNames.Track.FILE_SIZE_BYTES);

    if (fileSizeBytes != null && fileSizeBytes <= 0) {
      addError(DomainFieldNames.Track.FILE_SIZE_BYTES, ValidationError.INVALID_FILE_SIZE.getMessage());
    }

    this.fileSizeBytes = fileSizeBytes;
  }

  public AudioFormat getAudioFormat() {
    return audioFormat;
  }

  public void setAudioFormat(AudioFormat audioFormat) {
    clearError(DomainFieldNames.Track.AUDIO_FORMAT);

    if (audioFormat == null && filePath != null) {
      addError(DomainFieldNames.Track.AUDIO_FORMAT, ValidationError.EMPTY_AUDIO_FORMAT.getMessage());
    }

    this.audioFormat = audioFormat;
  }

  public Integer getBitrate() {
    return bitrate;
  }

  public void setBitrate(Integer bitrate) {
    clearError(DomainFieldNames.Track.BITRATE);

    if (bitrate != null && bitrate <= 0) {
      addError(DomainFieldNames.Track.BITRATE, ValidationError.INVALID_BITRATE.getMessage());
    }

    this.bitrate = bitrate;
  }

  public boolean hasAudioFile() {
    return filePath != null && !filePath.isBlank();
  }

  @Override
  public String toString() {
    return "Track [title=" + title + ", duration=" + duration + ", hasAudioFile=" + hasAudioFile() + "]";
  }
}
