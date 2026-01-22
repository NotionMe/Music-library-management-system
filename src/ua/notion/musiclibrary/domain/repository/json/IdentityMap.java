package ua.notion.musiclibrary.domain.repository.json;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Реалізація патерну Identity Map.
 * Кешує завантажені сутності за їх ідентифікаторами.
 *
 * @param <ID> тип ідентифікатора
 * @param <T>  тип сутності
 */
public class IdentityMap<ID, T> {

    private final Map<ID, T> cache = new HashMap<>();

    /**
     * Отримує сутність з кешу.
     *
     * @param id ідентифікатор
     * @return Optional з сутністю або порожній Optional
     */
    public Optional<T> get(ID id) {
        return Optional.ofNullable(cache.get(id));
    }

    /**
     * Додає сутність до кешу.
     *
     * @param id     ідентифікатор
     * @param entity сутність
     */
    public void put(ID id, T entity) {
        cache.put(id, entity);
    }

    /**
     * Видаляє сутність з кешу.
     *
     * @param id ідентифікатор
     */
    public void remove(ID id) {
        cache.remove(id);
    }

    /**
     * Очищає весь кеш.
     */
    public void clear() {
        cache.clear();
    }

    /**
     * Перевіряє наявність сутності в кеші.
     *
     * @param id ідентифікатор
     * @return true, якщо сутність є в кеші
     */
    public boolean contains(ID id) {
        return cache.containsKey(id);
    }

    /**
     * Повертає кількість закешованих сутностей.
     *
     * @return розмір кешу
     */
    public int size() {
        return cache.size();
    }
}