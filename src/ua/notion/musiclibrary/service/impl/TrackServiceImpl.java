package ua.notion.musiclibrary.service.impl;

import ua.notion.musiclibrary.dto.track.TrackDto;
import ua.notion.musiclibrary.dto.track.TrackSearchDto;
import ua.notion.musiclibrary.infrastructure.storage.contract.TrackRepository;
import ua.notion.musiclibrary.service.contract.TrackService;
import java.util.List;
import java.util.UUID;

public class TrackServiceImpl implements TrackService {

    @Override
    public List<TrackDto> searchTracks(TrackSearchDto searchDto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'searchTracks'");
    }

    @Override
    public TrackDto getTrackDetails(UUID trackId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTrackDetails'");
    }

    // 1. Поля (TrackRepository)

    // 2. Конструктор

    // 3. Реалізація searchTracks
    //    - Використати репозиторій для пошуку (findAll або специфічний пошук)
    //    - Фільтрувати результати згідно DTO (якщо репозиторій не вміє)
    //    - Перетворити Entity -> TrackDto
    //    - Повернути список
}
