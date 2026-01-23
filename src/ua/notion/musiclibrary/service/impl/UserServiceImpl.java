package ua.notion.musiclibrary.service.impl;

import ua.notion.musiclibrary.dto.user.UserProfileDto;
import ua.notion.musiclibrary.dto.user.UserUpdateDto;
import ua.notion.musiclibrary.infrastructure.storage.contract.UserRepository;
import ua.notion.musiclibrary.service.contract.UserService;
import java.util.UUID;

public class UserServiceImpl implements UserService {

    // 1. Поля (UserRepository)

    // 2. Конструктор

    // 3. Реалізація getProfile
    //    - Знайти user by ID
    //    - Сконвертувати в UserProfileDto
    //    - Повернути DTO

    // 4. Реалізація updateProfile
    //    - Отримати user
    //    - Змінити поля (якщо валідація пройшла)
    //    - repository.update(user)

    // 5. Реалізація followUser
    //    - Отримати поточного юзера і цільового
    //    - currentUser.followUser(targetId)
    //    - Зберегти зміни
}
