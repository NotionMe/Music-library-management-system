package ua.notion.musiclibrary.ui.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ua.notion.musiclibrary.domain.enums.Role;
import ua.notion.musiclibrary.ui.TuiSession;
import ua.notion.musiclibrary.ui.util.DisplayFormatter;
import ua.notion.musiclibrary.ui.util.MenuBuilder;

public class MainMenuController {

    private final TuiSession session;
    private final MenuBuilder menuBuilder;
    private final TrackController trackController;
    private final PlaylistController playlistController;
    private final ProfileController profileController;
    private final BrowseController browseController;
    private final PlayerController playerController;
    private final MyTracksController myTracksController;
    private final ModerationController moderationController;
    private final TrackUploadController trackUploadController;

    public MainMenuController(TuiSession session) {
        this.session = session;
        this.menuBuilder = new MenuBuilder(session.getTerminal());
        this.trackController = new TrackController(session);
        this.playlistController = new PlaylistController(session);
        this.profileController = new ProfileController(session);
        this.browseController = new BrowseController(session);
        this.playerController = new PlayerController(session);
        this.myTracksController = new MyTracksController(session);
        this.moderationController = new ModerationController(session);
        this.trackUploadController = new TrackUploadController(session);
    }

    public boolean showMainMenu() {
        DisplayFormatter.clearScreen();
        System.out.println(DisplayFormatter.header("Головне Меню"));
        System.out.println(DisplayFormatter.info(
                "Вітаємо, " + session.getCurrentUser().getUsername() + "!"));

        List<String> menuOptions = new ArrayList<>(Arrays.asList(
                "Пошук треків",
                "Мої плейлисти",
                "Огляд музики",
                "Аудіоплеєр",
                "Завантажити трек",
                "Мої треки",
                "Мій профіль"));

        if (session.getCurrentUser().getRole() == Role.ADMIN) {
            menuOptions.add("Модерація");
        }

        menuOptions.add("Вихід з акаунта");
        menuOptions.add("Вихід з програми");

        int choice = menuBuilder.createListMenu("Оберіть дію:", menuOptions);

        switch (choice) {
            case 0:
                trackController.showSearchMenu();
                return true;
            case 1:
                playlistController.showPlaylistMenu();
                return true;
            case 2:
                browseController.showBrowseMenu();
                return true;
            case 3:
                playerController.showNowPlaying();
                return true;
            case 4:
                trackUploadController.showUploadMenu();
                return true;
            case 5:
                myTracksController.showMyTracks();
                return true;
            case 6:
                profileController.showProfile();
                return true;
            case 7:
                if (session.getCurrentUser().getRole() == Role.ADMIN) {
                    moderationController.showModerationPanel();
                    return true;
                } else {
                    handleLogout();
                    return true;
                }
            case 8:
                if (session.getCurrentUser().getRole() == Role.ADMIN) {
                    handleLogout();
                    return true;
                } else {
                    return false;
                }
            case 9:
                return false;
            default:
                System.out.println(DisplayFormatter.error("Невірний вибір"));
                return true;
        }
    }

    private void handleLogout() {
        boolean confirm = menuBuilder.createConfirmation("Ви впевнені, що хочете вийти?");

        if (confirm) {
            session.logout();
            System.out.println(DisplayFormatter.success("Ви успішно вийшли з акаунта"));
        }
    }
}
