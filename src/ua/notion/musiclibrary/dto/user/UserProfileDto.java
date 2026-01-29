package ua.notion.musiclibrary.dto.user;

import ua.notion.musiclibrary.domain.enums.Role;

public record UserProfileDto(String username, String email, String password, Role role) {
}
