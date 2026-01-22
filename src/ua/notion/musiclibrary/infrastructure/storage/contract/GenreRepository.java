package ua.notion.musiclibrary.infrastructure.storage.contract;

import java.util.List;

import ua.notion.musiclibrary.domain.impl.Genre;
import ua.notion.musiclibrary.infrastructure.storage.Repository;

public interface GenreRepository extends Repository<Genre> {

  /**
   * Знаходить жанри за назвою.
   *
   * @param name назва жанру
   * @return список знайдених жанрів
   */
  List<Genre> findByName(String name);

}
