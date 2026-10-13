package org.example.email.verify;


import org.example.common.exception.BadRequestException;
import org.example.common.exception.ErrorCodeEnum;
import org.example.common.exception.NotFoundException;
import org.example.user.UserEntity;
import org.example.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
public class VerifyEmailService {

    private final UserRepository userRepository;


    public VerifyEmailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    private UserEntity getUserByToken(String token) {
        return userRepository.findByEmailVerificationToken(token)
                .orElseThrow(() -> new NotFoundException(ErrorCodeEnum.USER_NOT_FOUND, "User not found"));
    }


    @Transactional
    public void verify(String token) {

        UserEntity user = getUserByToken(token);


        if (user.getEmailVerificationExpiresIn().isBefore(LocalDateTime.now())) {

            throw new BadRequestException(ErrorCodeEnum.TOKEN_EXPIRED, "Token expired");

        }


        user.setEmailVerificationToken(null);
        user.setEmailVerificationExpiresIn(null);
        user.setEmailVerified(true);
        user.setEmailSent(true);


    }

}
