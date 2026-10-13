package org.example.user;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;


@Entity
@Table(name = "users")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder


public class UserEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column(unique = true)
    private String email;

    @Column
    private String password;

    @Column
    @Enumerated(EnumType.STRING)
    private UserRoleEnum role;

    @Column
    @Enumerated(EnumType.STRING)
    private UserStatusEnum status;

    @Column
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "email_verified", nullable = false)
    private Boolean emailVerified = false;

    @Column(name = "email_verification_token")
    private String emailVerificationToken;

    @Column(name = "email_verification_expires_in")
    private LocalDateTime emailVerificationExpiresIn;

    @Column(name = "email_sent", nullable = false)
    private Boolean emailSent = false;

}