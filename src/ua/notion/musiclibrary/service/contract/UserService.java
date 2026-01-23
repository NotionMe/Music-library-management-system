package ua.notion.musiclibrary.service.contract;

import ua.notion.musiclibrary.domain.impl.User;
import ua.notion.musiclibrary.dto.user.UserProfileDto;
import ua.notion.musiclibrary.dto.user.UserUpdateDto;
import java.util.UUID;

public interface UserService {

    // 1. Метод getProfile(UUID userId) -> повертає UserProfileDto

    // 2. Метод updateProfile(UUID userId, UserUpdateDto dto)

    // 3. Метод followUser(UUID currentUserId, UUID targetUserId)
}
