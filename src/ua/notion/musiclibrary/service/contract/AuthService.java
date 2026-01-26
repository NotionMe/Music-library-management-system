package ua.notion.musiclibrary.service.contract;

import java.util.UUID;

import ua.notion.musiclibrary.domain.impl.User;
import ua.notion.musiclibrary.dto.auth.UserLoginDto;
import ua.notion.musiclibrary.dto.auth.UserRegistrationDto;

public interface AuthService {

    UUID initiateRegistration(UserRegistrationDto dto);

    User completeRegistration(UUID tempUserId, String code);

    User login(UserLoginDto dto);

}