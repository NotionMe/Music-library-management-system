package ua.notion.musiclibrary.utils;

public enum ValidationError {
  // for USER
  EMPTY_NAME("Ім'я не може бути пустим!"),
  INVALID_NAME_LENGTH("Ім'я не може бути менше 2 символів та більше 50!"),
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
  INVALID_GENRE_LENGTH("Жанр не може бути менше 1 символу та більше 30!");

  private final String message;

  ValidationError(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }
}
