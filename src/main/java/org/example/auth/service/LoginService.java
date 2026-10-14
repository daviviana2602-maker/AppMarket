package org.example.auth.service;

import org.example.auth.dto.response.LoginResponse;
import org.example.common.exception.BadRequestException;
import org.example.common.exception.ErrorCodeEnum;
import org.example.common.exception.ForbiddenException;
import org.example.common.exception.NotFoundException;
import org.example.security.JwtService;
import org.example.user.UserEntity;
import org.example.user.UserRepository;
import org.example.user.UserStatusEnum;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static org.example.common.normalize.StringNormalizer.normalizeEmail;


@Service
public class LoginService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;


    public LoginService(UserRepository userRepository,
                        PasswordEncoder passwordEncoder,
                        JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }


    private UserEntity findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(ErrorCodeEnum.USER_NOT_FOUND,"User not found"));
    }


    public LoginResponse systemLogin(String email, String password){

        email = normalizeEmail(email);

        UserEntity user = findUserByEmail(email);


        if (user.getEmailVerified() == false){
            throw new ForbiddenException(ErrorCodeEnum.USER_NOT_VERIFIED, "User is not verified");
        }

        if (user.getStatus().equals(UserStatusEnum.DISABLED)) {
            throw new ForbiddenException(ErrorCodeEnum.USER_DISABLED,"User is disabled");
        }

        if (user.getStatus().equals(UserStatusEnum.DELETED)) {
            throw new ForbiddenException(ErrorCodeEnum.USER_DELETED,"User is deleted");
        }


        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadRequestException(ErrorCodeEnum.INVALID_CREDENTIALS,"wrong email or password");
        }


        String accessToken = jwtService.generateAccessToken(user.getId(), user.getRole());
        String refreshToken = jwtService.generateRefreshToken(user.getId(), user.getRole());


        return new LoginResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                accessToken,
                refreshToken
        );


    }

}