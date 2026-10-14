package org.example.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.user.UserRoleEnum;


@Getter
@AllArgsConstructor

public class LoginResponse {

    private Long userId;
    private String name;
    private String email;
    private UserRoleEnum role;
    private String accessToken;
    private String refreshToken;

}