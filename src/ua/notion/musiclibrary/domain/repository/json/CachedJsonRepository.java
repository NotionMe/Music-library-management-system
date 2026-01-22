package ua.notion.musiclibrary.domain.repository.json;

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
import java.util.function.Function;
import java.util.function.Predicate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ua.notion.musiclibrary.domain.exception.RepositoryException;
import ua.notion.musiclibrary.domain.repository.Repository;

/**
 * Покращена версія JsonRepository з підтримкою Identity Map.
 */
public abstract class CachedJsonRepository<T, ID> implements Repository<T, ID> {

    protected final Path filePath;
    protected final Gson gson;
    protected final Type listType;
    protected final Function<T, ID> idExtractor;

    // Identity Map для кешування
    protected final IdentityMap<ID, T> identityMap = new IdentityMap<>();

    // Прапорець "брудного" кешу
    private boolean cacheValid = false;
    private List<T> cachedList = null;

    protected CachedJsonRepository(String filename, Type listType, Function<T, ID> idExtractor) {
        this.filePath = Path.of(filename);
        this.listType = listType;
        this.idExtractor = idExtractor;
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
        ID id = idExtractor.apply(entity);

        // Оновлюємо Identity Map
        identityMap.put(id, entity);

        // Інвалідуємо кеш списку
        invalidateCache();

        // Зберігаємо у файл
        List<T> entities = loadFromFile();

        boolean found = false;
        for (int i = 0; i < entities.size(); i++) {
            if (idExtractor.apply(entities.get(i)).equals(id)) {
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
    public Optional<T> findById(ID id) {
        // Спочатку перевіряємо Identity Map
        Optional<T> cached = identityMap.get(id);
        if (cached.isPresent()) {
            return cached;
        }

        // Якщо немає в кеші — шукаємо у файлі
        Optional<T> found = findAllInternal().stream()
                .filter(entity -> idExtractor.apply(entity).equals(id))
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
    public boolean deleteById(ID id) {
        // Видаляємо з Identity Map
        identityMap.remove(id);
        invalidateCache();

        List<T> entities = loadFromFile();
        boolean removed = entities.removeIf(entity -> idExtractor.apply(entity).equals(id));

        if (removed) {
            writeToFile(entities);
        }
        return removed;
    }

    @Override
    public boolean delete(T entity) {
        return deleteById(idExtractor.apply(entity));
    }

    @Override
    public boolean existsById(ID id) {
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
            ID id = idExtractor.apply(entity);
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
