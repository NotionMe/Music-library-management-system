package ua.notion.musiclibrary.domain.exception;

import java.util.List;
import java.util.Map;

public class EntityValidationException extends RuntimeException {

  private final Map<String, List<String>> errors;

  public EntityValidationException(Map<String, List<String>> errors) {
    super("Валідація сутності не пройшла. Перевірте 'errors' для деталей");
    this.errors = errors;
  }

  public Map<String, List<String>> getErrors() {
    return errors;
  }

  @Override
  public String getMessage() {
    StringBuilder sb = new StringBuilder(super.getMessage());
    sb.append("\n");
    errors.forEach((field, message) -> {
      sb.append(field)
          .append(": ")
          .append(message)
          .append("\n");
    });
    return sb.toString();
  }
}
