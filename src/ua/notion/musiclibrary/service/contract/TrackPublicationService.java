package ua.notion.musiclibrary.service.contract;

import java.util.UUID;

import ua.notion.musiclibrary.domain.enums.Role;
import ua.notion.musiclibrary.domain.impl.User;

public interface TrackPublicationService {

    boolean canUserUploadTrack(User user);

    boolean canUserUploadForArtist(User user, UUID artistId);

    boolean canUserModerateTrack(User user);
}
