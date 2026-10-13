package org.example.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class CreateAccountRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Email format is wrong")
    String email;

    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
    String name;

    @NotBlank(message = "Password is required")
    @Size(min = 3, max = 50, message = "Password must be between 3 and 50 characters")
    String password;
}