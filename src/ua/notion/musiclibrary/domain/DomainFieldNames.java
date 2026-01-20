package ua.notion.musiclibrary.domain;

public final class DomainFieldNames {

  private DomainFieldNames() {
    throw new UnsupportedOperationException("Utility class cannot be instantiated");
  }

  public static final class Common {
    public static final String ID = "id";
    public static final String TITLE = "title";
    public static final String NAME = "name";
    public static final String USER_ID = "userId";
    public static final String TRACK_ID = "trackId";
    public static final String ALBUM_ID = "albumId";
    public static final String ARTIST_ID = "artistId";
    public static final String GENRE_ID = "genreId";
  }

  public static final class User {
    public static final String USERNAME = "username";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String ROLE = "role";
  }

  public static final class Album {
    public static final String RELEASE_DATE = "releaseDate";
  }

  public static final class Artist {
    public static final String STAGE_NAME = "stageName";
    public static final String BIO = "bio";
  }

  public static final class Track {
    public static final String DURATION = "duration";
  }

  public static final class Genre {
    public static final String GENRE_NAME = "name";
  }

  public static final class Favorite {
    public static final String ADDED_AT = "addedAt";
  }

  public static final class ListeningHistory {
    public static final String PLAYED_AT = "playedAt";
  }

  public static final class UserCollection {
    public static final String COLLECTION_NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String GROUPS = "groups";
  }
}
