package ua.notion.musiclibrary.domain;

import ua.notion.musiclibrary.domain.exception.EntityValidationException;
import ua.notion.musiclibrary.utils.ValidationError;

public class Genre extends BaseEntity {

  private String name;

  private Genre() {
    super();
  }

  public Genre(String name) {
    this();
    setName(name);

    if (!isValid()) {
      throw new EntityValidationException(getErrors());
    }
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    clearError(DomainFieldNames.Genre.GENRE_NAME);

    if (name == null || name.trim().isEmpty()) {
      addError(DomainFieldNames.Genre.GENRE_NAME, ValidationError.EMPTY_GENRE.getMessage());
    }

    if (name.length() < 1 || name.length() > 30) {
      addError(DomainFieldNames.Genre.GENRE_NAME, ValidationError.INVALID_GENRE_LENGTH.getMessage());
    }

    this.name = name;
  }

  @Override
  public String toString() {
    return "Genre [name=" + name + "]";
  }
}
