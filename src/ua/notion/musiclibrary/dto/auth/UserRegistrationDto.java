package ua.notion.musiclibrary.dto.auth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ua.notion.musiclibrary.domain.enums.Role;
import ua.notion.musiclibrary.domain.exception.EntityValidationException;
import static ua.notion.musiclibrary.domain.util.DomainFieldNames.User;

public record UserRegistrationDto(String username, String email, String password, Role role) {
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private static final String PASSWORD_REGEX = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$";

    public UserRegistrationDto {
        Map<String, List<String>> errors = new HashMap<>();

        if (username == null || username.isBlank()) {
            addError(errors, User.USERNAME, "Ім'я користувача обов'язкове!");
        } else if (username.length() < 3 || username.length() > 15) {
            addError(errors, User.USERNAME, "Ім'я має бути від 3 до 15 символів!");
        }

        if (email == null || email.isBlank()) {
            addError(errors, User.EMAIL, "Емейл обов'язковий!");
        } else if (!email.matches(EMAIL_REGEX)) {
            addError(errors, User.EMAIL, "Неправильний формат емейлу!");
        }

        if (password == null || password.isBlank()) {
            addError(errors, User.PASSWORD, "Пароль обов'язковий!");
        } else if (!password.matches(PASSWORD_REGEX)) {
            addError(errors, User.PASSWORD,
                    "Пароль має містити мінімум 8 символів, цифру, велику літеру та спецсимвол!");
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