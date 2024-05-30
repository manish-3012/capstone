package com.capstone.ems.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;

import javax.crypto.spec.SecretKeySpec;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;

class JwtUtilsTest {

    private JwtUtils jwtUtils;

    @Mock
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtUtils = new JwtUtils();
    }

    @Test
    void testGenerateToken() {
        when(userDetails.getUsername()).thenReturn("testuser");
        String token = jwtUtils.generateToken(userDetails);
        assertNotNull(token);
        assertEquals("testuser", jwtUtils.extractUsername(token));
    }

    @Test
    void testGenerateRefreshToken() {
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("refreshToken", "test_refresh_token");
        when(userDetails.getUsername()).thenReturn("testuser");
        String refreshToken = jwtUtils.generateRefreshToken(claims, userDetails);
        assertNotNull(refreshToken);
        assertEquals("testuser", jwtUtils.extractUsername(refreshToken));
    }

    @Test
    void testExtractUsername() {
        String secreteString = "843567893696976453275974432697R634976R738467TR678T34865R6834R8763T478378637664538745673865783678548735687R3";
        byte[] keyBytes = Base64.getDecoder().decode(secreteString.getBytes(StandardCharsets.UTF_8));
        SecretKeySpec key = new SecretKeySpec(keyBytes, "HmacSHA256");
        String token = Jwts.builder()
                .subject("testuser")
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(key)
                .compact();
        String username = jwtUtils.extractUsername(token);
        assertEquals("testuser", username);
    }

    @Test
    void testIsTokenValid() {
        when(userDetails.getUsername()).thenReturn("testuser");
        String token = jwtUtils.generateToken(userDetails);
        assertTrue(jwtUtils.isTokenValid(token, userDetails));
    }
    
    @Test
    void testIsTokenExpired() {
        String secretString = "843567893696976453275974432697R634976R738467TR678T34865R6834R8763T478378637664538745673865783678548735687R3";
        byte[] keyBytes = Base64.getDecoder().decode(secretString.getBytes(StandardCharsets.UTF_8));
        SecretKeySpec key = new SecretKeySpec(keyBytes, "HmacSHA256");

        String token = Jwts.builder()
                .subject("testuser")
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 24)) // Expired 24 hours ago
                .signWith(key)
                .compact();

        try {
            jwtUtils.isTokenExpired(token);
            fail("Expected ExpiredJwtException");
        } catch (ExpiredJwtException e) {
            // Expected behavior, token is expired
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
}