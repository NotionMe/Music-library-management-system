package ua.notion.musiclibrary;

import ua.notion.musiclibrary.infrastructure.storage.impl.DataContext;
import ua.notion.musiclibrary.test.LibraryApplication;

public class Main {
  public static void main(String[] args) {
    // Отримуємо singleton контекст репозиторіїв
    DataContext context = DataContext.getInstance();

    // Створюємо застосунок з контекстом
    LibraryApplication libraryApplication = new LibraryApplication(context);

    libraryApplication.run();
  }
}
