package ua.notion.musiclibrary.domain.enums;

import java.util.UUID;

public enum Role {
  USER("користувач"),
  ADMIN("адміністратор"),
  PREMIUM("преміум");

  private String name;
  private UUID id;

  private Role(String name) {
    this.name = name;
    this.id = UUID.randomUUID();
  }

  public String getName() {
    return name;
  }

}
