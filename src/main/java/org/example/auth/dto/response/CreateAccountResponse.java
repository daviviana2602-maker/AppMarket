package org.example.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.user.enums.UserRoleEnum;
import org.example.user.enums.UserStatusEnum;

@Getter
@AllArgsConstructor


public class CreateAccountResponse {
    private Long id;
    private String name;
    private String email;
    private UserRoleEnum role;
    private UserStatusEnum status;
}