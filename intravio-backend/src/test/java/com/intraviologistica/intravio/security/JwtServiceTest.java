package com.intraviologistica.intravio.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.lang.reflect.Field;
import java.security.Key;
import java.util.Base64;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class JwtServiceTest {
    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final long EXPIRATION_TIME = 3600000L;

    @Mock
    private UserDetails userDetails;

    private JwtService jwtService;

    @BeforeEach
    void setUp() throws IllegalAccessException, NoSuchFieldException {
        MockitoAnnotations.openMocks(this);
        jwtService = new JwtService();
        Field secretKeyField = JwtService.class.getDeclaredField("secret_key");
        secretKeyField.setAccessible(true);
        secretKeyField.set(jwtService, Base64.getEncoder().encodeToString(SECRET_KEY.getEncoded()));

        Field expirationField = JwtService.class.getDeclaredField("expiration");
        expirationField.setAccessible(true);
        expirationField.set(jwtService, EXPIRATION_TIME);
    }

    @Test
    void extractUsername_ValidToken_RetornaUsername() {
        String username = "antonio.sousa";
        String token = generateTokenWithSubject(username);

        String extractedUsername = jwtService.extractUsername(token);

        assertEquals(username, extractedUsername);
    }

    @Test
    void extractClaim_ValidToken_ReturnaValorDasClaims() {
        String claimName = "customClaim";
        String claimValue = "customValue";
        String token = generateTokenWithClaim(claimName, claimValue);

        String extractedClaimValue = jwtService.extractClaim(token, claims -> claims.get(claimName, String.class));

        assertEquals(claimValue, extractedClaimValue);
    }

    @Test
    void generateToken_RetornaTokenValido() {
        String username = "antonio.sousa";
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        when(userDetails.getUsername()).thenReturn(username);

        String token = jwtService.generateToken(userDetails);

        assertNotNull(token);
        assertTrue(isTokenValid(token, userDetails));
    }

    @Test
    void isTokenValid_ValidTokenAndMatchingUserDetails_RetornaVerdade() {
        String username = "antonio.sousa";
        UserDetails matchingUserDetails = new User(username, "password", Collections.emptyList());
        String token = generateTokenWithSubject(username);

        boolean isValid = jwtService.isTokenValid(token, matchingUserDetails);

        assertTrue(isValid);
    }

    @Test
    void isTokenValid_ValidTokenButDifferentUserDetails_ReturnaFalso() {
        String username = "john.doe";
        UserDetails differentUserDetails = new User("antonio.sousa", "password", Collections.emptyList());
        String token = generateTokenWithSubject(username);

        boolean isValid = jwtService.isTokenValid(token, differentUserDetails);

        assertFalse(isValid);
    }

    private String generateTokenWithSubject(String subject) {
        return Jwts.builder()
                .setSubject(subject)
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    private String generateTokenWithClaim(String claimName, String claimValue) {
        return Jwts.builder()
                .claim(claimName, claimValue)
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    private boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = jwtService.extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !jwtService.isTokenExpired(token);
    }
}
