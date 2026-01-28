package ua.notion.musiclibrary.ui;

import org.jline.terminal.Terminal;
import ua.notion.musiclibrary.domain.impl.User;

public class TuiSession {

    private final Terminal terminal;
    private User currentUser;

    public TuiSession(Terminal terminal) {
        this.terminal = terminal;
    }

    public boolean isAuthenticated() {
        return currentUser != null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public void logout() {
        this.currentUser = null;
    }

    public Terminal getTerminal() {
        return terminal;
    }
}
