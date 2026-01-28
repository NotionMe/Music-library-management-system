package ua.notion.musiclibrary.ui.util;

import java.util.regex.Pattern;

public class InputValidator {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
            "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$");

    public static ValidationResult validateEmail(String email) {
        if (email == null || email.isBlank()) {
            return new ValidationResult(false, "Email не може бути порожнім");
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            return new ValidationResult(false, "Невірний формат email");
        }

        return new ValidationResult(true, null);
    }

    public static ValidationResult validateUsername(String username) {
        if (username == null || username.isBlank()) {
            return new ValidationResult(false, "Ім'я користувача не може бути порожнім");
        }

        if (username.length() < 3 || username.length() > 15) {
            return new ValidationResult(false, "Ім'я користувача має бути від 3 до 15 символів");
        }

        return new ValidationResult(true, null);
    }

    public static ValidationResult validatePassword(String password) {
        if (password == null || password.isBlank()) {
            return new ValidationResult(false, "Пароль не може бути порожнім");
        }

        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            return new ValidationResult(false,
                    "Пароль має містити мінімум 8 символів, цифру, велику літеру та спецсимвол");
        }

        return new ValidationResult(true, null);
    }

    public static ValidationResult validateNotEmpty(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            return new ValidationResult(false, fieldName + " не може бути порожнім");
        }

        return new ValidationResult(true, null);
    }

    public static class ValidationResult {
        private final boolean valid;
        private final String errorMessage;

        public ValidationResult(boolean valid, String errorMessage) {
            this.valid = valid;
            this.errorMessage = errorMessage;
        }

        public boolean isValid() {
            return valid;
        }

        public String getErrorMessage() {
            return errorMessage;
        }
    }
}
