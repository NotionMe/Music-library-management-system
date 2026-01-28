package ua.notion.musiclibrary.dto.auth;

import java.util.Objects;

import ua.notion.musiclibrary.domain.enums.Role;

public record UserRegistrationDto(
        String username,
        String email,
        String password,
        Role role) {
    public UserRegistrationDto {
        Objects.requireNonNull(username);
        Objects.requireNonNull(email);
        Objects.requireNonNull(password);
        Objects.requireNonNull(role);
    }
}