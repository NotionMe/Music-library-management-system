package ua.notion.musiclibrary.ui.controller;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import ua.notion.musiclibrary.domain.impl.Album;
import ua.notion.musiclibrary.domain.impl.Artist;
import ua.notion.musiclibrary.domain.impl.Genre;
import ua.notion.musiclibrary.dto.track.TrackCreateDto;
import ua.notion.musiclibrary.dto.track.TrackDto;
import ua.notion.musiclibrary.infrastructure.storage.impl.DataContext;
import ua.notion.musiclibrary.service.contract.TrackService;
import ua.notion.musiclibrary.service.impl.TrackServiceImpl;
import ua.notion.musiclibrary.ui.TuiSession;
import ua.notion.musiclibrary.ui.util.DisplayFormatter;
import ua.notion.musiclibrary.ui.util.InputValidator;
import ua.notion.musiclibrary.ui.util.MenuBuilder;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.base.MediaPlayer;

public class TrackUploadController {

    private final TuiSession session;
    private final MenuBuilder menuBuilder;
    private final DataContext dataContext;
    private final TrackService trackService;

    public TrackUploadController(TuiSession session) {
        this.session = session;
        this.menuBuilder = new MenuBuilder(session.getTerminal());
        this.dataContext = DataContext.getInstance();
        this.trackService = new TrackServiceImpl(dataContext);
    }

    public void showUploadMenu() {
        DisplayFormatter.clearScreen();
        System.out.println(DisplayFormatter.header("Завантажити Трек"));

        String title = menuBuilder.createInputPrompt("Назва треку:", "");
        var titleValidation = InputValidator.validateNotEmpty(title, "Назва");
        if (!titleValidation.isValid()) {
            System.out.println(DisplayFormatter.error(titleValidation.getErrorMessage()));
            DisplayFormatter.waitForKeyPress();
            return;
        }

        String audioFilePath = menuBuilder.createInputPrompt(
                "Шлях до аудіофайлу (або Enter щоб пропустити):",
                "");

        Duration duration = null;

        if (!audioFilePath.isBlank()) {
            duration = extractDurationFromFile(audioFilePath);
            if (duration != null) {
                System.out.println(DisplayFormatter.success("✅ Тривалість автоматично визначена: " +
                        formatDuration(duration)));
            }
        }

        if (duration == null) {
            String durationStr = menuBuilder.createInputPrompt("Тривалість (секунди):", "180");
            try {
                int durationSeconds = Integer.parseInt(durationStr);
                if (durationSeconds <= 0) {
                    throw new NumberFormatException();
                }
                duration = Duration.ofSeconds(durationSeconds);
            } catch (NumberFormatException e) {
                System.out.println(DisplayFormatter.error("Невірний формат тривалості"));
                DisplayFormatter.waitForKeyPress();
                return;
            }
        }

        UUID albumId = selectOrCreateAlbum();
        if (albumId == null) {
            System.out.println(DisplayFormatter.error("Альбом не обрано. Скасовано."));
            DisplayFormatter.waitForKeyPress();
            return;
        }

        Set<UUID> artistIds = selectArtists();
        Set<UUID> genreIds = selectGenres();

        try {
            TrackCreateDto createDto = new TrackCreateDto(
                    title,
                    duration,
                    albumId,
                    genreIds,
                    artistIds,
                    audioFilePath.isBlank() ? null : audioFilePath,
                    session.getCurrentUser().getID());

            TrackDto track = trackService.createTrack(createDto);
            System.out.println(DisplayFormatter.success("✅ Трек '" + track.title() + "' успішно створено!"));
            System.out.println(DisplayFormatter.info("Статус: PUBLISHED (доступний для всіх)"));
            DisplayFormatter.waitForKeyPress();
        } catch (Exception e) {
            System.out.println(DisplayFormatter.error("Помилка створення: " + e.getMessage()));
            System.out.println("Деталі помилки:");
            e.printStackTrace();
            DisplayFormatter.waitForKeyPress();
        }
    }

    private Set<UUID> selectArtists() {
        Set<UUID> artistIds = new HashSet<>();
        List<Artist> artists = dataContext.artists().findAll();

        if (artists.isEmpty()) {
            return artistIds;
        }

        System.out.println("\nДоступні артисти:");
        for (int i = 0; i < artists.size(); i++) {
            System.out.println(String.format("%d. %s", (i + 1), artists.get(i).getStageName()));
        }

        String artistIndexStr = menuBuilder.createInputPrompt("Номер артиста (або Enter щоб пропустити):", "1");
        if (!artistIndexStr.isBlank()) {
            try {
                int artistIndex = Integer.parseInt(artistIndexStr) - 1;
                if (artistIndex >= 0 && artistIndex < artists.size()) {
                    artistIds.add(artists.get(artistIndex).getID());
                }
            } catch (NumberFormatException e) {
            }
        }

        return artistIds;
    }

