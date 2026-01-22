package ua.notion.musiclibrary.domain.repository;

import java.util.List;
import java.util.Optional;

/**
 * Базовий інтерфейс Repository.
 * Визначає стандартні CRUD-операції для будь-якої сутності.
 *
 * @param <T>  тип сутності
 * @param <ID> тип ідентифікатора
 */
public interface Repository<T, ID> {

  /**
   * Зберігає сутність.
   * Якщо сутність нова — створює, якщо існує — оновлює.
   *
   * @param entity сутність для збереження
   * @return збережена сутність
   */
  T save(T entity);

  /**
   * Знаходить сутність за ідентифікатором.
   *
   * @param id ідентифікатор
   * @return Optional з сутністю або порожній Optional
   */
  Optional<T> findById(ID id);

  /**
   * Повертає всі сутності.
   *
   * @return список всіх сутностей
   */
  List<T> findAll();

  /**
   * Видаляє сутність за ідентифікатором.
   *
   * @param id ідентифікатор
   * @return true, якщо сутність було видалено
   */
  boolean deleteById(ID id);

  /**
   * Видаляє конкретну сутність.
   *
   * @param entity сутність для видалення
   * @return true, якщо сутність було видалено
   */
  boolean delete(T entity);

  /**
   * Перевіряє існування сутності за ідентифікатором.
   *
   * @param id ідентифікатор
   * @return true, якщо сутність існує
   */
  boolean existsById(ID id);

  /**
   * Повертає кількість сутностей у сховищі.
   *
   * @return кількість сутностей
   */
  long count();
}
