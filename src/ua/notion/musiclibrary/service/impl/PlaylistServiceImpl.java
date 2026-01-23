package ua.notion.musiclibrary.service.impl;

import ua.notion.musiclibrary.domain.impl.Playlist;
import ua.notion.musiclibrary.dto.playlist.PlaylistCreateDto;
import ua.notion.musiclibrary.dto.playlist.PlaylistUpdateDto;
import ua.notion.musiclibrary.infrastructure.storage.contract.PlaylistRepository;
import ua.notion.musiclibrary.infrastructure.storage.contract.TrackRepository;
import ua.notion.musiclibrary.service.contract.PlaylistService;
import java.util.List;
import java.util.UUID;

public class PlaylistServiceImpl implements PlaylistService {

    // 1. Поля (PlaylistRepository, TrackRepository - якщо треба перевіряти треки)

    // 2. Конструктор

    // 3. Реалізація createPlaylist
    //    - Створити new Playlist(...)
    //    - Зберегти в репозиторій

    // 4. Реалізація addTrackToPlaylist
    //    - Знайти плейлист
    //    - Перевірити чи існує трек
    //    - playlist.addTrack(trackId)
    //    - Зберегти (оновити) плейлист

    // ... інші методи
}
