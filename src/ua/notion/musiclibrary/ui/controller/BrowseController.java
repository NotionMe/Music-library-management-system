package ua.notion.musiclibrary.ui.controller;

import java.util.Arrays;
import java.util.List;

import ua.notion.musiclibrary.domain.impl.Album;
import ua.notion.musiclibrary.domain.impl.Artist;
import ua.notion.musiclibrary.domain.impl.Genre;
import ua.notion.musiclibrary.infrastructure.storage.impl.DataContext;
import ua.notion.musiclibrary.ui.TuiSession;
import ua.notion.musiclibrary.ui.util.DisplayFormatter;
import ua.notion.musiclibrary.ui.util.MenuBuilder;

public class BrowseController {

    private final DataContext dataContext;
    private final TuiSession session;
    private final MenuBuilder menuBuilder;

    public BrowseController(TuiSession session) {
        this.session = session;
        this.menuBuilder = new MenuBuilder(session.getTerminal());
        this.dataContext = DataContext.getInstance();
    }

    public void showBrowseMenu() {
        DisplayFormatter.clearScreen();
        System.out.println(DisplayFormatter.header("Огляд Музики"));

        int choice = menuBuilder.createListMenu(
                "Оберіть категорію:",
                Arrays.asList(
                        "Переглянути виконавців",
                        "Переглянути альбоми",
                        "Переглянути жанри",
                        "Назад"));

        switch (choice) {
            case 0:
                browseArtists();
                break;
            case 1:
                browseAlbums();
                break;
            case 2:
                browseGenres();
                break;
            case 3:
                break;
            default:
                System.out.println(DisplayFormatter.error("Невірний вибір"));
        }
    }

    private void browseArtists() {
        System.out.println(DisplayFormatter.header("Виконавці"));

        List<Artist> artists = dataContext.artists().findAll();

        if (artists.isEmpty()) {
            System.out.println(DisplayFormatter.warning("Виконавців не знайдено"));
            DisplayFormatter.waitForKeyPress();
            return;
        }

        System.out.println(DisplayFormatter.bold("\nСписок виконавців:\n"));
        for (int i = 0; i < artists.size(); i++) {
            Artist artist = artists.get(i);
            System.out.println(String.format("%d. %s", (i + 1), artist.getStageName()));
        }

        String indexStr = menuBuilder.createInputPrompt("\nВведіть номер виконавця для деталей (0 для повернення):",
                "0");
        try {
            int index = Integer.parseInt(indexStr) - 1;
            if (index >= 0 && index < artists.size()) {
                displayArtistDetails(artists.get(index));
            }
        } catch (NumberFormatException e) {
            System.out.println(DisplayFormatter.error("Невірний формат номера"));
        }
    }

    private void displayArtistDetails(Artist artist) {
        System.out.println(DisplayFormatter.header("Виконавець: " + artist.getStageName()));
        System.out.println(DisplayFormatter.bold("Біографія:\n") + artist.getBio());

        String back = menuBuilder.createInputPrompt("\nНатисніть Enter для повернення...", "");
    }

    private void browseAlbums() {
        System.out.println(DisplayFormatter.header("Альбоми"));

        List<Album> albums = dataContext.albums().findAll();

        if (albums.isEmpty()) {
            System.out.println(DisplayFormatter.warning("Альбомів не знайдено"));
            DisplayFormatter.waitForKeyPress();
            return;
        }

        System.out.println(DisplayFormatter.bold("\nСписок альбомів:\n"));
        for (int i = 0; i < albums.size(); i++) {
            Album album = albums.get(i);
            System.out.println(String.format("%d. %s (%s)",
                    (i + 1),
                    album.getTitle(),
                    album.getReleaseDate()));
        }

        String indexStr = menuBuilder.createInputPrompt("\nВведіть номер альбому для деталей (0 для повернення):", "0");
        try {
            int index = Integer.parseInt(indexStr) - 1;
            if (index >= 0 && index < albums.size()) {
                displayAlbumDetails(albums.get(index));
            }
        } catch (NumberFormatException e) {
            System.out.println(DisplayFormatter.error("Невірний формат номера"));
        }
    }

    private void displayAlbumDetails(Album album) {
        System.out.println(DisplayFormatter.header("Альбом: " + album.getTitle()));
        System.out.println(DisplayFormatter.bold("Дата випуску: ") + album.getReleaseDate());

        try {
            Artist artist = dataContext.artists().findById(album.getArtistId()).orElse(null);
            if (artist != null) {
                System.out.println(DisplayFormatter.bold("Виконавець: ") + artist.getStageName());
            }
        } catch (Exception e) {
        }

        String back = menuBuilder.createInputPrompt("\nНатисніть Enter для повернення...", "");
    }

    private void browseGenres() {
        System.out.println(DisplayFormatter.header("Жанри"));

        List<Genre> genres = dataContext.genres().findAll();

        if (genres.isEmpty()) {
            System.out.println(DisplayFormatter.warning("Жанрів не знайдено"));
            DisplayFormatter.waitForKeyPress();
            return;
        }

        System.out.println(DisplayFormatter.bold("\nСписок жанрів:\n"));
        for (int i = 0; i < genres.size(); i++) {
            Genre genre = genres.get(i);
            System.out.println(String.format("%d. %s", (i + 1), genre.getName()));
        }

        String back = menuBuilder.createInputPrompt("\nНатисніть Enter для повернення...", "");
    }
}
