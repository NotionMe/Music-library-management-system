package ua.notion.musiclibrary.service.contract;

import ua.notion.musiclibrary.domain.impl.User;
import ua.notion.musiclibrary.dto.auth.UserLoginDto;
import ua.notion.musiclibrary.dto.auth.UserRegistrationDto;

public interface AuthService {
    
    User register(UserRegistrationDto dto);

    User login(UserLoginDto dto);

}