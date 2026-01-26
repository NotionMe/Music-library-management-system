package ua.notion.musiclibrary.ui;

import java.io.IOException;

import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import ua.notion.musiclibrary.infrastructure.storage.impl.DataContext;
import ua.notion.musiclibrary.service.impl.AuthServiceImpl;
import ua.notion.musiclibrary.service.impl.EmailServiceImpl;
import ua.notion.musiclibrary.ui.controller.AuthenticationController;
import ua.notion.musiclibrary.ui.controller.MainMenuController;
import ua.notion.musiclibrary.ui.util.DisplayFormatter;

public class TuiApplication {

    private TuiSession session;
    private Terminal terminal;
    private AuthenticationController authController;
    private MainMenuController mainMenuController;

    public void start() {
        try {
            terminal = TerminalBuilder.builder().build();
            session = new TuiSession(terminal);

            DataContext dataContext = DataContext.getInstance();
            authController = new AuthenticationController(
                    new AuthServiceImpl(dataContext, new EmailServiceImpl(System.getenv("APP_GOOGLE"))),
                    session);
            mainMenuController = new MainMenuController(session);

            showWelcome();
            runMainLoop();

        } catch (IOException e) {
            System.err.println("Помилка ініціалізації терміналу: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Критична помилка: " + e.getMessage());
            e.printStackTrace();
        } finally {
            shutdown();
        }
    }

    private void showWelcome() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println(DisplayFormatter.bold("    Система Управління Музичною Бібліотекою"));
        System.out.println("=".repeat(80) + "\n");
    }

    private void runMainLoop() {
        boolean running = true;

        while (running) {
            try {
                if (session.isAuthenticated()) {
                    running = mainMenuController.showMainMenu();

                    if (!session.isAuthenticated()) {
                        continue;
                    }
                } else {
                    running = authController.showAuthMenu();
                }
            } catch (Exception e) {
                System.out.println(DisplayFormatter.error("Помилка: " + e.getMessage()));
                e.printStackTrace();
            }
        }
    }

    private void shutdown() {
        try {
            if (terminal != null) {
                terminal.close();
            }
        } catch (IOException e) {
            System.err.println("Помилка закриття терміналу: " + e.getMessage());
        }

        System.out.println(
                DisplayFormatter.success("\nДякуємо за використання Системи Управління Музичною Бібліотекою!"));
        System.out.println("До побачення!\n");
    }
}
