package ua.notion.musiclibrary.domain.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public abstract class BaseEntity implements Entity {
  private final UUID id;
  protected Map<String, List<String>> errors;

  protected BaseEntity() {
    this.id = UUID.randomUUID();
    this.errors = new HashMap<>();
  }

  protected void addError(String field, String message) {
    this.errors.computeIfAbsent(field, k -> new ArrayList<>()).add(message);
  }

  protected void clearError(String field) {
    this.errors.remove(field);
  }

  public Map<String, List<String>> getErrors() {
    return new HashMap<>(errors);
  }

  public boolean isValid() {
    return errors.isEmpty();
  }

  @Override
  public UUID getID() {
    return id;
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null || getClass() != obj.getClass())
      return false;

    BaseEntity entity = (BaseEntity) obj;
    return id.equals(entity.id);
  }
}
