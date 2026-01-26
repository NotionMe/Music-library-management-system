package ua.notion.musiclibrary.ui.util;

import java.time.Duration;
import java.util.List;

import ua.notion.musiclibrary.domain.impl.Playlist;
import ua.notion.musiclibrary.domain.impl.User;
import ua.notion.musiclibrary.dto.track.TrackDto;

public class DisplayFormatter {

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_BOLD = "\u001B[1m";

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void waitForKeyPress() {
        System.out.print("\nНатисніть Enter для продовження...");
        try {
            System.in.read();
            while (System.in.available() > 0) {
                System.in.read();
            }
        } catch (Exception e) {
        }
    }

    public static String formatDuration(Duration duration) {
        if (duration == null) {
            return "00:00";
        }

        long totalSeconds = duration.getSeconds();
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;

        return String.format("%02d:%02d", minutes, seconds);
    }

    public static String formatTrackInfo(TrackDto track) {
        return String.format("%s - %s - %s",
                track.title(),
                track.artistName(),
                track.duration());
    }

    public static String formatPlaylistInfo(Playlist playlist, int trackCount) {
        String privacy = playlist.isPrivate() ? "Приватний" : "Публічний";
        return String.format("%s (%s) - Треків: %d",
                playlist.getName(),
                privacy,
                trackCount);
    }

    public static String formatUserProfile(User user) {
        return String.format("Користувач: %s\nEmail: %s\nРоль: %s",
                user.getUsername(),
                user.getEmail(),
                user.getRole());
    }

    public static void printTrackTable(List<TrackDto> tracks) {
        if (tracks.isEmpty()) {
            System.out.println(warning("Треки не знайдено"));
            return;
        }

        System.out.println(bold("\n" + "=".repeat(80)));
        System.out
                .println(bold(String.format("%-4s | %-30s | %-20s | %-10s", "#", "Назва", "Виконавець", "Тривалість")));
        System.out.println(bold("=".repeat(80)));

        for (int i = 0; i < tracks.size(); i++) {
            TrackDto track = tracks.get(i);
            System.out.println(String.format("%-4d | %-30s | %-20s | %-10s",
                    (i + 1),
                    truncate(track.title(), 30),
                    truncate(track.artistName(), 20),
                    track.duration()));
        }

        System.out.println("=".repeat(80) + "\n");
    }

    public static String success(String message) {
        return ANSI_GREEN + "✓ " + message + ANSI_RESET;
    }

    public static String error(String message) {
        return ANSI_RED + "✗ " + message + ANSI_RESET;
    }

    public static String warning(String message) {
        return ANSI_YELLOW + "⚠ " + message + ANSI_RESET;
    }

    public static String info(String message) {
        return ANSI_CYAN + "ℹ " + message + ANSI_RESET;
    }

    public static String bold(String text) {
        return ANSI_BOLD + text + ANSI_RESET;
    }

    public static String header(String title) {
        String border = "═".repeat(title.length() + 4);
        return String.format("\n%s\n║ %s ║\n%s\n",
                border,
                bold(title),
                border);
    }

    private static String truncate(String text, int maxLength) {
        if (text == null) {
            return "";
        }

        if (text.length() <= maxLength) {
            return text;
        }

        return text.substring(0, maxLength - 3) + "...";
    }
}