    private Set<UUID> selectGenres() {
        Set<UUID> genreIds = new HashSet<>();
        List<Genre> genres = dataContext.genres().findAll();

        if (genres.isEmpty()) {
            return genreIds;
        }

        System.out.println("\nДоступні жанри:");
        for (int i = 0; i < genres.size(); i++) {
            System.out.println(String.format("%d. %s", (i + 1), genres.get(i).getName()));
        }

        String genreIndexStr = menuBuilder.createInputPrompt("Номер жанру (або Enter щоб пропустити):", "1");
        if (!genreIndexStr.isBlank()) {
            try {
                int genreIndex = Integer.parseInt(genreIndexStr) - 1;
                if (genreIndex >= 0 && genreIndex < genres.size()) {
                    genreIds.add(genres.get(genreIndex).getID());
                }
            } catch (NumberFormatException e) {
            }
        }

        return genreIds;
    }

    private UUID selectOrCreateAlbum() {
        List<Album> albums = dataContext.albums().findAll();

        if (!albums.isEmpty()) {
            System.out.println("\nДоступні альбоми:");
            for (int i = 0; i < albums.size(); i++) {
                Album album = albums.get(i);
                System.out.println(String.format("%d. %s", (i + 1), album.getTitle()));
            }
            System.out.println("0. Створити новий альбом");

            String choice = menuBuilder.createInputPrompt("Ваш вибір:", "0");
            try {
                int index = Integer.parseInt(choice);
                if (index > 0 && index <= albums.size()) {
                    return albums.get(index - 1).getID();
                }
            } catch (NumberFormatException e) {
            }
        }
        return createNewAlbum();
    }

    private UUID createNewAlbum() {
        int attempts = 0;
        while (attempts < 3) {
            System.out.println(DisplayFormatter.header("Створити Новий Альбом"));

            String albumTitle = menuBuilder.createInputPrompt("Назва альбому (або 'q' для скасування):", "");

            if (albumTitle.equals("q")) {
                System.out.println(DisplayFormatter.warning("Створення альбому скасовано"));
                return null;
            }

            if (albumTitle.isBlank()) {
                System.out.println(DisplayFormatter.error("Назва не може бути порожньою"));
                attempts++;
                continue;
            }

            UUID artistId = selectOrCreateArtist();
            if (artistId == null) {
                System.out.println(DisplayFormatter.warning("Артист не обраний, скасування..."));
                return null;
            }

            Album album = new Album(albumTitle, LocalDate.now(), artistId);
            dataContext.albums().save(album);
            dataContext.commit();

            System.out.println(DisplayFormatter.success("✅ Альбом '" + albumTitle + "' створено!"));
            return album.getID();
        }

        System.out.println(DisplayFormatter.error("Перевищено кількість спроб"));
        return null;
    }

    private UUID selectOrCreateArtist() {
        List<Artist> artists = dataContext.artists().findAll();

        if (!artists.isEmpty()) {
            System.out.println("\nДоступні артисти:");
            for (int i = 0; i < artists.size(); i++) {
                System.out.println(String.format("%d. %s", (i + 1), artists.get(i).getStageName()));
            }
            System.out.println("0. Створити нового артиста");

            String choice = menuBuilder.createInputPrompt("Ваш вибір:", "0");
            try {
                int index = Integer.parseInt(choice);
                if (index > 0 && index <= artists.size()) {
                    return artists.get(index - 1).getID();
                }
            } catch (NumberFormatException e) {
            }
        }

        return createNewArtist();
    }

    private UUID createNewArtist() {
        int attempts = 0;
        while (attempts < 3) {
            System.out.println(DisplayFormatter.header("Створити Нового Артиста"));

            String stageName = menuBuilder.createInputPrompt("Ім'я артиста (або 'q' для скасування):", "");

            if (stageName.equals("q")) {
                System.out.println(DisplayFormatter.warning("Створення артиста скасовано"));
                return null;
            }

            if (stageName.isBlank()) {
                System.out.println(DisplayFormatter.error("Ім'я не може бути порожнім"));
                attempts++;
                continue;
            }

            String bio = menuBuilder.createInputPrompt("Біографія (опціонально, Enter для пропуску):", "");
            if (bio.isBlank()) {
                bio = "Немає біографії";
            }

            Artist artist = new Artist(stageName, bio, session.getCurrentUser().getID());
            dataContext.artists().save(artist);
            dataContext.commit();

            System.out.println(DisplayFormatter.success("✅ Артиста '" + stageName + "' створено!"));
            return artist.getID();
        }

        System.out.println(DisplayFormatter.error("Перевищено кількість спроб"));
        return null;
    }

    private Duration extractDurationFromFile(String filePath) {
        try {
            java.io.File audioFile = new java.io.File(filePath);
            if (!audioFile.exists()) {
                System.out.println(DisplayFormatter.warning("⚠ Файл не знайдено: " + filePath));
                return null;
            }

            MediaPlayerFactory factory = new MediaPlayerFactory();
            MediaPlayer tempPlayer = factory.mediaPlayers().newMediaPlayer();

            tempPlayer.media().play(audioFile.getAbsolutePath());
            Thread.sleep(100);

            long durationMillis = tempPlayer.status().length();

            tempPlayer.controls().stop();
            tempPlayer.release();
            factory.release();

            if (durationMillis > 0) {
                return Duration.ofMillis(durationMillis);
            }
            return null;
        } catch (Exception e) {
            System.out.println(DisplayFormatter.warning("⚠ Не вдалося визначити тривалість з файлу"));
            return null;
        }
    }

    private String formatDuration(Duration duration) {
        long seconds = duration.getSeconds();
        long minutes = seconds / 60;
        long secs = seconds % 60;
        return String.format("%d:%02d", minutes, secs);
    }
}
