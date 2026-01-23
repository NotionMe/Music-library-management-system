package ua.notion.musiclibrary.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.password4j.Password;

import ua.notion.musiclibrary.domain.exception.EntityValidationException;
import ua.notion.musiclibrary.domain.impl.User;
import ua.notion.musiclibrary.domain.util.DomainFieldNames;
import ua.notion.musiclibrary.dto.auth.UserLoginDto;
import ua.notion.musiclibrary.dto.auth.UserRegistrationDto;
import ua.notion.musiclibrary.infrastructure.storage.impl.DataContext;
import ua.notion.musiclibrary.mapper.UserMapper;
import ua.notion.musiclibrary.service.contract.AuthService;
import ua.notion.musiclibrary.service.contract.EmailService;

public class AuthServiceImpl implements AuthService {

    private final DataContext dataContext;
    private final EmailService emailService;

    public AuthServiceImpl(DataContext dataContext, EmailService emailService) {
        this.dataContext = dataContext;
        this.emailService = emailService;
    }

    @Override
    public User register(UserRegistrationDto dto) {
        Map<String, List<String>> methodErrors = new HashMap<>();
        Optional<User> userExist = dataContext.users().findByEmail(dto.email());

        if (userExist.isPresent()) {
            addError(methodErrors, DomainFieldNames.User.EMAIL, "Користувач з таким емейлом вже існує");
            throw new EntityValidationException(methodErrors);
        }

        String hashedPassword = Password.hash(dto.password()).withBcrypt().getResult();

        User newUser = UserMapper.toDomain(dto, hashedPassword);

        String verificationCode = emailService.sendPasswordCode(newUser.getEmail(), newUser.getUsername());
        newUser.setVerificationCode(verificationCode);

        dataContext.registerNew(newUser);
        dataContext.commit();

        return newUser;
    }

    @Override
    public User login(UserLoginDto dto) {
        Map<String, List<String>> methodErrors = new HashMap<>();
        Optional<User> userOpt = dataContext.users().findByEmail(dto.email());

        if (userOpt.isEmpty()) {
            addError(methodErrors, DomainFieldNames.User.EMAIL, "Користувача не знайдено");
            throw new EntityValidationException(methodErrors);
        }
        User user = userOpt.get();

        boolean isPasswordMatch = Password.check(dto.password(), user.getPassword())
                .withBcrypt();

        if (!isPasswordMatch) {
            addError(methodErrors, DomainFieldNames.User.PASSWORD, "Пароль не збігається!");
            throw new EntityValidationException(methodErrors);
        }
        return user;
    }

    private void addError(Map<String, List<String>> errors, String field, String message) {
        errors.computeIfAbsent(field, k -> new ArrayList<>()).add(message);
    }

}
