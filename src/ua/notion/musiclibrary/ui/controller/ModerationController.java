package ua.notion.musiclibrary.ui.controller;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import ua.notion.musiclibrary.domain.enums.Role;
import ua.notion.musiclibrary.dto.track.TrackDto;
import ua.notion.musiclibrary.infrastructure.storage.impl.DataContext;
import ua.notion.musiclibrary.service.contract.ModerationService;
import ua.notion.musiclibrary.service.impl.ModerationServiceImpl;
import ua.notion.musiclibrary.ui.TuiSession;
import ua.notion.musiclibrary.ui.util.DisplayFormatter;
import ua.notion.musiclibrary.ui.util.MenuBuilder;

public class ModerationController {

    private final TuiSession session;
    private final MenuBuilder menuBuilder;
    private final ModerationService moderationService;

    public ModerationController(TuiSession session) {
        this.session = session;
        this.menuBuilder = new MenuBuilder(session.getTerminal());
        this.moderationService = new ModerationServiceImpl(DataContext.getInstance());
    }

    public void showModerationPanel() {
        if (session.getCurrentUser().getRole() != Role.ADMIN) {
            System.out.println(DisplayFormatter.error("Доступ заборонено. Тільки адміністратори."));
            DisplayFormatter.waitForKeyPress();
            return;
        }

        DisplayFormatter.clearScreen();
        System.out.println(DisplayFormatter.header("Панель Модерації"));

        List<TrackDto> pendingTracks = moderationService.getPendingTracks();

        if (pendingTracks.isEmpty()) {
            System.out.println(DisplayFormatter.info("Немає треків на розгляді"));
            DisplayFormatter.waitForKeyPress();
            return;
        }

        System.out.println(String.format("\nТреків на розгляді: %d\n", pendingTracks.size()));

        for (int i = 0; i < pendingTracks.size(); i++) {
            TrackDto track = pendingTracks.get(i);
            System.out.println(String.format("%d. %s - %s (%s)",
                    (i + 1),
                    track.title(),
                    track.artistName(),
                    track.albumName()));
        }

        String choice = menuBuilder.createInputPrompt("\nВведіть номер треку для розгляду (або Enter для повернення):",
                "");

        if (!choice.isBlank()) {
            try {
                int index = Integer.parseInt(choice) - 1;
                if (index >= 0 && index < pendingTracks.size()) {
                    reviewTrack(pendingTracks.get(index));
                }
            } catch (NumberFormatException e) {
            }
        }
    }

    private void reviewTrack(TrackDto track) {
        DisplayFormatter.clearScreen();
        System.out.println(DisplayFormatter.header("Розгляд Треку"));

        System.out.println(String.format("Назва: %s", track.title()));
        System.out.println(String.format("Артист: %s", track.artistName()));
        System.out.println(String.format("Альбом: %s", track.albumName()));
        System.out.println(String.format("Тривалість: %s", track.duration()));
        if (track.fileSize() != null) {
            System.out.println(String.format("Розмір файлу: %s", track.fileSize()));
        }

        int choice = menuBuilder.createListMenu(
                "\nВиберіть дію:",
                Arrays.asList(
                        "✅ Схвалити трек",
                        "❌ Відхилити трек",
                        "⬅ Назад"));

        switch (choice) {
            case 0:
                approveTrack(track.id());
                break;
            case 1:
                rejectTrack(track.id());
                break;
            case 2:
                break;
        }
    }

    private void approveTrack(UUID trackId) {
        try {
            moderationService.approveTrack(trackId, session.getCurrentUser().getID());
            System.out.println(DisplayFormatter.success("Трек схвалено та опубліковано!"));
            DisplayFormatter.waitForKeyPress();
        } catch (Exception e) {
            System.out.println(DisplayFormatter.error("Помилка: " + e.getMessage()));
            DisplayFormatter.waitForKeyPress();
        }
    }

    private void rejectTrack(UUID trackId) {
        String reason = menuBuilder.createInputPrompt(
                "Вкажіть причину відхилення:",
                "Не відповідає вимогам якості");

        try {
            moderationService.rejectTrack(trackId, session.getCurrentUser().getID(), reason);
            System.out.println(DisplayFormatter.success("Трек відхилено"));
            DisplayFormatter.waitForKeyPress();
        } catch (Exception e) {
            System.out.println(DisplayFormatter.error("Помилка: " + e.getMessage()));
            DisplayFormatter.waitForKeyPress();
        }
    }
}
