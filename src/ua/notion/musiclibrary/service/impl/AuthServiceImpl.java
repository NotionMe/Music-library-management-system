package ua.notion.musiclibrary.service.impl;

import ua.notion.musiclibrary.domain.impl.User;
import ua.notion.musiclibrary.dto.auth.UserLoginDto;
import ua.notion.musiclibrary.dto.auth.UserRegistrationDto;
import ua.notion.musiclibrary.infrastructure.storage.contract.UserRepository;
import ua.notion.musiclibrary.service.contract.AuthService;
import ua.notion.musiclibrary.service.contract.EmailService;

public class AuthServiceImpl implements AuthService {

    // 1. Поля (UserRepository, EmailService)

    // 2. Конструктор (Dependency Injection)

    // 3. Реалізація register(UserRegistrationDto dto)
    //    - Перевірити чи user існує (через repository)
    //    - Створити new User(...) з даних DTO
    //    - repository.save(user)
    //    - emailService.sendWelcomeEmail(...)
    //    - Повернути user

    // 4. Реалізація login(UserLoginDto dto)
    //    - Знайти user за email
    //    - Перевірити пароль
    //    - Повернути user або кинути помилку
}
