package org.example.auth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.auth.dto.request.CreateAccountRequest;
import org.example.auth.dto.request.LoginRequest;
import org.example.auth.dto.response.CreateAccountResponse;
import org.example.auth.dto.response.LoginResponse;
import org.example.auth.service.CreateAccountService;
import org.example.auth.service.LoginService;
import org.example.email.verify.VerifyEmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor


public class AuthController {

    private final CreateAccountService createAccountService;
    private final VerifyEmailService verifyEmailService;
    private final LoginService loginService;


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


    @GetMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(
            @RequestParam String token
    ) {
        verifyEmailService.verify(token);

        return ResponseEntity.ok("Email verified!");
    }


}