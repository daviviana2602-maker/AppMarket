package org.example.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor


public class UpdateUserRequest {

    @Size(max = 50, message = "Name cannot exceed 50 characters")
    private String name;

    @Email(message = "New email format is wrong")
    private String newEmail;


}