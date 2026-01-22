package ua.notion.musiclibrary.domain.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import ua.notion.musiclibrary.domain.model.Album;

public interface AlbumRepository extends Repository<Album, UUID> {

  /**
   * Знаходить альбом за назвою (часткове співпадіння).
   *
   * @param title частина назви
   * @return список знайдених альбомів
   */

  List<Album> findByTitleContaining(String title);

  /**
   * Знаходить альбоми артиста.
   *
   * @param artistId ID артиста
   * @return список альбомів артиста
   */

  List<Album> findByArtistId(UUID artistId);

  /**
   * Знаходить всі доступні альбоми.
   *
   * @return список доступних альбомів
   */

  /**
   * Знаходить альбом, видані в певному році.
   *
   * @param date дата видання
   * @return список альбомів
   */

  List<Album> findByReleaseDate(LocalDate date);

  /**
   * Знаходить альбоми, видані в діапазоні років.
   *
   * @param startDate початкова дата
   * @param endDate   кінцева дата
   * @return список альбомів
   */

  List<Album> findByReleaseDateBetween(LocalDate startDate, LocalDate endDate);

}
