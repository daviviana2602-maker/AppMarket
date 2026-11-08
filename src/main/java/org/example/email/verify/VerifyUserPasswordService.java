package org.example.email.verify;

import org.example.auth.repository.RefreshTokenRepository;
import org.example.common.exception.BadRequestException;
import org.example.common.exception.ErrorCodeEnum;
import org.example.common.exception.NotFoundException;
import org.example.user.UserEntity;
import org.example.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
public class VerifyUserPasswordService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;


    public VerifyUserPasswordService(UserRepository userRepository, RefreshTokenRepository refreshTokenRepository) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
    }


    private UserEntity getUserByToken(String token) {
        return userRepository.findByPasswordResetToken(token)
                .orElseThrow(() -> new NotFoundException(ErrorCodeEnum.USER_NOT_FOUND, "User not found"));
    }


    @Transactional
    public void checkPassword(String token) {

        UserEntity user = getUserByToken(token);


        if (user.getPasswordResetExpiresAt().isBefore(LocalDateTime.now())) {

            throw new BadRequestException(ErrorCodeEnum.TOKEN_EXPIRED, "Token expired");

        }


        user.setPassword(user.getNewPassword());

        user.setNewPassword(null);
        user.setPasswordResetToken(null);
        user.setPasswordResetExpiresAt(null);

        refreshTokenRepository.deleteAllByUserId(user.getId());

    }

}
