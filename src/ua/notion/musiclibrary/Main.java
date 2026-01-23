package ua.notion.musiclibrary;

import ua.notion.musiclibrary.infrastructure.storage.impl.DataContext;
import ua.notion.musiclibrary.test.LibraryApplication;

import com.password4j.Password;
import com.password4j.Hash;

public class Main {
  public static void main(String[] args) {
    // // Отримуємо singleton контекст репозиторіїв
    // DataContext context = DataContext.getInstance();

    // // Створюємо застосунок з контекстом
    // LibraryApplication libraryApplication = new LibraryApplication(context);

    // libraryApplication.run();

    String rawPassword = "мій_пароль_2026";

    // 1. Хешування
    Hash hash = Password.hash(rawPassword).withBcrypt();
    String hashString = hash.getResult();
    System.out.println("Хеш: " + hashString);

    // 2. Перевірка
    boolean isCorrect = Password.check(rawPassword, hashString).withBcrypt();
    System.out.println("Пароль вірний: " + isCorrect);
  }
}
