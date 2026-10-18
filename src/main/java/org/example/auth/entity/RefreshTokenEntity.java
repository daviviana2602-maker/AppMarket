package org.example.auth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.user.UserEntity;

import java.time.LocalDateTime;

@Entity
@Table(name = "refresh_tokens")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor


public class RefreshTokenEntity {

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token_hash", nullable = false, unique = true)
    private String tokenHash;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

}