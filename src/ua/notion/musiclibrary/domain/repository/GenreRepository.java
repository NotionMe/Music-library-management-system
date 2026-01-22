package ua.notion.musiclibrary.domain.repository;

import java.util.UUID;
import java.util.List;

import ua.notion.musiclibrary.domain.model.Genre;

public interface GenreRepository extends Repository<Genre, UUID> {

  /**
   * Знаходить жанри за назвою.
   *
   * @param name назва жанру
   * @return список знайдених жанрів
   */
  List<Genre> findByName(String name);

}
