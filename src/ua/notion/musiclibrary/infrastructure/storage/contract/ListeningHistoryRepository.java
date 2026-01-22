package ua.notion.musiclibrary.infrastructure.storage.contract;

import ua.notion.musiclibrary.infrastructure.storage.Repository;

import java.time.LocalDateTime;
import ua.notion.musiclibrary.domain.impl.ListeningHistory;
import java.util.List;
import java.util.UUID;

public interface ListeningHistoryRepository extends Repository<ListeningHistory> {

  /**
   * Знаходить історію прослуховування користувача.
   *
   * @param userId ID користувача
   * @return список записів історії
   */
  List<ListeningHistory> findByUserId(UUID userId);

  /**
   * Знаходить історію прослуховування конкретного треку.
   *
   * @param trackId ID треку
   * @return список записів історії
   */
  List<ListeningHistory> findByTrackId(UUID trackId);

  /**
   * Знаходить записи історії за часом прослуховування.
   *
   * @param dateTime час прослуховування
   * @return список записів історії
   */
  List<ListeningHistory> findByPlayedAt(LocalDateTime dateTime);

}
