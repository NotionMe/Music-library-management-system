package ua.notion.musiclibrary.ui.controller;

import java.util.Arrays;
import java.util.UUID;

import ua.notion.musiclibrary.domain.impl.User;
import ua.notion.musiclibrary.dto.auth.UserLoginDto;
import ua.notion.musiclibrary.dto.auth.UserRegistrationDto;
import ua.notion.musiclibrary.service.contract.AuthService;
import ua.notion.musiclibrary.ui.TuiSession;
import ua.notion.musiclibrary.ui.util.DisplayFormatter;
import ua.notion.musiclibrary.ui.util.InputValidator;
import ua.notion.musiclibrary.ui.util.InputValidator.ValidationResult;
import ua.notion.musiclibrary.ui.util.MenuBuilder;

public class AuthenticationController {

    private final AuthService authService;
    private final TuiSession session;
    private final MenuBuilder menuBuilder;

    public AuthenticationController(AuthService authService, TuiSession session) {
        this.authService = authService;
        this.session = session;
        this.menuBuilder = new MenuBuilder(session.getTerminal());
    }

    public boolean showAuthMenu() {
        System.out.println(DisplayFormatter.header("Система Управління Музичною Бібліотекою"));

        int choice = menuBuilder.createListMenu(
                "Оберіть дію:",
                Arrays.asList("Реєстрація", "Вхід", "Вихід"));

        switch (choice) {
            case 0:
                return handleRegistration();
            case 1:
                return handleLogin();
            case 2:
                return false;
            default:
                System.out.println(DisplayFormatter.error("Невірний вибір"));
                return true;
        }
    }

    private boolean handleRegistration() {
        System.out.println(DisplayFormatter.header("Реєстрація"));

        String username = menuBuilder.createInputPrompt("Ім'я користувача (3-15 символів):", "");
        ValidationResult usernameValidation = InputValidator.validateUsername(username);
        if (!usernameValidation.isValid()) {
            System.out.println(DisplayFormatter.error(usernameValidation.getErrorMessage()));
            return true;
        }

        String email = menuBuilder.createInputPrompt("Email:", "");
        ValidationResult emailValidation = InputValidator.validateEmail(email);
        if (!emailValidation.isValid()) {
            System.out.println(DisplayFormatter.error(emailValidation.getErrorMessage()));
            return true;
        }

        String password = menuBuilder.createMaskedInput("Пароль (мін 8 символів, велика літера, цифра, спецсимвол):");
        ValidationResult passwordValidation = InputValidator.validatePassword(password);
        if (!passwordValidation.isValid()) {
            System.out.println(DisplayFormatter.error(passwordValidation.getErrorMessage()));
            return true;
        }

        String confirmPassword = menuBuilder.createMaskedInput("Підтвердіть пароль:");
        if (!password.equals(confirmPassword)) {
            System.out.println(DisplayFormatter.error("Паролі не співпадають"));
            return true;
        }

        try {
            UserRegistrationDto registrationDto = new UserRegistrationDto(username, email, password,
                    ua.notion.musiclibrary.domain.enums.Role.USER);
            UUID tempUserId = authService.initiateRegistration(registrationDto);

            System.out.println(DisplayFormatter.success(
                    "Код верифікації надіслано на " + email + ". Перевірте вашу пошту."));

            String code = menuBuilder.createInputPrompt("Введіть код верифікації:", "");

            User user = authService.completeRegistration(tempUserId, code);
            session.setCurrentUser(user);

            System.out.println(DisplayFormatter.success(
                    "Реєстрація успішна! Ласкаво просимо, " + user.getUsername() + "!"));

            return true;
        } catch (Exception e) {
            System.out.println(DisplayFormatter.error("Помилка реєстрації: " + e.getMessage()));
            return true;
        }
    }

    private boolean handleLogin() {
        System.out.println(DisplayFormatter.header("Вхід"));

        final int MAX_ATTEMPTS = 3;
        int attempts = 0;

        while (attempts < MAX_ATTEMPTS) {
            String emailOrUsername = menuBuilder.createInputPrompt("Email або ім'я користувача:", "");
            String password = menuBuilder.createMaskedInput("Пароль:");

            try {
                UserLoginDto loginDto = new UserLoginDto(emailOrUsername, password);
                User user = authService.login(loginDto);

                session.setCurrentUser(user);
                System.out.println(DisplayFormatter.success(
                        "Вхід успішний! Ласкаво просимо, " + user.getUsername() + "!"));
                DisplayFormatter.waitForKeyPress();
                return true;

            } catch (Exception e) {
                attempts++;
                System.out.println(DisplayFormatter.error("Невірний email/ім'я користувача або пароль"));
                System.out.println(DisplayFormatter.warning(
                        String.format("Залишилось спроб: %d з %d", MAX_ATTEMPTS - attempts, MAX_ATTEMPTS)));

                if (attempts >= MAX_ATTEMPTS) {
                    System.out.println(DisplayFormatter.error(
                            "Перевищено максимальну кількість спроб входу. Спробуйте пізніше."));
                    DisplayFormatter.waitForKeyPress();
                    return true;
                }

                boolean retry = menuBuilder.createConfirmation("Спробувати ще раз?");
                if (!retry) {
                    return true;
                }
            }
        }

        return true;
    }
}
