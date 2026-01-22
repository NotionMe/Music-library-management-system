package ua.notion.musiclibrary.domain.repository.json;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Function;

import ua.notion.musiclibrary.domain.repository.Repository;

/**
 * Реалізація патерну Unit of Work.
 * Відстежує зміни та координує їх збереження.
 *
 * @param <T>  тип сутності
 * @param <ID> тип ідентифікатора
 */
public class UnitOfWork<T, ID> {

    private final Set<T> newEntities = new LinkedHashSet<>();
    private final Set<T> dirtyEntities = new LinkedHashSet<>();
    private final Set<ID> deletedIds = new LinkedHashSet<>();

    private final Function<T, ID> idExtractor;
    private final Repository<T, ID> repository;

    public UnitOfWork(Repository<T, ID> repository, Function<T, ID> idExtractor) {
        this.repository = repository;
        this.idExtractor = idExtractor;
    }

    /**
     * Реєструє нову сутність для вставки.
     */
    public void registerNew(T entity) {
        ID id = idExtractor.apply(entity);
        deletedIds.remove(id);
        dirtyEntities.remove(entity);
        newEntities.add(entity);
    }

    /**
     * Реєструє змінену сутність для оновлення.
     */
    public void registerDirty(T entity) {
        ID id = idExtractor.apply(entity);
        if (!newEntities.contains(entity) && !deletedIds.contains(id)) {
            dirtyEntities.add(entity);
        }
    }

    /**
     * Реєструє сутність для видалення.
     */
    public void registerDeleted(T entity) {
        ID id = idExtractor.apply(entity);
        if (newEntities.remove(entity)) {
            return; // Нова сутність — просто видаляємо з черги
        }
        dirtyEntities.remove(entity);
        deletedIds.add(id);
    }

    /**
     * Фіксує всі зміни в репозиторії.
     */
    public void commit() {
        // Спочатку вставляємо нові
        for (T entity : newEntities) {
            repository.save(entity);
        }

        // Потім оновлюємо змінені
        for (T entity : dirtyEntities) {
            repository.save(entity);
        }

        // Нарешті видаляємо
        for (ID id : deletedIds) {
            repository.deleteById(id);
        }

        // Очищаємо черги
        clear();
    }

    /**
     * Відкочує всі незбережені зміни.
     */
    public void rollback() {
        clear();
    }

    /**
     * Очищає всі черги.
     */
    public void clear() {
        newEntities.clear();
        dirtyEntities.clear();
        deletedIds.clear();
    }

    /**
     * Перевіряє, чи є незбережені зміни.
     */
    public boolean hasChanges() {
        return !newEntities.isEmpty() || !dirtyEntities.isEmpty() || !deletedIds.isEmpty();
    }

    /**
     * Повертає статистику змін.
     */
    public String getChangesSummary() {
        return String.format(
                "New: %d, Dirty: %d, Deleted: %d",
                newEntities.size(),
                dirtyEntities.size(),
                deletedIds.size());
    }
}
