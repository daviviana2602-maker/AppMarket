package org.example.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.example.user.UserRoleEnum;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.accessExpiration}")
    private long accessExpirationMinutes;

    @Value("${jwt.refreshExpiration}")
    private long refreshExpirationMinutes;


    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));  // Transform the secret in a new SecretKey (JWT use it to sign and validate token)
    }


    public JwtDataFormat extractClaims(String token) {

        Claims claims = Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();       // extract the body of token


        return new JwtDataFormat(
                claims.getSubject(),
                claims.get("role", String.class),
                claims.get("type", String.class)
        );

    }


    public String generateAccessToken(Long userId, UserRoleEnum role) {

        Date now = new Date();
        Date expiration = new Date(now.getTime() + accessExpirationMinutes * 60 * 1000);

        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("role", role)
                .claim("type", "ACCESS")
                .issuedAt(now)
                .expiration(expiration)
                .signWith(getKey())
                .compact();
    }


    public String generateRefreshToken(Long userId, UserRoleEnum role) {

        Date now = new Date();
        Date expiration = new Date(now.getTime() + refreshExpirationMinutes * 60 * 1000);

        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("role", role)
                .claim("type", "REFRESH")
                .issuedAt(now)
                .expiration(expiration)
                .signWith(getKey())
                .compact();
    }

}
