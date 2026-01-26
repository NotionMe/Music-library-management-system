package ua.notion.musiclibrary.ui.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import ua.notion.musiclibrary.domain.impl.Playlist;
import ua.notion.musiclibrary.dto.playlist.PlaylistCreateDto;
import ua.notion.musiclibrary.dto.playlist.PlaylistUpdateDto;
import ua.notion.musiclibrary.dto.track.TrackDto;
import ua.notion.musiclibrary.dto.track.TrackSearchDto;
import ua.notion.musiclibrary.infrastructure.storage.impl.DataContext;
import ua.notion.musiclibrary.service.contract.PlaylistService;
import ua.notion.musiclibrary.service.contract.TrackService;
import ua.notion.musiclibrary.service.impl.PlaylistServiceImpl;
import ua.notion.musiclibrary.service.impl.TrackServiceImpl;
import ua.notion.musiclibrary.ui.TuiSession;
import ua.notion.musiclibrary.ui.util.DisplayFormatter;
import ua.notion.musiclibrary.ui.util.InputValidator;
import ua.notion.musiclibrary.ui.util.MenuBuilder;

public class PlaylistController {

    private final PlaylistService playlistService;
    private final TrackService trackService;
    private final TuiSession session;
    private final MenuBuilder menuBuilder;

    public PlaylistController(TuiSession session) {
        this.session = session;
        this.menuBuilder = new MenuBuilder(session.getTerminal());
        DataContext context = DataContext.getInstance();
        this.playlistService = new PlaylistServiceImpl(context);
        this.trackService = new TrackServiceImpl(context);
    }

    public void showPlaylistMenu() {
        DisplayFormatter.clearScreen();
        System.out.println(DisplayFormatter.header("Мої Плейлисти"));

        List<Playlist> playlists = playlistService.getMyPlaylists(session.getCurrentUser().getID());

        if (playlists.isEmpty()) {
            System.out.println(DisplayFormatter.info("У вас ще немає плейлистів"));
        } else {
            System.out.println(DisplayFormatter.bold("\nВаші плейлисти:\n"));
            for (int i = 0; i < playlists.size(); i++) {
                Playlist playlist = playlists.get(i);
                System.out.println(String.format("%d. %s",
                        (i + 1),
                        DisplayFormatter.formatPlaylistInfo(playlist, playlist.getTrackIds().size())));
            }
            System.out.println();
        }

        int choice = menuBuilder.createListMenu(
                "Оберіть дію:",
                Arrays.asList(
                        "Створити новий плейлист",
                        "Переглянути плейлист",
                        "Редагувати плейлист",
                        "Видалити плейлист",
                        "Назад"));

        switch (choice) {
            case 0:
                createPlaylist();
                break;
            case 1:
                viewPlaylist(playlists);
                break;
            case 2:
                editPlaylist(playlists);
                break;
            case 3:
                deletePlaylist(playlists);
                break;
            case 4:
                break;
            default:
                System.out.println(DisplayFormatter.error("Невірний вибір"));
        }
    }

    private void createPlaylist() {
        System.out.println(DisplayFormatter.header("Створення Плейлиста"));

        String name = menuBuilder.createInputPrompt("Назва плейлиста (1-100 символів):", "");
        var validation = InputValidator.validateNotEmpty(name, "Назва плейлиста");

        if (!validation.isValid()) {
            System.out.println(DisplayFormatter.error(validation.getErrorMessage()));
            return;
        }

        boolean isPrivate = menuBuilder.createConfirmation("Зробити плейлист приватним?");

        try {
            PlaylistCreateDto createDto = new PlaylistCreateDto(
                    name,
                    isPrivate,
                    session.getCurrentUser().getID());

            Playlist playlist = playlistService.createPlayList(createDto);
            System.out.println(DisplayFormatter.success(
                    "Плейлист '" + playlist.getName() + "' успішно створено!"));

            boolean addTracks = menuBuilder.createConfirmation("Додати треки зараз?");
            if (addTracks) {
                addTracksToPlaylist(playlist.getID());
            }
        } catch (Exception e) {
            System.out.println(DisplayFormatter.error("Помилка створення плейлиста: " + e.getMessage()));
        }
    }

    private void viewPlaylist(List<Playlist> playlists) {
        if (playlists.isEmpty()) {
            System.out.println(DisplayFormatter.warning("Немає плейлистів для перегляду"));
            DisplayFormatter.waitForKeyPress();
            return;
        }

        String indexStr = menuBuilder.createInputPrompt("Введіть номер плейлиста:", "1");
        try {
            int index = Integer.parseInt(indexStr) - 1;
            if (index >= 0 && index < playlists.size()) {
                Playlist playlist = playlists.get(index);
                displayPlaylistDetails(playlist);
            } else {
                System.out.println(DisplayFormatter.error("Невірний номер плейлиста"));
            }
        } catch (NumberFormatException e) {
            System.out.println(DisplayFormatter.error("Невірний формат номера"));
        }
    }

