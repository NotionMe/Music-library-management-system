package ua.notion.musiclibrary.domain.repository.json;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.google.gson.reflect.TypeToken;

import ua.notion.musiclibrary.domain.model.Genre;
import ua.notion.musiclibrary.domain.repository.GenreRepository;

public class JsonGenreRepository extends CachedJsonRepository<Genre, UUID> implements GenreRepository {

    public JsonGenreRepository(String filename) {
        super(
                filename,
                new TypeToken<ArrayList<Genre>>() {
                }.getType(),
                Genre::getID);
    }

    @Override
    public List<Genre> findByName(String name) {
        return findBy(genre -> genre.getName().equals(name));
    }
}
