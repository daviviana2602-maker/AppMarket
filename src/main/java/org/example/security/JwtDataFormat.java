package org.example.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor

public class JwtDataFormat {
    private String userId;
    private String role;
    private String type;
}