    private void displayPlaylistDetails(Playlist playlist) {
        System.out.println(DisplayFormatter.header("Плейлист: " + playlist.getName()));
        System.out.println(DisplayFormatter.bold("Приватність: ") +
                (playlist.isPrivate() ? "Приватний" : "Публічний"));
        System.out.println(DisplayFormatter.bold("Кількість треків: ") +
                playlist.getTrackIds().size());

        if (!playlist.getTrackIds().isEmpty()) {
            System.out.println("\n" + DisplayFormatter.bold("Треки:"));
            List<TrackDto> tracks = new ArrayList<>();

            for (UUID trackId : playlist.getTrackIds()) {
                try {
                    TrackDto track = trackService.getTrackDetails(trackId);
                    tracks.add(track);
                } catch (Exception e) {
                }
            }

            DisplayFormatter.printTrackTable(tracks);
        }

        int choice = menuBuilder.createListMenu(
                "Оберіть дію:",
                Arrays.asList("Додати треки", "Назад"));

        if (choice == 0) {
            addTracksToPlaylist(playlist.getID());
        }
    }

    private void addTracksToPlaylist(UUID playlistId) {
        System.out.println(DisplayFormatter.header("Додати Треки до Плейлиста"));

        TrackSearchDto searchDto = new TrackSearchDto(null, null, null);
        List<TrackDto> allTracks = trackService.searchTracks(searchDto);

        if (allTracks.isEmpty()) {
            System.out.println(DisplayFormatter.warning("Немає доступних треків"));
            DisplayFormatter.waitForKeyPress();
            return;
        }

        DisplayFormatter.printTrackTable(allTracks);

        String indexStr = menuBuilder.createInputPrompt("Введіть номер треку для додавання (0 для скасування):", "0");
        try {
            int index = Integer.parseInt(indexStr) - 1;
            if (index >= 0 && index < allTracks.size()) {
                TrackDto track = allTracks.get(index);
                boolean success = playlistService.addTrackToPlaylist(playlistId, track.id());

                if (success) {
                    System.out.println(DisplayFormatter.success("Трек '" + track.title() + "' додано до плейлиста"));
                } else {
                    System.out.println(DisplayFormatter.error("Не вдалося додати трек"));
                }
            }
        } catch (NumberFormatException e) {
            System.out.println(DisplayFormatter.error("Невірний формат номера"));
        }
    }

    private void editPlaylist(List<Playlist> playlists) {
        if (playlists.isEmpty()) {
            System.out.println(DisplayFormatter.warning("Немає плейлистів для редагування"));
            return;
        }

        String indexStr = menuBuilder.createInputPrompt("Введіть номер плейлиста:", "1");
        try {
            int index = Integer.parseInt(indexStr) - 1;
            if (index >= 0 && index < playlists.size()) {
                Playlist playlist = playlists.get(index);

                String newName = menuBuilder.createInputPrompt("Нова назва (Enter для збереження поточної):",
                        playlist.getName());
                boolean isPrivate = menuBuilder.createConfirmation("Зробити приватним?");

                PlaylistUpdateDto updateDto = new PlaylistUpdateDto(
                        playlist.getID(),
                        newName,
                        isPrivate);

                playlistService.updatePlaylist(updateDto);
                System.out.println(DisplayFormatter.success("Плейлист оновлено"));
            } else {
                System.out.println(DisplayFormatter.error("Невірний номер плейлиста"));
            }
        } catch (Exception e) {
            System.out.println(DisplayFormatter.error("Помилка оновлення: " + e.getMessage()));
        }
    }

    private void deletePlaylist(List<Playlist> playlists) {
        if (playlists.isEmpty()) {
            System.out.println(DisplayFormatter.warning("Немає плейлистів для видалення"));
            return;
        }

        String indexStr = menuBuilder.createInputPrompt("Введіть номер плейлиста:", "1");
        try {
            int index = Integer.parseInt(indexStr) - 1;
            if (index >= 0 && index < playlists.size()) {
                Playlist playlist = playlists.get(index);

                boolean confirm = menuBuilder.createConfirmation(
                        "Ви впевнені, що хочете видалити '" + playlist.getName() + "'? Це незворотня дія.");

                if (confirm) {
                    boolean success = playlistService.deletePlaylist(playlist.getID());
                    if (success) {
                        System.out.println(DisplayFormatter.success("Плейлист видалено"));
                    } else {
                        System.out.println(DisplayFormatter.error("Не вдалося видалити плейлист"));
                    }
                }
            } else {
                System.out.println(DisplayFormatter.error("Невірний номер плейлиста"));
            }
        } catch (Exception e) {
            System.out.println(DisplayFormatter.error("Помилка видалення: " + e.getMessage()));
        }
    }
}
