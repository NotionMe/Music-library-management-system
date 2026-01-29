package ua.notion.musiclibrary.service.impl;

import java.util.Collections;
import java.util.UUID;

import ua.notion.musiclibrary.domain.exception.EntityValidationException;
import ua.notion.musiclibrary.domain.impl.User;
import ua.notion.musiclibrary.dto.user.UserProfileDto;
import ua.notion.musiclibrary.dto.user.UserUpdateDto;
import ua.notion.musiclibrary.infrastructure.storage.impl.DataContext;
import ua.notion.musiclibrary.mapper.UserMapper;
import ua.notion.musiclibrary.service.contract.UserService;

public class UserServiceImpl implements UserService {

    private final DataContext dataContext;

    public UserServiceImpl(DataContext dataContext) {
        this.dataContext = dataContext;
    }

    @Override
    public UserProfileDto getProfile(UUID userId) {
        User user = dataContext.users().findById(userId)
                .orElseThrow(() -> new EntityValidationException(
                        Collections.singletonMap("username",
                                Collections.singletonList("Такого користувача не існує!"))));

        return UserMapper.toProfileDto(user);
    }

    @Override
    public boolean updateProfile(UUID userId, UserUpdateDto dto) {
        User user = dataContext.users().findById(userId)
                .orElseThrow(() -> new EntityValidationException(
                        Collections.singletonMap("username",
                                Collections.singletonList("Такого користувача не існує!"))));

        boolean wasModified = false;

        if (dto.newUsername() != null && !dto.newUsername().isBlank()) {
            user.setUsername(dto.newUsername());
            wasModified = true;
        }

        if (dto.newEmail() != null && !dto.newEmail().isBlank()) {
            user.setEmail(dto.newEmail());
            wasModified = true;
        }

        if (dto.newPassword() != null && !dto.newPassword().isBlank()) {
            if (dto.currentPassword() == null || dto.currentPassword().isBlank()) {
                throw new EntityValidationException(
                        Collections.singletonMap("currentPassword",
                                Collections.singletonList("Для зміни пароля потрібен поточний пароль")));
            }
            user.setPassword(dto.newPassword());
            wasModified = true;
        }

        if (wasModified) {
            dataContext.registerDirty(user);
            dataContext.commit();
        }

        return wasModified;
    }

    @Override
    public boolean followUser(UUID currentUserId, UUID targetUserId) {
        if (currentUserId.equals(targetUserId)) {
            throw new EntityValidationException(
                    Collections.singletonMap("targetUserId",
                            Collections.singletonList("Не можна підписатися на самого себе")));
        }

        User currentUser = dataContext.users().findById(currentUserId)
                .orElseThrow(() -> new EntityValidationException(
                        Collections.singletonMap("currentUserId",
                                Collections.singletonList("Поточного користувача не знайдено"))));

        if (!dataContext.users().findById(targetUserId).isPresent()) {
            throw new EntityValidationException(
                    Collections.singletonMap("targetUserId",
                            Collections.singletonList("Цільового користувача не знайдено")));
        }

        currentUser.followUser(targetUserId);
        dataContext.registerDirty(currentUser);
        dataContext.commit();
        return true;
    }
}
