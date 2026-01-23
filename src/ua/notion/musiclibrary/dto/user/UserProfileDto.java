package ua.notion.musiclibrary.dto.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ua.notion.musiclibrary.domain.enums.Role;
import ua.notion.musiclibrary.domain.exception.EntityValidationException;
import static ua.notion.musiclibrary.domain.util.DomainFieldNames.User;

public record UserProfileDto(String username, String email, String password, Role role) {
    public UserProfileDto {
        Map<String, List<String>> errors = new HashMap<>();

        if (username == null || username.isBlank()) {
            addError(errors, User.USERNAME, "Ім'я користувача обов'язкове!");
        }
        if (email == null || email.isBlank()) {
            addError(errors, User.EMAIL, "Емейл обов'язковий!");
        }
        if (password == null || password.isBlank()) {
            addError(errors, User.PASSWORD, "Пароль обов'язковий!");
        }
        if (role == null) {
            addError(errors, User.ROLE, "Роль обов'язкова!");
        }

        if (!errors.isEmpty()) {
            throw new EntityValidationException(errors);
        }
    }

    private void addError(Map<String, List<String>> errors, String field, String message) {
        errors.computeIfAbsent(field, k -> new ArrayList<>()).add(message);
    }
}
