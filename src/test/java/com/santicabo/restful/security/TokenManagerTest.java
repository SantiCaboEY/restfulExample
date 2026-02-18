package com.santicabo.restful.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TokenManagerTest {

    private TokenManager tokenManager;
    private String secretKey;
    private Long validity;
    private String testUsername;

    @BeforeEach
    void setUp() {
        secretKey = Base64.getEncoder().encodeToString("very-long-secret-key-for-testing-purposes-256-bits-23432-paaaaaddddinnng"
                .getBytes(StandardCharsets.UTF_8));
        validity = 3600000L;
        testUsername = "juan@example.com";

        tokenManager = new TokenManager(secretKey, validity);
        tokenManager.init();
    }


    @Test
    void testGenerateTokenFormat() {
        String token = tokenManager.generateToken(testUsername);

        String[] parts = token.split("\\.");
        assertEquals(3, parts.length, "JWT debe tener 3 partes separadas por puntos");
    }

    @Test
    void testTokenContainsCorrectSubject() {
        String token = tokenManager.generateToken(testUsername);

        Claims claims = extractClaims(token);
        assertEquals(testUsername, claims.getSubject());
    }


    @Test
    void testTokenContainsExpiration() {
        String token = tokenManager.generateToken(testUsername);

        Claims claims = extractClaims(token);
        Date expiration = claims.getExpiration();

        assertNotNull(expiration);
        assertTrue(expiration.getTime() > System.currentTimeMillis());
    }

    @Test
    void testTokenIsProperlySignedWithSecret() {
        String token = tokenManager.generateToken(testUsername);

        Claims claims = extractClaims(token);
        assertEquals(testUsername, claims.getSubject());
    }


    @Test
    void testValidityInMilliseconds() {
        var shortValidity = 1000L;
        TokenManager shortLivedTokenManager = new TokenManager(secretKey, shortValidity);
        shortLivedTokenManager.init();

        String token = shortLivedTokenManager.generateToken(testUsername);
        Claims claims = extractClaims(token);

        long expirationTime = claims.getExpiration().getTime();
        long currentTime = System.currentTimeMillis();
        long timeDifference = expirationTime - currentTime;

        assertTrue(timeDifference <= shortValidity && timeDifference > 0,
                "El token debe expirar en el tiempo especificado");
    }


    @Test
    void testTokenCanBeParsedBack() {
        String token = tokenManager.generateToken(testUsername);

        Claims claims = extractClaims(token);

        assertNotNull(claims);
        assertEquals(testUsername, claims.getSubject());
        assertNotNull(claims.getIssuedAt());
        assertNotNull(claims.getExpiration());
    }

    private Claims extractClaims(String token) {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        SecretKey key = Keys.hmacShaKeyFor(keyBytes);

        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
