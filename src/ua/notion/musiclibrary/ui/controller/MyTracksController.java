package ua.notion.musiclibrary.ui.controller;

import java.util.Arrays;
import java.util.List;

import ua.notion.musiclibrary.domain.enums.PublicationStatus;
import ua.notion.musiclibrary.dto.track.TrackDto;
import ua.notion.musiclibrary.service.contract.ModerationService;
import ua.notion.musiclibrary.service.impl.ModerationServiceImpl;
import ua.notion.musiclibrary.infrastructure.storage.impl.DataContext;
import ua.notion.musiclibrary.ui.TuiSession;
import ua.notion.musiclibrary.ui.util.DisplayFormatter;
import ua.notion.musiclibrary.ui.util.MenuBuilder;

public class MyTracksController {

    private final TuiSession session;
    private final MenuBuilder menuBuilder;
    private final ModerationService moderationService;

    public MyTracksController(TuiSession session) {
        this.session = session;
        this.menuBuilder = new MenuBuilder(session.getTerminal());
        this.moderationService = new ModerationServiceImpl(DataContext.getInstance());
    }

    public void showMyTracks() {
        DisplayFormatter.clearScreen();
        System.out.println(DisplayFormatter.header("Мої Треки"));

        List<TrackDto> myTracks = moderationService.getUserTracks(session.getCurrentUser().getID());

        if (myTracks.isEmpty()) {
            System.out.println(DisplayFormatter.warning("У вас ще немає завантажених треків"));
            DisplayFormatter.waitForKeyPress();
            return;
        }

        System.out.println(String.format("\nВсього треків: %d\n", myTracks.size()));

        for (int i = 0; i < myTracks.size(); i++) {
            TrackDto track = myTracks.get(i);
            String statusBadge = getStatusBadge(track);
            System.out.println(String.format("%d. %s %s - %s",
                    (i + 1),
                    statusBadge,
                    track.title(),
                    track.artistName()));
        }

        String choice = menuBuilder.createInputPrompt("\nВведіть номер треку для деталей (або Enter для повернення):",
                "");

        if (!choice.isBlank()) {
            try {
                int index = Integer.parseInt(choice) - 1;
                if (index >= 0 && index < myTracks.size()) {
                    showTrackDetails(myTracks.get(index));
                }
            } catch (NumberFormatException e) {
            }
        }
    }

    private void showTrackDetails(TrackDto track) {
        DisplayFormatter.clearScreen();
        System.out.println(DisplayFormatter.header("Деталі Треку"));

        System.out.println(String.format("Назва: %s", track.title()));
        System.out.println(String.format("Артист: %s", track.artistName()));
        System.out.println(String.format("Альбом: %s", track.albumName()));
        System.out.println(String.format("Тривалість: %s", track.duration()));
        System.out.println(String.format("Статус: %s", getStatusBadge(track)));

        DisplayFormatter.waitForKeyPress();
    }

    private String getStatusBadge(TrackDto track) {
        return "🔵 Unknown";
    }
}
