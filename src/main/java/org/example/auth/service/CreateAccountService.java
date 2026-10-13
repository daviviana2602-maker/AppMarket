package org.example.auth.service;

import org.example.auth.dto.response.CreateAccountResponse;
import org.example.common.Exception.BadRequestException;
import org.example.common.Exception.ConflictException;
import org.example.common.Exception.ErrorCodeEnum;
import org.example.email.EmailService;
import org.example.user.UserEntity;
import org.example.user.UserRepository;
import org.example.user.UserRoleEnum;
import org.example.user.UserStatusEnum;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.example.common.normalize.StringNormalizer.normalizeEmail;
import static org.example.common.normalize.StringNormalizer.normalizeName;

@Service
public class CreateAccountService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;


    public CreateAccountService(UserRepository userRepository,
                                PasswordEncoder passwordEncoder,
                                EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
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


        String token = UUID.randomUUID().toString();

        user.setEmailVerified(false);
        user.setEmailVerificationToken(token);
        user.setEmailVerificationExpiresIn(LocalDateTime.now().plusMinutes(10));

        emailService.sendVerificationEmail(email, token);


        userRepository.saveAndFlush(user);

        if (user.getId() == 1) {
            user.setRole(UserRoleEnum.ADMIN);
        }


        return new CreateAccountResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getStatus()
        );

    }

}