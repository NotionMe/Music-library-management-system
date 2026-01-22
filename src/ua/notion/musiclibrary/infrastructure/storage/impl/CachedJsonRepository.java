package ua.notion.musiclibrary.infrastructure.storage.impl;

import ua.notion.musiclibrary.domain.impl.Entity;
import ua.notion.musiclibrary.domain.exception.RepositoryException;
import ua.notion.musiclibrary.infrastructure.storage.Repository;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Покращена версія JsonRepository з підтримкою Identity Map.
 *
 * @param <T> тип сутності, яка розширює Entity
 */
public abstract class CachedJsonRepository<T extends Entity> implements Repository<T> {

    protected final Path filePath;
    protected final Gson gson;
    protected final Type listType;

    // Identity Map для кешування
    protected final IdentityMap<UUID, T> identityMap = new IdentityMap<>();

    // Прапорець "брудного" кешу
    private boolean cacheValid = false;
    private List<T> cachedList = null;

    protected CachedJsonRepository(String filename, Type listType) {
        this.filePath = Path.of(filename);
        this.listType = listType;
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        ensureDirectoryExists();
    }

    private void ensureDirectoryExists() {
        Path parent = filePath.getParent();
        if (parent != null && !Files.exists(parent)) {
            try {
                Files.createDirectories(parent);
            } catch (IOException e) {
                throw new RepositoryException("Не вдалося створити директорію: " + parent, e);
            }
        }
    }

    @Override
    public T save(T entity) {
        UUID id = entity.getID();

        // Оновлюємо Identity Map
        identityMap.put(id, entity);

        // Інвалідуємо кеш списку
        invalidateCache();

        // Зберігаємо у файл
        List<T> entities = loadFromFile();

        boolean found = false;
        for (int i = 0; i < entities.size(); i++) {
            if (entities.get(i).getID().equals(id)) {
                entities.set(i, entity);
                found = true;
                break;
            }
        }

        if (!found) {
            entities.add(entity);
        }

        writeToFile(entities);
        return entity;
    }

    @Override
    public Optional<T> findById(UUID id) {
        // Спочатку перевіряємо Identity Map
        Optional<T> cached = identityMap.get(id);
        if (cached.isPresent()) {
            return cached;
        }

        // Якщо немає в кеші — шукаємо у файлі
        Optional<T> found = findAllInternal().stream()
                .filter(entity -> entity.getID().equals(id))
                .findFirst();

        // Додаємо знайдене в Identity Map
        found.ifPresent(entity -> identityMap.put(id, entity));

        return found;
    }

    @Override
    public List<T> findAll() {
        return new ArrayList<>(findAllInternal());
    }

    @Override
    public boolean deleteById(UUID id) {
        // Видаляємо з Identity Map
        identityMap.remove(id);
        invalidateCache();

        List<T> entities = loadFromFile();
        boolean removed = entities.removeIf(entity -> entity.getID().equals(id));

        if (removed) {
            writeToFile(entities);
        }
        return removed;
    }

    @Override
    public boolean delete(T entity) {
        return deleteById(entity.getID());
    }

    @Override
    public boolean existsById(UUID id) {
        return identityMap.contains(id) || findById(id).isPresent();
    }

    @Override
    public long count() {
        return findAllInternal().size();
    }

    /**
     * Інвалідує кеш. Викликається при модифікації даних.
     */
    protected void invalidateCache() {
        cacheValid = false;
        cachedList = null;
    }

    /**
     * Очищає Identity Map та кеш. Корисно для тестування.
     */
    public void clearCache() {
        identityMap.clear();
        invalidateCache();
    }

    protected List<T> findBy(Predicate<T> predicate) {
        return findAllInternal().stream()
                .filter(predicate)
                .toList();
    }

    protected Optional<T> findFirstBy(Predicate<T> predicate) {
        return findAllInternal().stream()
                .filter(predicate)
                .findFirst();
    }

    /**
     * Повертає всі сутності з кешу або файлу.
     */
    protected List<T> findAllInternal() {
        if (cacheValid && cachedList != null) {
            return cachedList;
        }

        cachedList = loadFromFile();
        cacheValid = true;

        // Заповнюємо Identity Map
        for (T entity : cachedList) {
            UUID id = entity.getID();
            if (!identityMap.contains(id)) {
                identityMap.put(id, entity);
            }
        }

        return cachedList;
    }

    private List<T> loadFromFile() {
        if (!Files.exists(filePath)) {
            return new ArrayList<>();
        }

        try (Reader reader = new FileReader(filePath.toFile())) {
            List<T> entities = gson.fromJson(reader, listType);
            return entities != null ? new ArrayList<>(entities) : new ArrayList<>();
        } catch (IOException e) {
            throw new RepositoryException("Помилка читання: " + filePath, e);
        }
    }

    protected void writeToFile(List<T> entities) {
        try (Writer writer = new FileWriter(filePath.toFile())) {
            gson.toJson(entities, writer);
        } catch (IOException e) {
            throw new RepositoryException("Помилка запису: " + filePath, e);
        }
    }
}
