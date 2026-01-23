package ua.notion.musiclibrary.dto.user;

public record UserUpdateDto(
        String newUsername,
        String newEmail,
        String newPassword,
        String currentPassword) {
}
