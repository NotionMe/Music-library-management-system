package ua.notion.musiclibrary.service.impl;

import ua.notion.musiclibrary.dto.track.TrackDto;
import ua.notion.musiclibrary.dto.track.TrackSearchDto;
import ua.notion.musiclibrary.infrastructure.storage.contract.TrackRepository;
import ua.notion.musiclibrary.service.contract.TrackService;
import java.util.List;
import java.util.UUID;

public class TrackServiceImpl implements TrackService {

    // 1. Поля (TrackRepository)

    // 2. Конструктор

    // 3. Реалізація searchTracks
    //    - Використати репозиторій для пошуку (findAll або специфічний пошук)
    //    - Фільтрувати результати згідно DTO (якщо репозиторій не вміє)
    //    - Перетворити Entity -> TrackDto
    //    - Повернути список
}
