package org.example.user.service;

import org.example.common.exception.BadRequestException;
import org.example.common.exception.ErrorCodeEnum;
import org.example.common.exception.NotFoundException;
import org.example.common.messaging.event.UserChangedEmailEvent;
import org.example.common.messaging.producer.UserChangedEmailEventProducer;
import org.example.security.AuthenticatedService;
import org.example.user.UserEntity;
import org.example.user.UserRepository;
import org.example.user.dto.response.UpdateUserResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;


import static org.example.common.normalize.StringNormalizer.normalizeEmail;
import static org.example.common.normalize.StringNormalizer.normalizeName;


@Service
public class UpdateUserService {

    private final UserRepository userRepository;
    private final AuthenticatedService authenticatedService;
    private final UserChangedEmailEventProducer userChangedEmailEventProducer;



    public UpdateUserService(UserRepository userRepository,
                             AuthenticatedService authenticatedService,
                             UserChangedEmailEventProducer userChangedEmailEventProducer) {
        this.userRepository = userRepository;
        this.authenticatedService = authenticatedService;
        this.userChangedEmailEventProducer = userChangedEmailEventProducer;
    }


    private UserEntity findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCodeEnum.USER_NOT_FOUND, "User not found"));
    }



    @Transactional
    public UpdateUserResponse updateUser(String name, String newEmail) {


        if (name == null && newEmail == null) {
            throw new BadRequestException(ErrorCodeEnum.ONE_FIELD_REQUIRED, "at least one field is required");
        }


        if (name != null) {

            name = normalizeName(name);

            if (name.length() < 3) {
                throw new BadRequestException(ErrorCodeEnum.INVALID_NAME, "name must be at least 3 characters");
            }
        }


        Long userId = authenticatedService.getAuthenticatedUserId();
        UserEntity user = findUserById(userId);


        if (name != null) {
            user.setName(name);
        }


        if (newEmail != null) {

            newEmail = normalizeEmail(newEmail);

            if (newEmail.isBlank()) {
                throw new BadRequestException(ErrorCodeEnum.INVALID_EMAIL, "Email cannot be empty");
            }


            if (userRepository.existsByEmail(newEmail)) {
                throw new BadRequestException(ErrorCodeEnum.EMAIL_ALREADY_EXISTS, "Email already exists");
            }


            String token = UUID.randomUUID().toString();

            user.setEmailVerificationToken(token);
            user.setEmailVerificationExpiresIn(LocalDateTime.now().plusHours(1));

            user.setNewEmail(newEmail);

            userChangedEmailEventProducer.publishUserChangedEmail(new UserChangedEmailEvent(newEmail, token));

        }


        return new UpdateUserResponse(
                user.getName(),
                user.getEmail(),
                user.getNewEmail()
        );

    }

}