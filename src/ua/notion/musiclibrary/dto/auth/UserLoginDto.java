package ua.notion.musiclibrary.dto.auth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ua.notion.musiclibrary.domain.exception.EntityValidationException;
import static ua.notion.musiclibrary.domain.util.DomainFieldNames.User;

public record UserLoginDto(String email, String password) {
    public UserLoginDto {
        Map<String, List<String>> errors = new HashMap<>();

        if (email == null || email.isBlank()) {
            addError(errors, User.EMAIL, "Емейл обов'язковий!");
        }

        if (password == null || password.isBlank()) {
            addError(errors, User.PASSWORD, "Пароль обов'язковий!");
        }

        if (!errors.isEmpty()) {
            throw new EntityValidationException(errors);
        }
    }

    private void addError(Map<String, List<String>> errors, String field, String message) {
        errors.computeIfAbsent(field, k -> new ArrayList<>()).add(message);
    }
}