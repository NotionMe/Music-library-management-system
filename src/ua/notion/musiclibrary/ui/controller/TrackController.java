package ua.notion.musiclibrary.ui.controller;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import ua.notion.musiclibrary.dto.track.TrackDto;
import ua.notion.musiclibrary.dto.track.TrackSearchDto;
import ua.notion.musiclibrary.infrastructure.storage.impl.DataContext;
import ua.notion.musiclibrary.service.contract.TrackService;
import ua.notion.musiclibrary.service.impl.TrackServiceImpl;
import ua.notion.musiclibrary.ui.TuiSession;
import ua.notion.musiclibrary.ui.util.DisplayFormatter;
import ua.notion.musiclibrary.ui.util.MenuBuilder;

public class TrackController {

    private final TrackService trackService;
    private final TuiSession session;
    private final MenuBuilder menuBuilder;

    public TrackController(TuiSession session) {
        this.session = session;
        this.menuBuilder = new MenuBuilder(session.getTerminal());
        this.trackService = new TrackServiceImpl(DataContext.getInstance());
    }

    public void showSearchMenu() {
        DisplayFormatter.clearScreen();
        System.out.println(DisplayFormatter.header("Пошук Треків"));

        int choice = menuBuilder.createListMenu(
                "Оберіть тип пошуку:",
                Arrays.asList(
                        "Пошук за назвою",
                        "Пошук за виконавцем",
                        "Пошук за альбомом",
                        "Перегляд всіх треків",
                        "Назад"));

        switch (choice) {
            case 0:
                searchByTitle();
                break;
            case 1:
                searchByArtist();
                break;
            case 2:
                searchByAlbum();
                break;
            case 3:
                viewAllTracks();
                break;
            case 4:
                break;
            default:
                System.out.println(DisplayFormatter.error("Невірний вибір"));
        }
    }

    private void searchByTitle() {
        String title = menuBuilder.createInputPrompt("Введіть назву треку:", "");

        if (title == null || title.isBlank()) {
            System.out.println(DisplayFormatter.warning("Пошук скасовано"));
            return;
        }

        TrackSearchDto searchDto = new TrackSearchDto(title, null, null);

        List<TrackDto> results = trackService.searchTracks(searchDto);
        displaySearchResults(results);
    }

    private void searchByArtist() {
        String artist = menuBuilder.createInputPrompt("Введіть ім'я виконавця:", "");

        if (artist == null || artist.isBlank()) {
            System.out.println(DisplayFormatter.warning("Пошук скасовано"));
            return;
        }

        TrackSearchDto searchDto = new TrackSearchDto(null, null, artist);

        List<TrackDto> results = trackService.searchTracks(searchDto);
        displaySearchResults(results);
    }

    private void searchByAlbum() {
        String album = menuBuilder.createInputPrompt("Введіть назву альбому:", "");

        if (album == null || album.isBlank()) {
            System.out.println(DisplayFormatter.warning("Пошук скасовано"));
            return;
        }

        TrackSearchDto searchDto = new TrackSearchDto(album, null, null);

        List<TrackDto> results = trackService.searchTracks(searchDto);
        displaySearchResults(results);
    }

    private void viewAllTracks() {
        TrackSearchDto searchDto = new TrackSearchDto(null, null, null);
        List<TrackDto> results = trackService.searchTracks(searchDto);
        displaySearchResults(results);
    }

    private void displaySearchResults(List<TrackDto> tracks) {
        if (tracks.isEmpty()) {
            System.out.println(DisplayFormatter.warning("Треки не знайдено"));
            DisplayFormatter.waitForKeyPress();
            return;
        }

        DisplayFormatter.printTrackTable(tracks);

        int choice = menuBuilder.createListMenu(
                "Оберіть дію:",
                Arrays.asList("Переглянути деталі треку", "Назад"));

        if (choice == 0) {
            String indexStr = menuBuilder.createInputPrompt("Введіть номер треку:", "1");
            try {
                int index = Integer.parseInt(indexStr) - 1;
                if (index >= 0 && index < tracks.size()) {
                    displayTrackDetails(tracks.get(index));
                } else {
                    System.out.println(DisplayFormatter.error("Невірний номер треку"));
                }
            } catch (NumberFormatException e) {
                System.out.println(DisplayFormatter.error("Невірний формат номера"));
            }
        }
    }

    private void displayTrackDetails(TrackDto track) {
        System.out.println(DisplayFormatter.header("Деталі Треку"));
        System.out.println(DisplayFormatter.bold("Назва: ") + track.title());
        System.out.println(DisplayFormatter.bold("Виконавець: ") + track.artistName());
        System.out.println(DisplayFormatter.bold("Альбом: ") + track.albumName());
        System.out.println(DisplayFormatter.bold("Тривалість: ") + track.duration());
        System.out.println(DisplayFormatter.bold("Формат: ") + track.audioFormat());

        if (track.bitrate() != null) {
            System.out.println(DisplayFormatter.bold("Бітрейт: ") + track.bitrate() + " kbps");
        }

        int choice = menuBuilder.createListMenu(
                "\nОберіть дію:",
                Arrays.asList("▶ Відтворити", "Назад"));

        if (choice == 0) {
            playTrack(track);
        }
    }

    private void playTrack(TrackDto track) {
        if (track.filePath() == null || track.filePath().isBlank()) {
            System.out.println(DisplayFormatter.error("Файл треку не знайдено!"));
            System.out.println("filePath: " + track.filePath());
            DisplayFormatter.waitForKeyPress();
            return;
        }

        try {
            System.out.println("DEBUG: Спроба відтворити файл: " + track.filePath());

            ua.notion.musiclibrary.service.contract.AudioPlayerService playerService = ua.notion.musiclibrary.service.impl.AudioPlayerServiceImpl
                    .getInstance();

            playerService.play(track.filePath());

            System.out.println("DEBUG: playerService.isPlaying() = " + playerService.isPlaying());

            System.out.println(DisplayFormatter.success("▶ Відтворення розпочато: " + track.title()));
            System.out.println(DisplayFormatter.info("Перейдіть в 'Аудіоплеєр' для керування відтворенням"));
            DisplayFormatter.waitForKeyPress();
        } catch (Exception e) {
            System.out.println(DisplayFormatter.error("Помилка відтворення: " + e.getMessage()));
            System.out.println("DEBUG: Exception type: " + e.getClass().getName());
            e.printStackTrace();
            DisplayFormatter.waitForKeyPress();
        }
    }
}
