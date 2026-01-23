package ua.notion.musiclibrary.dto.user;

import java.util.Collections;

import ua.notion.musiclibrary.domain.enums.Role;
import ua.notion.musiclibrary.domain.exception.EntityValidationException;

public record UserProfileDto(String username, String email, String password, Role role) {
    public UserProfileDto {
        if (username == null && username.trim().isBlank()) {
            throw new EntityValidationException(
                    Collections.singletonMap("username", Collections.singletonList("username is required!")));
        }
        if (email == null && email.trim().isBlank()) {
            throw new EntityValidationException(
                    Collections.singletonMap("email", Collections.singletonList("email is required!")));
        }
        if (password == null || password.isBlank()) {
            throw new EntityValidationException(
                    Collections.singletonMap("password", Collections.singletonList("Password is required!")));
        }
        if (role == null) {
            throw new EntityValidationException(
                    Collections.singletonMap("role", Collections.singletonList("Role is required!")));
        }
    }

}
