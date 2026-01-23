package ua.notion.musiclibrary.dto.user;

import java.util.Collections;

import ua.notion.musiclibrary.domain.exception.EntityValidationException;

public record UserUpdateDto(String newUsername, String newEmail, String newPassword, String currentPassword) {
    public UserUpdateDto {
        if (newUsername.length() < 3 && newUsername.length() >= 15) {
            throw new EntityValidationException(Collections.singletonMap("username",
                    Collections.singletonList("Ім'я не може бути менше 3 і більше 15!")));
        }
        if (newEmail == null && newEmail.trim().isEmpty()) {
            throw new EntityValidationException(Collections.singletonMap("email",
                    Collections.singletonList("Емайлу не може бути пустим!")));
        } else if (!newEmail.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new EntityValidationException(Collections.singletonMap("username",
                    Collections.singletonList("Неправильний формат емайлу!")));
        }
        if (newPassword == null && newPassword.trim().isEmpty()) {
            
        }
    }

    // 1. Оголосити поля (newEmail, newPassword, currentPassword - для
    // підтвердження)

    // 2. Конструктор

    // 3. Валідація (чи нові дані коректні)

    // 4. Геттери
}
