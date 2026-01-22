package ua.notion.musiclibrary.infrastructure.storage.contract;

import ua.notion.musiclibrary.infrastructure.storage.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import ua.notion.musiclibrary.domain.impl.Favorite;

public interface FavoriteRepository extends Repository<Favorite> {

  /**
   * Знаходить улюблені треки користувача.
   *
   * @param userId ID користувача
   * @return список улюблених записів
   */
  List<Favorite> findByUserId(UUID userId);

  /**
   * Знаходить всіх користувачів, які додали трек в улюблені.
   *
   * @param trackId ID треку
   * @return список записів про улюблені треки
   */
  List<Favorite> findByTrackId(UUID trackId);

  /**
   * Знаходить записи, додані в конкретний час.
   *
   * @param addedAt дата і час додавання
   * @return список записів
   */
  List<Favorite> findByAddedAt(LocalDateTime addedAt);

}
