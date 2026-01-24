package ua.notion.musiclibrary;

import uk.co.caprica.vlcj.player.component.AudioPlayerComponent;

public class Main {
  public static void main(String[] args) {
    // // Отримуємо singleton контекст репозиторіїв
    // DataContext context = DataContext.getInstance();

    // // Створюємо застосунок з контекстом
    // LibraryApplication libraryApplication = new LibraryApplication(context);

    // libraryApplication.run();

    // 1. Створюємо компонент аудіоплеєра
    AudioPlayerComponent audioPlayerComponent = new AudioPlayerComponent();

    // 2. Вказуємо шлях до файлу (працює з будь-яким форматом)
    String filePath = "data/audio/1.mp3";

    // 3. Запускаємо відтворення
    audioPlayerComponent.mediaPlayer().media().play(filePath);

    System.out.println("Грає музика... Натисніть Enter, щоб вимкнути.");

    // Керування
    // audioPlayerComponent.mediaPlayer().controls().pause();
    // audioPlayerComponent.mediaPlayer().audio().setVolume(80);

    try {
      System.in.read();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      audioPlayerComponent.release();
      System.exit(0);
    }
  }
}
