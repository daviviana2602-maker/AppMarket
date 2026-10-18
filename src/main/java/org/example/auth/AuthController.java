package org.example.auth;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.auth.dto.request.CreateAccountRequest;
import org.example.auth.dto.request.LoginRequest;
import org.example.auth.dto.request.LogoutRequest;
import org.example.auth.dto.response.CreateAccountResponse;
import org.example.auth.dto.response.LoginResponse;
import org.example.auth.service.CreateAccountService;
import org.example.auth.service.LoginService;
import org.example.auth.service.LogoutService;
import org.example.email.verify.VerifyEmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor


public class AuthController {

    private final CreateAccountService createAccountService;
    private final VerifyEmailService verifyEmailService;
    private final LoginService loginService;
    private final LogoutService logoutService;


    @PostMapping("/create")
    public CreateAccountResponse createUser(
            @Valid @RequestBody CreateAccountRequest createAccountRequest
    )
    {
        return createAccountService.createAccount(

                createAccountRequest.getName(),
                createAccountRequest.getEmail(),
                createAccountRequest.getPassword()
        );
    }


    @PostMapping("/login")
    public LoginResponse log(
            @Valid @RequestBody LoginRequest loginRequest
    )
    {
        return loginService.systemLogin(
                loginRequest.getEmail(),
                loginRequest.getPassword()
        );
    }


    @PostMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Void> leaveUser(
            @Valid @RequestBody LogoutRequest logoutRequest
    )
    {
        logoutService.logoutAccount(logoutRequest.getPassword());

        return ResponseEntity.noContent().build();
    }


    @GetMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(
            @RequestParam String token
    ) {
        verifyEmailService.verify(token);

        return ResponseEntity.ok("Email verified!");
    }


}