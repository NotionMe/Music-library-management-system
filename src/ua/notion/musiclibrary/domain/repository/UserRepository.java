package ua.notion.musiclibrary.domain.repository;

import java.util.UUID;
import java.util.List;
import java.util.Optional;

import ua.notion.musiclibrary.domain.enums.Role;
import ua.notion.musiclibrary.domain.model.User;

public interface UserRepository extends Repository<User, UUID> {
  /**
   * Знаходить користувача за логіном.
   *
   * @param username логін користувача
   * @return Optional з користувачем або порожній Optional
   */
  Optional<User> findByUsername(String username);

  /**
   * Знаходить користувача за email.
   *
   * @param email електронна пошта
   * @return Optional з користувачем або порожній Optional
   */
  Optional<User> findByEmail(String email);

  /**
   * Знаходить користувачів за роллю.
   *
   * @param role роль користувача
   * @return список користувачів з вказаною роллю
   */
  List<User> findByRole(Role role);

  /**
   * Перевіряє існування користувача за логіном.
   *
   * @param username логін користувача
   * @return true, якщо користувач існує
   */
  boolean existsByUsername(String username);

  /**
   * Перевіряє існування користувача за email.
   *
   * @param email електронна пошта
   * @return true, якщо користувач існує
   */
  boolean existsByEmail(String email);

  /**
   * Знаходить користувачів, чий логін містить вказаний текст (без врахування
   * регістру).
   *
   * @param username частина логіна
   * @return список знайдених користувачів
   */
  List<User> findByUsernameContainingIgnoreCase(String username);
}
