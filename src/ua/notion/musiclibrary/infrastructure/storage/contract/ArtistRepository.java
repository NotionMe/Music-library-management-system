package ua.notion.musiclibrary.infrastructure.storage.contract;

import ua.notion.musiclibrary.infrastructure.storage.Repository;

import java.util.UUID;
import java.util.List;
import java.util.Optional;

import ua.notion.musiclibrary.domain.impl.Artist;

public interface ArtistRepository extends Repository<Artist> {
  /**
   * Знаходить артиста, пов'язаного з користувачем.
   *
   * @param userId ID користувача
   * @return Optional з артистом
   */
  Optional<Artist> findByUserId(UUID userId);

  /**
   * Знаходить артистів за сценічним ім'ям.
   *
   * @param stageName сценічне ім'я
   * @return список знайдених артистів
   */
  List<Artist> findByStageName(String stageName);

  /**
   * Знаходить артистів за описом біографії.
   *
   * @param bio текст біографії
   * @return список знайдених артистів
   */
  List<Artist> findByBio(String bio);

}
