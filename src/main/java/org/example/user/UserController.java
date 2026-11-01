package org.example.user;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.user.dto.request.UpdateUserRequest;
import org.example.user.dto.response.UpdateUserResponse;
import org.example.user.service.UpdateUserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/user")
@RequiredArgsConstructor


public class UserController {

    private final UpdateUserService updateUserService;


    @PatchMapping("/")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "bearerAuth")
    public UpdateUserResponse updateUser(
            @Valid @RequestBody UpdateUserRequest updateUserRequest
    )
    {
        return updateUserService.updateUser(
                updateUserRequest.getName(),
                updateUserRequest.getNewEmail()
        );
    }



}