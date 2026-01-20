package ua.notion.musiclibrary.utils;

public enum ValidationError {
  // for USER
  EMPTY_NAME("Ім'я не може бути пустим!"),
  EMPTY_EMAIL("Емаіл не може бути пустим!"),
  INVALID_EMAIL_FORMAT("Неправильний формат емайлу!"),
  EMPTY_PASSWORD("Пароль не може бути пустим!"),
  EMPTY_ROLE("Роль не може бути пуста!"),
  EMPTY_TITLE("Назва не може бути пустою!"),
  INVALID_TITLE_LENGTH("Назва не може бути менше 1 символу та більше 100!"),
  EMPTY_ARTIST("Виконавець не може бути пустим!"),
  INVALID_ARTIST_LENGTH("Виконавець не може бути менше 1 символу та більше 50!"),
  EMPTY_DURATION("Тривалість не може бути пустою!"),
  INVALID_DURATION_FORMAT("Неправильний формат тривалості!"),
  EMPTY_GENRE("Жанр не може бути пустим!"),
  INVALID_GENRE_LENGTH("Жанр не може бути менше 1 символу та більше 30!"),

  // for IDs and UUIDs
  EMPTY_USER_ID("ID користувача не може бути пустим!"),
  EMPTY_TRACK_ID("ID треку не може бути пустим!"),
  EMPTY_PLAYLIST_ID("ID плейлиста не може бути пустим!"),
  EMPTY_ALBUM_ID("ID альбому не може бути пустим!"),
  EMPTY_GENRE_ID("ID жанру не може бути пустим!"),
  EMPTY_ARTIST_ID("ID виконавця не може бути пустим!"),

  // for dates
  INVALID_DATE_IN_FUTURE("Дата не може бути в майбутньому!"),
  EMPTY_DATE("Дата не може бути пустою!"),
  EMPTY_RELEASE_DATE("Дата релізу не може бути пустою!"),
  INVALID_RELEASE_DATE_IN_FUTURE("Дата релізу не може бути в майбутньому!"),

  // for time fields
  EMPTY_ADDED_AT("Час додавання не може бути пустим!"),
  INVALID_ADDED_AT_IN_FUTURE("Час додавання не може бути в майбутньому!"),
  EMPTY_PLAYED_AT("Час прослуховування не може бути пустим!"),
  INVALID_PLAYED_AT_IN_FUTURE("Час прослуховування не може бути в майбутньому!"),

  // for bio fields
  EMPTY_BIO("Біографія не може бути пустою!"),
  INVALID_BIO_LENGTH("Біографія не може бути більше 500 символів!"),

  // for specific validations
  PASSWORD_TOO_SHORT("Пароль повинен містити щонайменше 6 символів!"),
  USERNAME_LENGTH_INVALID("Ім'я користувача повинно бути від 3 до 50 символів!");

  private final String message;

  ValidationError(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }
}
