package ua.notion.musiclibrary.service.impl;

import ua.notion.musiclibrary.infrastructure.storage.contract.PlaylistRepository;
import ua.notion.musiclibrary.infrastructure.storage.contract.TrackRepository;
import ua.notion.musiclibrary.service.contract.ExportService;
import java.util.UUID;

public class ExportServiceImpl implements ExportService {

    // 1. Поля (PlaylistRepository, TrackRepository)

    // 2. Конструктор

    // 3. Реалізація exportPlaylistToCsv
    //    - Знайти плейлист
    //    - Отримати список треків
    //    - Відкрити FileWriter
    //    - Записати заголовок "Title,Duration,Artist"
    //    - Пройтись циклом і записати дані
    //    - Закрити файл
}
