package ua.notion.musiclibrary.infrastructure.storage.impl;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.reflect.TypeToken;

import ua.notion.musiclibrary.domain.impl.Genre;
import ua.notion.musiclibrary.infrastructure.storage.contract.GenreRepository;

class JsonGenreRepository extends CachedJsonRepository<Genre> implements GenreRepository {

    public JsonGenreRepository(String filename) {
        super(
                filename,
                new TypeToken<ArrayList<Genre>>() {
                }.getType());
    }

    @Override
    public List<Genre> findByName(String name) {
        return findBy(genre -> genre.getName().equals(name));
    }
}
