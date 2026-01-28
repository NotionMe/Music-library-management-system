package ua.notion.musiclibrary.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

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
import ua.notion.musiclibrary.service.contract.VerificationCodeStore;

public class AuthServiceImpl implements AuthService {

    private final DataContext dataContext;
    private final EmailService emailService;
    private final VerificationCodeStore verificationCodeStore;
    private final PendingUserStore pendingUserStore;

    public AuthServiceImpl(DataContext dataContext, EmailService emailService) {
        this.dataContext = dataContext;
        this.emailService = emailService;
        this.verificationCodeStore = InMemoryVerificationCodeStore.getInstance();
        this.pendingUserStore = PendingUserStore.getInstance();
    }

    @Override
    public UUID initiateRegistration(UserRegistrationDto dto) {
        Map<String, List<String>> methodErrors = new HashMap<>();
        Optional<User> userExist = dataContext.users().findByEmail(dto.email());

        if (userExist.isPresent()) {
            addError(methodErrors, DomainFieldNames.User.EMAIL, "Користувач з таким емейлом вже існує");
            throw new EntityValidationException(methodErrors);
        }

        UUID tempUserId = UUID.randomUUID();

        String verificationCode = emailService.sendPasswordCode(dto.email(), dto.username());

        pendingUserStore.store(tempUserId, dto);
        verificationCodeStore.store(tempUserId, verificationCode);

        return tempUserId;
    }

    @Override
    public User completeRegistration(UUID tempUserId, String code) {
        Map<String, List<String>> methodErrors = new HashMap<>();

        boolean isCodeValid = verificationCodeStore.verify(tempUserId, code);

        if (!isCodeValid) {
            addError(methodErrors, "verificationCode", "Код невірний або прострочений (1 хвилина)");
            throw new EntityValidationException(methodErrors);
        }

        Optional<UserRegistrationDto> dtoOpt = pendingUserStore.retrieve(tempUserId);

        if (dtoOpt.isEmpty()) {
            addError(methodErrors, "registration", "Дані реєстрації не знайдено");
            throw new EntityValidationException(methodErrors);
        }

        UserRegistrationDto dto = dtoOpt.get();
        String hashedPassword = Password.hash(dto.password()).withBcrypt().getResult();
        User newUser = UserMapper.toDomain(dto, hashedPassword);

        dataContext.registerNew(newUser);
        dataContext.commit();

        verificationCodeStore.remove(tempUserId);
        pendingUserStore.remove(tempUserId);

        return newUser;
    }

    @Override
    public User login(UserLoginDto dto) {
        Map<String, List<String>> methodErrors = new HashMap<>();
        Optional<User> userOpt = dataContext.users().findByEmail(dto.email());

        if (userOpt.isEmpty()) {
            userOpt = dataContext.users().findByUsername(dto.email());
        }

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
