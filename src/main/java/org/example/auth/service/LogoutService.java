package org.example.auth.service;

import org.example.auth.repository.RefreshTokenRepository;
import org.example.common.exception.ErrorCodeEnum;
import org.example.common.exception.ForbiddenException;
import org.example.common.exception.NotFoundException;
import org.example.security.AuthenticatedService;
import org.example.user.UserEntity;
import org.example.user.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LogoutService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final AuthenticatedService authenticatedService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;


    public LogoutService(RefreshTokenRepository refreshTokenRepository,
                         AuthenticatedService authenticatedService,
                         PasswordEncoder passwordEncoder,
                         UserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.authenticatedService = authenticatedService;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }


    private UserEntity findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCodeEnum.USER_NOT_FOUND, "User not found"));
    }


    @Transactional
    public void logoutAccount(String password) {

        Long userId = authenticatedService.getAuthenticatedUserId();

        UserEntity user = findById(userId);


        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new ForbiddenException(ErrorCodeEnum.INVALID_PASSWORD, "Invalid password");
        }

        refreshTokenRepository.deleteAllByUserId(user.getId());

    }

}
