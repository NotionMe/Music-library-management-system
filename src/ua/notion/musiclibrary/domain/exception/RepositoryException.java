package ua.notion.musiclibrary.domain.exception;

/**
 * Виняток, що виникає при помилках роботи з репозиторієм.
 */
public class RepositoryException extends RuntimeException {

    public RepositoryException(String message) {
        super(message);
    }

    public RepositoryException(String message, Throwable cause) {
        super(message, cause);
    }
}
