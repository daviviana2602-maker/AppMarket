package org.example.auth.repository;

import org.example.auth.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;


public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {

    Optional<RefreshTokenEntity> findByTokenHash(String tokenHash);

    void deleteAllByUserId(Long userId);

    void deleteByExpiresAtBefore(LocalDateTime expiresAt);

}
