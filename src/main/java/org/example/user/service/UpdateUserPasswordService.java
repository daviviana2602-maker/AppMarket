package org.example.user.service;

import org.example.common.exception.BadRequestException;
import org.example.common.exception.ErrorCodeEnum;
import org.example.common.exception.NotFoundException;
import org.example.common.messaging.event.UserChangedPasswordEvent;
import org.example.common.messaging.producer.UserChangedPasswordEventProducer;
import org.example.security.AuthenticatedService;
import org.example.user.UserEntity;
import org.example.user.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UpdateUserPasswordService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticatedService authenticatedService;
    private final UserChangedPasswordEventProducer userChangedPasswordEventProducer;


    public UpdateUserPasswordService(UserRepository userRepository,
                                     PasswordEncoder passwordEncoder,
                                     AuthenticatedService authenticatedService,
                                     UserChangedPasswordEventProducer userChangedPasswordEventProducer
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticatedService = authenticatedService;
        this.userChangedPasswordEventProducer = userChangedPasswordEventProducer;
    }


    private UserEntity findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCodeEnum.USER_NOT_FOUND,"User not found"));
    }


    @Transactional
    public void changeUserPassword(String oldPassword, String newPassword) {

        Long userId = authenticatedService.getAuthenticatedUserId();
        UserEntity user =  findUserById(userId);


        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BadRequestException(ErrorCodeEnum.WRONG_PASSWORD, "Current password is wrong");
        }


        String token = UUID.randomUUID().toString();

        user.setPasswordResetToken(token);
        user.setPasswordResetExpiresAt(LocalDateTime.now().plusHours(1));

        user.setNewPassword(passwordEncoder.encode(newPassword));

        userChangedPasswordEventProducer.publishUserChangedPassword(new UserChangedPasswordEvent(user.getEmail(), token));

    }

}
