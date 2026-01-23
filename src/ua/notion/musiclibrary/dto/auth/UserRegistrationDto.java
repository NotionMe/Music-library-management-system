package ua.notion.musiclibrary.dto.auth;

import ua.notion.musiclibrary.domain.enums.Role;

public record UserRegistrationDto(String username, String email, String password, Role role) {
}