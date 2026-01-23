package ua.notion.musiclibrary.dto.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ua.notion.musiclibrary.domain.exception.EntityValidationException;

public record UserUpdateDto(
        String newUsername,
        String newEmail,
        String newPassword,
        String currentPassword) {

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private static final String PASSWORD_REGEX = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$";

    public UserUpdateDto {
        Map<String, List<String>> errors = new HashMap<>();

        if (newUsername == null || newUsername.isBlank()) {
            addError(errors, "newUsername", "Ім'я не може бути порожнім!");
        } else if (newUsername.length() < 3 || newUsername.length() > 15) {
            addError(errors, "newUsername", "Ім'я не може бути менше 3 і більше 15 символів!");
        }

        if (newEmail == null || newEmail.isBlank()) {
            addError(errors, "newEmail", "Емейл не може бути порожнім!");
        } else if (!newEmail.matches(EMAIL_REGEX)) {
            addError(errors, "newEmail", "Неправильний формат емейлу!");
        }

        if (newPassword == null || newPassword.isBlank()) {
            addError(errors, "newPassword", "Новий пароль не може бути порожнім!");
        } else if (!newPassword.matches(PASSWORD_REGEX)) {
            addError(errors, "newPassword",
                    "Пароль має містити мінімум 8 символів, цифру, велику літеру та спецсимвол!");
        }

        if (currentPassword == null || currentPassword.isBlank()) {
            addError(errors, "currentPassword", "Старий пароль не може бути порожнім!");
        }

        if (!errors.isEmpty()) {
            throw new EntityValidationException(errors);
        }
    }

    private void addError(Map<String, List<String>> errors, String field, String message) {
        errors.computeIfAbsent(field, k -> new ArrayList<>()).add(message);
    }
}
