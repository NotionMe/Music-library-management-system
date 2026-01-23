package ua.notion.musiclibrary;

import java.util.UUID;

import ua.notion.musiclibrary.infrastructure.storage.impl.DataContext;
import ua.notion.musiclibrary.service.contract.EmailService;
import ua.notion.musiclibrary.service.impl.EmailServiceImpl;
import ua.notion.musiclibrary.test.LibraryApplication;

public class Main {
  public static void main(String[] args) {
    // Отримуємо singleton контекст репозиторіїв
    // DataContext context = DataContext.getInstance();

    // Створюємо застосунок з контекстом
    // LibraryApplication libraryApplication = new LibraryApplication(context);

    // libraryApplication.run();

    // EmailService email = new EmailServiceImpl("adlo nula ulej jwxg",
    // "hagami16@gmail.com");
    // email.sendPasswordCode("ubogijstas@gmail.com", "elpacho");

  }
}
