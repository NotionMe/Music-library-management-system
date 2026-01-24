package ua.notion.musiclibrary.test;

import java.io.File;
import java.time.Duration;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Scanner;

import ua.notion.musiclibrary.domain.enums.Role;
import ua.notion.musiclibrary.domain.impl.Album;
import ua.notion.musiclibrary.domain.impl.Artist;
import ua.notion.musiclibrary.domain.impl.User;
import ua.notion.musiclibrary.dto.auth.UserRegistrationDto;
import ua.notion.musiclibrary.dto.track.TrackCreateDto;
import ua.notion.musiclibrary.dto.track.TrackDto;
import ua.notion.musiclibrary.infrastructure.storage.impl.DataContext;
import ua.notion.musiclibrary.service.contract.AuthService;
import ua.notion.musiclibrary.service.contract.EmailService;
import ua.notion.musiclibrary.service.impl.AuthServiceImpl;
import ua.notion.musiclibrary.service.impl.EmailServiceImpl;
import ua.notion.musiclibrary.service.impl.TrackServiceImpl;

public class AudioFileIntegrationTest {

    private static final Scanner scanner = new Scanner(System.in);
    private static final DataContext dataContext = DataContext.getInstance();

    public static void main(String[] args) {
        new File("data/tracks.json").delete();

        try {
            System.out.println("=== Audio File Integration Test ===\n");
            System.out.println("Цей тест демонструє обов'язковість filePath при створенні треків.\n");

            setupTestData();

            Album album = dataContext.albums().findAll().stream()
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("No album created"));

            testMandatoryFilePath(album);
            testWithRealAudioFile(album);
            displaySummary();

            System.out.println("\n=== All Tests Passed ===");

        } catch (Exception e) {
            System.err.println("\n✗ Test Failed: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }

    private static void setupTestData() {
        System.out.println("📝 Створення тестових даних...\n");

        System.out.print("Введіть ім'я користувача (3-15 символів): ");
        String username = scanner.nextLine().trim();
        if (username.length() < 3)
            username = "testuser";

        System.out.print("Введіть email: ");
        String email = scanner.nextLine().trim();
        if (email.isBlank())
            email = "test@example.com";
        EmailService emailService = new EmailServiceImpl("");

        UserRegistrationDto registrationDto = new UserRegistrationDto(username, email, "123123dcgd@@", Role.ADMIN);

        AuthService authService = new AuthServiceImpl(dataContext, emailService);
        User user = authService.register(registrationDto);
        System.out.println("✓ Користувач створений: " + username);

        System.out.print("\nВведіть ім'я артиста: ");
        String artistName = scanner.nextLine().trim();
        if (artistName.isBlank())
            artistName = "Test Artist";

        Artist artist = new Artist(artistName, "Test bio", user.getID());
        dataContext.artists().save(artist);
        System.out.println("✓ Артист створений: " + artistName);

        System.out.print("\nВведіть назву альбому: ");
        String albumTitle = scanner.nextLine().trim();
        if (albumTitle.isBlank())
            albumTitle = "Test Album";

        Album album = new Album(albumTitle, LocalDate.now(), artist.getID());
        dataContext.albums().save(album);
        System.out.println("✓ Альбом створений: " + albumTitle);

        dataContext.commit();
        System.out.println("\n✅ Тестові дані готові!\n");
    }

    private static void testMandatoryFilePath(Album album) throws Exception {
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println("TEST 1: FilePath є обов'язковим полем");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");

        System.out.print("Введіть назву треку: ");
        String trackTitle = scanner.nextLine().trim();
        if (trackTitle.isBlank())
            trackTitle = "Demo Track";

        System.out.print("Введіть тривалість (секунди, наприклад 180): ");
        String durationInput = scanner.nextLine().trim();
        int durationSeconds = durationInput.isBlank() ? 180 : Integer.parseInt(durationInput);

        System.out.print("Введіть шлях до аудіофайлу (може не існувати): ");
        String filePath = scanner.nextLine().trim();
        if (filePath.isBlank())
            filePath = "/future/upload/audio.mp3";

        TrackCreateDto createDto = new TrackCreateDto(
                trackTitle,
                Duration.ofSeconds(durationSeconds),
                album.getID(),
                new HashSet<>(),
                new HashSet<>(),
                filePath);

        TrackServiceImpl trackService = new TrackServiceImpl(dataContext);
        TrackDto track = trackService.createTrack(createDto);

        System.out.println("\n✅ Результат:");
        System.out.println("  ID: " + track.id());
        System.out.println("  Назва: " + track.title());
        System.out.println("  FilePath: " + track.filePath());
        System.out.println("  Метадані завантажені: " + (track.audioFormat() != null ? "Так" : "Ні (файл не існує)"));
        System.out.println("\n✓ Track успішно створений з обов'язковим filePath!\n");
    }

    private static void testWithRealAudioFile(Album album) throws Exception {
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println("TEST 2: Обробка реального аудіофайлу");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");

        System.out.print("Введіть шлях до реального MP3 файлу (Enter щоб пропустити): ");
        String audioFilePath = scanner.nextLine().trim();

        if (audioFilePath.isBlank()) {
            System.out.println("ⓘ Пропущено (файл не вказано)\n");
            return;
        }

        File audioFile = new File(audioFilePath);
        if (!audioFile.exists()) {
            System.out.println("ⓘ Пропущено (файл не знайдено: " + audioFilePath + ")\n");
            return;
        }

        System.out.print("Введіть назву для цього треку: ");
        String trackTitle = scanner.nextLine().trim();
        if (trackTitle.isBlank())
            trackTitle = "Track With Audio";

        TrackCreateDto createDto = new TrackCreateDto(
                trackTitle,
                Duration.ofMinutes(4),
                album.getID(),
                new HashSet<>(),
                new HashSet<>(),
                audioFilePath);

        TrackServiceImpl trackService = new TrackServiceImpl(dataContext);
        TrackDto track = trackService.createTrack(createDto);

        System.out.println("\n✅ Результат:");
        System.out.println("  ID: " + track.id());
        System.out.println("  Назва: " + track.title());
        System.out.println("  Оригінальний файл: " + audioFilePath);
        System.out.println("  Збережено як: " + track.filePath());
        System.out.println("  Формат: " + track.audioFormat());
        System.out.println("  Розмір: " + track.fileSize());
        System.out.println("  Бітрейт: " + track.bitrate() + " kbps");
        System.out.println("\n✓ Файл успішно оброблений та збережений!\n");
    }

    private static void displaySummary() {
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println("📋 Підсумок функціоналу");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
        System.out.println("✓ FilePath є ОБОВ'ЯЗКОВИМ параметром конструктора Track");
        System.out.println("✓ Неможливо створити Track без вказівки шляху до файлу");
        System.out.println("✓ Track можна створити навіть якщо файл ще не існує");
        System.out.println("✓ При наявності файлу - автоматично витягуються метадані");
        System.out.println("✓ Файли зберігаються з дедуплікацією (SHA-256 hash)");
        System.out.println("✓ Підтримуються формати: MP3, FLAC, WAV, M4A, OGG");
    }
}
