package ua.notion.musiclibrary.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import ua.notion.musiclibrary.domain.model.UserCollection;

public interface UserCollectionRepository extends Repository<UserCollection, UUID> {
  /**
   * Знаходить колекцію користувача.
   *
   * @param userId ID користувача
   * @return Optional з колекцією або порожній Optional
   */
  Optional<UserCollection> findByUserId(UUID userId);

  /**
   * Знаходить колекції за назвою (без врахування регістру).
   *
   * @param name частина назви
   * @return список знайдених колекцій
   */
  List<UserCollection> findByNameContainingIgnoreCase(String name);

  /**
   * Знаходить колекції за описом (без врахування регістру).
   *
   * @param keyword ключове слово в описі
   * @return список знайдених колекцій
   */
  List<UserCollection> findByDescriptionContainingIgnoreCase(String keyword);

  /**
   * Знаходить колекції, що належать до вказаної групи.
   *
   * @param groupName назва групи
   * @return список знайдених колекцій
   */
  List<UserCollection> findByGroupsContaining(String groupName);

  /**
   * Знаходить колекції, що містять вказаний плейлист.
   *
   * @param playlistId ID плейлиста
   * @return список знайдених колекцій
   */
  List<UserCollection> findByPlaylistIdsContaining(UUID playlistId);

  /**
   * Перевіряє існування колекції у користувача.
   *
   * @param userId ID користувача
   * @return true, якщо колекція існує
   */
  boolean existsByUserId(UUID userId);
}
