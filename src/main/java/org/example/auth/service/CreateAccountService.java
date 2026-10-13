package org.example.auth.service;

import org.example.auth.dto.response.CreateAccountResponse;
import org.example.common.exception.BadRequestException;
import org.example.common.exception.ConflictException;
import org.example.common.exception.ErrorCodeEnum;
import org.example.common.messaging.event.UserRegisteredEvent;
import org.example.common.messaging.producer.UserRegisteredEventProducer;
import org.example.email.EmailService;
import org.example.user.UserEntity;
import org.example.user.UserRepository;
import org.example.user.UserRoleEnum;
import org.example.user.UserStatusEnum;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.example.common.normalize.StringNormalizer.normalizeEmail;
import static org.example.common.normalize.StringNormalizer.normalizeName;


@Service
public class CreateAccountService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final UserRegisteredEventProducer userRegisteredEventProducer;


    public CreateAccountService(UserRepository userRepository,
                                PasswordEncoder passwordEncoder,
                                EmailService emailService,
                                UserRegisteredEventProducer userRegisteredEventProducer) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.userRegisteredEventProducer = userRegisteredEventProducer;
    }


    private boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }


    @Transactional
    public CreateAccountResponse createAccount(String name, String email, String password) {


        name = normalizeName(name);
        email = normalizeEmail(email);


        if (name.length() < 3) {
            throw new BadRequestException(ErrorCodeEnum.INVALID_NAME, "Name must be at least 3 characters");
        }

        if (existsByEmail(email)) {
            throw new ConflictException(ErrorCodeEnum.EMAIL_ALREADY_EXISTS, "Email already exists");
        }


        UserEntity user = new UserEntity();

        user.setRole(UserRoleEnum.USER);
        user.setName(name);
        user.setEmail(email);
        user.setStatus(UserStatusEnum.ACTIVE);

        String passwordHash = passwordEncoder.encode(password);
        user.setPassword(passwordHash);


        userRepository.saveAndFlush(user);

        if (user.getId() == 1) {
            user.setRole(UserRoleEnum.ADMIN);
        }


        userRegisteredEventProducer.publishUserRegistered(new UserRegisteredEvent(user.getId(), email));


        return new CreateAccountResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getStatus()
        );

    }

}