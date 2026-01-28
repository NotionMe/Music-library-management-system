package ua.notion.musiclibrary.service.impl;

import java.util.UUID;

import ua.notion.musiclibrary.domain.enums.Role;
import ua.notion.musiclibrary.domain.impl.User;
import ua.notion.musiclibrary.infrastructure.storage.impl.DataContext;
import ua.notion.musiclibrary.service.contract.TrackPublicationService;

public class TrackPublicationServiceImpl implements TrackPublicationService {

    private final DataContext dataContext;

    public TrackPublicationServiceImpl(DataContext dataContext) {
        this.dataContext = dataContext;
    }

    @Override
    public boolean canUserUploadTrack(User user) {
        if (user == null) {
            return false;
        }

        return user.getRole() == Role.ADMIN;
    }

    @Override
    public boolean canUserUploadForArtist(User user, UUID artistId) {
        if (!canUserUploadTrack(user)) {
            return false;
        }

        if (user.getRole() == Role.ADMIN) {
            return true;
        }

        return dataContext.artists()
                .findById(artistId)
                .map(artist -> artist.getID().equals(artistId))
                .orElse(false);
    }

    @Override
    public boolean canUserModerateTrack(User user) {
        return user != null && user.getRole() == Role.ADMIN;
    }
}
