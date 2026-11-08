package org.example.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor


public class UpdateUserResponse {

    private String name;
    private String email;
    private String newEmail;

}