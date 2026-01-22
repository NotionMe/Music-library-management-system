package ua.notion.musiclibrary.domain.repository;

import java.util.List;
import java.util.UUID;

import ua.notion.musiclibrary.domain.model.Playlist;

public interface PlaylistRepository extends Repository<Playlist, UUID> {
  /**
   * Знаходить плейлисти користувача.
   *
   * @param userId ID користувача
   * @return список плейлистів
   */
  List<Playlist> findByUserId(UUID userId);

  /**
   * Знаходить плейлисти за назвою (без врахування регістру).
   *
   * @param name частина назви
   * @return список знайдених плейлистів
   */
  List<Playlist> findByNameContainingIgnoreCase(String name);

  /**
   * Знаходить всі публічні плейлисти.
   *
   * @return список публічних плейлистів
   */
  List<Playlist> findByIsPrivateFalse();

  /**
   * Знаходить публічні плейлисти конкретного користувача.
   *
   * @param userId ID користувача
   * @return список публічних плейлистів користувача
   */
  List<Playlist> findByUserIdAndIsPrivateFalse(UUID userId);

  /**
   * Знаходить плейлисти, що містять вказаний трек.
   *
   * @param trackId ID треку
   * @return список плейлистів
   */
  List<Playlist> findByTrackIdsContaining(UUID trackId);

}
