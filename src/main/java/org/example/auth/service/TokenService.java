package org.example.auth.service;

import org.example.auth.entity.RefreshTokenEntity;
import org.example.auth.repository.RefreshTokenRepository;
import org.example.common.exception.BadRequestException;
import org.example.common.exception.ErrorCodeEnum;
import org.example.common.exception.NotFoundException;
import org.example.security.JwtDataFormat;
import org.example.security.JwtService;
import org.example.security.TokenHashService;
import org.example.user.UserRoleEnum;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;


@Service
public class TokenService {

    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenHashService tokenHashService;


    public TokenService(JwtService jwtService,
                        RefreshTokenRepository refreshTokenRepository,
                        TokenHashService tokenHashService
    ) {
        this.jwtService = jwtService;
        this.refreshTokenRepository = refreshTokenRepository;
        this.tokenHashService = tokenHashService;
    }


    @Value("${jwt.refreshExpiration}")
    private long refreshExpirationMinutes;


    private RefreshTokenEntity findByTokenHash(String hashRefreshToken) {
        return refreshTokenRepository.findByTokenHash(hashRefreshToken)
                .orElseThrow(() -> new NotFoundException(ErrorCodeEnum.INVALID_CREDENTIALS, "Token doesn't exist"));
    }


    @Transactional
    public ResponseEntity refreshAccessToken(String refreshToken) {


        JwtDataFormat data = jwtService.extractClaims(refreshToken);

        if (!"REFRESH".equals(data.getType())) {
            throw new BadRequestException(ErrorCodeEnum.ONE_FIELD_REQUIRED, "Refresh token is required");
        }


        String hashRefreshToken = tokenHashService.sha256(refreshToken);

        RefreshTokenEntity refresh = findByTokenHash(hashRefreshToken);

        if (LocalDateTime.now().isAfter(refresh.getExpiresAt())) {
            throw new BadRequestException(ErrorCodeEnum.TOKEN_EXPIRED,"Token has expired");
        }


        Long userId = Long.valueOf(data.getUserId());
        UserRoleEnum role = UserRoleEnum.valueOf(data.getRole());


        refreshTokenRepository.delete(refresh);

        String newAccessToken = jwtService.generateAccessToken(userId, role);
        String newRefreshToken = jwtService.generateRefreshToken(userId, role);


        String newHash = tokenHashService.sha256(newRefreshToken);

        RefreshTokenEntity newRefresh = new RefreshTokenEntity();
        refresh.setUser(refresh.getUser());
        newRefresh.setTokenHash(newHash);
        newRefresh.setExpiresAt(LocalDateTime.now().plusMinutes(refreshExpirationMinutes));

        refreshTokenRepository.save(newRefresh);


        return ResponseEntity.ok(
                Map.of(
                        "accessToken", newAccessToken,
                        "refreshToken", newRefreshToken
                )
        );

    }

}