package ua.notion.musiclibrary.infrastructure.storage;

import ua.notion.musiclibrary.domain.impl.Entity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface Repository<T extends Entity> {

  T save(T entity);

  Optional<T> findById(UUID id);

  List<T> findAll();

  boolean deleteById(UUID id);

  boolean delete(T entity);

  boolean existsById(UUID id);

  long count();
}
