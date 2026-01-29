package ua.notion.musiclibrary.ui.controller;

import java.util.Arrays;

import ua.notion.musiclibrary.domain.impl.User;
import ua.notion.musiclibrary.dto.user.UserProfileDto;
import ua.notion.musiclibrary.dto.user.UserUpdateDto;
import ua.notion.musiclibrary.infrastructure.storage.impl.DataContext;
import ua.notion.musiclibrary.service.contract.UserService;
import ua.notion.musiclibrary.service.impl.UserServiceImpl;
import ua.notion.musiclibrary.ui.TuiSession;
import ua.notion.musiclibrary.ui.util.DisplayFormatter;
import ua.notion.musiclibrary.ui.util.InputValidator;
import ua.notion.musiclibrary.ui.util.MenuBuilder;

public class ProfileController {

    private final UserService userService;
    private final TuiSession session;
    private final MenuBuilder menuBuilder;

    public ProfileController(TuiSession session) {
        this.session = session;
        this.menuBuilder = new MenuBuilder(session.getTerminal());
        this.userService = new UserServiceImpl(DataContext.getInstance());
    }

    public void showProfile() {
        DisplayFormatter.clearScreen();
        System.out.println(DisplayFormatter.header("Мій Профіль"));

        int choice = menuBuilder.createListMenu(
                "Оберіть дію:",
                Arrays.asList(
                        "Переглянути профіль",
                        "Редагувати профіль",
                        "Переглянути підписки",
                        "Переглянути вподобані треки",
                        "Назад"));

        switch (choice) {
            case 0:
                viewProfileDetails();
                break;
            case 1:
                updateProfile();
                break;
            case 2:
                viewFollowing();
                break;
            case 3:
                viewLikedTracks();
                break;
            case 4:
                break;
            default:
                System.out.println(DisplayFormatter.error("Невірний вибір"));
        }
    }

    private void viewProfileDetails() {
        User currentUser = session.getCurrentUser();

        System.out.println(DisplayFormatter.header("Деталі Профілю"));
        System.out.println(DisplayFormatter.bold("Ім'я користувача: ") + currentUser.getUsername());
        System.out.println(DisplayFormatter.bold("Email: ") + currentUser.getEmail());
        System.out.println(DisplayFormatter.bold("Роль: ") + currentUser.getRole());

        System.out.println("\n" + DisplayFormatter.bold("Статистика:"));
        System.out.println("  Підписки на користувачів: " + currentUser.getFollowedUserIds().size());
        System.out.println("  Підписки на виконавців: " + currentUser.getFollowedArtistIds().size());
        System.out.println("  Підписки на плейлисти: " + currentUser.getFollowedPlaylistIds().size());
        System.out.println("  Вподобані треки: " + currentUser.getLikedTrackIds().size());
        System.out.println("  Збережені альбоми: " + currentUser.getSavedAlbumIds().size());

        String back = menuBuilder.createInputPrompt("\nНатисніть Enter для повернення...", "");
    }

    private void updateProfile() {
        System.out.println(DisplayFormatter.header("Редагування Профілю"));

        User currentUser = session.getCurrentUser();

        String newUsername = menuBuilder.createInputPrompt(
                "Нове ім'я користувача (Enter для збереження поточного):",
                currentUser.getUsername());

        String newEmail = menuBuilder.createInputPrompt(
                "Новий email (Enter для збереження поточного):",
                currentUser.getEmail());

        boolean changePassword = menuBuilder.createConfirmation("Змінити пароль?");
        String newPassword = null;

        if (changePassword) {
            newPassword = menuBuilder.createMaskedInput("Новий пароль:");
            var validation = InputValidator.validatePassword(newPassword);

            if (!validation.isValid()) {
                System.out.println(DisplayFormatter.error(validation.getErrorMessage()));
                return;
            }

            String confirmPassword = menuBuilder.createMaskedInput("Підтвердіть новий пароль:");
            if (!newPassword.equals(confirmPassword)) {
                System.out.println(DisplayFormatter.error("Паролі не співпадають"));
                return;
            }
        }

        try {
            UserUpdateDto updateDto = new UserUpdateDto(
                    newUsername.isBlank() ? currentUser.getUsername() : newUsername,
                    newEmail.isBlank() ? currentUser.getEmail() : newEmail,
                    newPassword,
                    currentUser.getPassword());

            boolean success = userService.updateProfile(currentUser.getID(), updateDto);

            if (success) {
                System.out.println(DisplayFormatter.success("Профіль успішно оновлено"));
                currentUser.setUsername(updateDto.newUsername());
                currentUser.setEmail(updateDto.newEmail());
                if (newPassword != null) {
                    currentUser.setPassword(newPassword);
                }
            } else {
                System.out.println(DisplayFormatter.error("Не вдалося оновити профіль"));
            }
        } catch (Exception e) {
            System.out.println(DisplayFormatter.error("Помилка оновлення: " + e.getMessage()));
        }
    }

    private void viewFollowing() {
        User currentUser = session.getCurrentUser();

        System.out.println(DisplayFormatter.header("Підписки"));
        System.out.println(DisplayFormatter.bold("Користувачі: ") +
                currentUser.getFollowedUserIds().size());
        System.out.println(DisplayFormatter.bold("Виконавці: ") +
                currentUser.getFollowedArtistIds().size());
        System.out.println(DisplayFormatter.bold("Плейлисти: ") +
                currentUser.getFollowedPlaylistIds().size());

        System.out.println(DisplayFormatter.info("\nДеталі підписок будуть доступні у наступній версії"));

        String back = menuBuilder.createInputPrompt("\nНатисніть Enter для повернення...", "");
    }

    private void viewLikedTracks() {
        User currentUser = session.getCurrentUser();

        System.out.println(DisplayFormatter.header("Вподобані Треки"));
        System.out.println(DisplayFormatter.bold("Усього: ") + currentUser.getLikedTrackIds().size());

        System.out.println(DisplayFormatter.info("\nПерегляд вподобаних треків буде доступний у наступній версії"));

        String back = menuBuilder.createInputPrompt("\nНатисніть Enter для повернення...", "");
    }
}
