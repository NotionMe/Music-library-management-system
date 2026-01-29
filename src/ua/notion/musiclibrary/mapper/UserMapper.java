package ua.notion.musiclibrary.mapper;

import ua.notion.musiclibrary.domain.impl.User;
import ua.notion.musiclibrary.dto.auth.UserRegistrationDto;
import ua.notion.musiclibrary.dto.user.UserProfileDto;

public final class UserMapper {

    private UserMapper() {
    }

    public static User toDomain(UserRegistrationDto dto, String hashedPassword) {
        return new User(
                dto.username(),
                dto.email(),
                hashedPassword,
                dto.role());
    }

    public static UserProfileDto toProfileDto(User user) {
        return new UserProfileDto(
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.getRole());
    }
}
