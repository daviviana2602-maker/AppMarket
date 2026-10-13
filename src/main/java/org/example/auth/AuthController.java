package org.example.auth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.auth.dto.request.CreateAccountRequest;
import org.example.auth.dto.response.CreateAccountResponse;
import org.example.auth.service.CreateAccountService;
import org.example.email.verify.VerifyEmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor


public class AuthController {

    private final CreateAccountService createAccountService;
    private final VerifyEmailService verifyEmailService;


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


    @GetMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(
            @RequestParam String token
    ) {
        verifyEmailService.verify(token);

        return ResponseEntity.ok("Email verified!");
    }


}