package ua.notion.musiclibrary.infrastructure.storage.contract;

import ua.notion.musiclibrary.infrastructure.storage.Repository;

import java.util.List;
import java.util.UUID;

import java.time.Duration;

import ua.notion.musiclibrary.domain.impl.Track;

public interface TrackRepository extends Repository<Track> {

  /**
   * Знаходить треки за ID альбому.
   *
   * @param albumId ID альбому
   * @return список треків в альбомі
   */
  List<Track> findByAlbumId(UUID albumId);

  /**
   * Знаходить треки за ID жанру.
   *
   * @param genreId ID жанру
   * @return список треків цього жанру
   */
  List<Track> findByGenreId(UUID genreId);

  /**
   * Знаходить треки, в яких бере участь артист.
   *
   * @param artistId ID артиста
   * @return список треків
   */
  List<Track> findByArtistId(UUID artistId);

  /**
   * Знаходить треки за назвою (без врахування регістру).
   *
   * @param title частина назви треку
   * @return список знайдених треків
   */
  List<Track> findByTitleContainingIgnoreCase(String title);

  /**
   * Знаходить треки, тривалість яких більша за вказану.
   *
   * @param duration мінімальна тривалість
   * @return список треків
   */
  List<Track> findByDurationGreaterThan(Duration duration);

  /**
   * Знаходить треки в діапазоні тривалості.
   *
   * @param start початкова тривалість
   * @param end   кінцева тривалість
   * @return список треків
   */
  List<Track> findByDurationBetween(Duration start, Duration end);

  /**
   * Знаходить треки за ID альбому та ID жанру.
   *
   * @param albumId ID альбому
   * @param genreId ID жанру
   * @return список треків
   */
  List<Track> findByAlbumIdAndGenreId(UUID albumId, UUID genreId);

}
