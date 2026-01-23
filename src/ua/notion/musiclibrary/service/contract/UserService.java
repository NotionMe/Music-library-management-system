package ua.notion.musiclibrary.service.contract;

import ua.notion.musiclibrary.domain.impl.User;
import ua.notion.musiclibrary.dto.user.UserProfileDto;
import ua.notion.musiclibrary.dto.user.UserUpdateDto;
import java.util.UUID;

public interface UserService {

    UserProfileDto getProfile(UUID userId);

    boolean updateProfile(UUID userId, UserUpdateDto dto);

    boolean followUser(UUID currentUserId, UUID targetUserId);
}
