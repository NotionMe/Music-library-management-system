package ua.notion.musiclibrary.service.contract;

import ua.notion.musiclibrary.domain.impl.User;
import ua.notion.musiclibrary.dto.auth.UserLoginDto;
import ua.notion.musiclibrary.dto.auth.UserRegistrationDto;

public interface AuthService {

    // 1. Метод register(UserRegistrationDto dto) -> повертає User

    // 2. Метод login(UserLoginDto dto) -> повертає User (або кидає помилку)
}