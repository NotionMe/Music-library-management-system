package ua.notion.musiclibrary.ui.controller;

import java.util.Arrays;

import ua.notion.musiclibrary.infrastructure.storage.impl.DataContext;
import ua.notion.musiclibrary.service.contract.AudioPlayerService;
import ua.notion.musiclibrary.service.impl.AudioPlayerServiceImpl;
import ua.notion.musiclibrary.ui.TuiSession;
import ua.notion.musiclibrary.ui.util.DisplayFormatter;
import ua.notion.musiclibrary.ui.util.MenuBuilder;

public class PlayerController {

    private final AudioPlayerService playerService;
    private final TuiSession session;
    private final MenuBuilder menuBuilder;

    public PlayerController(TuiSession session) {
        this.session = session;
        this.menuBuilder = new MenuBuilder(session.getTerminal());
        this.playerService = AudioPlayerServiceImpl.getInstance();
    }

    public void showNowPlaying() {
        DisplayFormatter.clearScreen();
        System.out.println(DisplayFormatter.header("Аудіоплеєр"));

        if (!playerService.isPlaying()) {
            System.out.println(DisplayFormatter.info("Зараз нічого не відтворюється"));
            System.out.println(DisplayFormatter.info("Виберіть трек у меню пошуку або плейлисті для відтворення"));

            String back = menuBuilder.createInputPrompt("\nНатисніть Enter для повернення...", "");
            return;
        }

        while (true) {
            boolean shouldExit = displayPlayerControls();
            if (shouldExit) {
                break;
            }
        }
    }

    private boolean displayPlayerControls() {
        DisplayFormatter.clearScreen();
        System.out.println(DisplayFormatter.header("Аудіоплеєр"));

        long currentTime = playerService.getCurrentTime();
        long duration = playerService.getDuration();
        int volume = playerService.getVolume();

        System.out.println(DisplayFormatter.bold("Статус: ") +
                (playerService.isPlaying() ? "Відтворення ▶" : "Пауза ⏸"));

        System.out.println(DisplayFormatter.bold("Час: ") +
                formatTime(currentTime) + " / " + formatTime(duration));

        System.out.println(DisplayFormatter.bold("Гучність: ") + volume + "%");

        int choice = menuBuilder.createListMenu(
                "Оберіть дію:",
                Arrays.asList(
                        playerService.isPlaying() ? "Пауза" : "Відновити",
                        "Зупинити",
                        "Регулювати гучність",
                        "Перемотати",
                        "Назад"));

        switch (choice) {
            case 0:
                handlePauseResume();
                return false;
            case 1:
                handleStop();
                return true;
            case 2:
                handleVolumeControl();
                return false;
            case 3:
                handleSeek();
                return false;
            case 4:
                return true;
            default:
                System.out.println(DisplayFormatter.error("Невірний вибір"));
                return false;
        }
    }

    private void handlePauseResume() {
        if (playerService.isPlaying()) {
            playerService.pause();
            System.out.println(DisplayFormatter.success("Відтворення призупинено"));
        } else {
            playerService.resume();
            System.out.println(DisplayFormatter.success("Відтворення відновлено"));
        }
    }

    private void handleStop() {
        playerService.stop();
        System.out.println(DisplayFormatter.success("Відтворення зупинено"));
    }

    private void handleVolumeControl() {
        String volumeStr = menuBuilder.createInputPrompt(
                "Введіть рівень гучності (0-100):",
                String.valueOf(playerService.getVolume()));

        try {
            int volume = Integer.parseInt(volumeStr);
            if (volume >= 0 && volume <= 100) {
                playerService.setVolume(volume);
                System.out.println(DisplayFormatter.success("Гучність встановлено на " + volume + "%"));
            } else {
                System.out.println(DisplayFormatter.error("Гучність має бути від 0 до 100"));
            }
        } catch (NumberFormatException e) {
            System.out.println(DisplayFormatter.error("Невірний формат числа"));
        }
    }

    private void handleSeek() {
        long duration = playerService.getDuration();
        String timeStr = menuBuilder.createInputPrompt(
                "Введіть час у секундах (0-" + (duration / 1000) + "):",
                "0");

        try {
            long seconds = Long.parseLong(timeStr);
            long millis = seconds * 1000;

            if (millis >= 0 && millis <= duration) {
                playerService.seek(millis);
                System.out.println(DisplayFormatter.success("Перемотано до " + formatTime(millis)));
            } else {
                System.out.println(DisplayFormatter.error("Час має бути від 0 до " + (duration / 1000) + " секунд"));
            }
        } catch (NumberFormatException e) {
            System.out.println(DisplayFormatter.error("Невірний формат числа"));
        }
    }

    private String formatTime(long millis) {
        long totalSeconds = millis / 1000;
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
}
