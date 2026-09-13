package com.livrotech.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

class JwtServiceTest {

    @Test
    void shouldGenerateAndValidateToken() {
        JwtService jwtService = new JwtService();
        setField(jwtService, "secret", "test-secret-key-12345678901234567890");
        setField(jwtService, "issuer", "livrotech-api");
        setField(jwtService, "expirationMs", 3_600_000L);

        UserDetails user = User.withUsername("admin")
                .password("secret")
                .roles("ADMIN")
                .build();

        String token = jwtService.generateToken(user);

        assertEquals("admin", jwtService.extractUsername(token));
        assertTrue(jwtService.isTokenValid(token, user));
    }

    private void setField(Object target, String fieldName, Object value) {
        try {
            var field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (ReflectiveOperationException exception) {
            throw new RuntimeException("Unable to set field: " + fieldName, exception);
        }
    }
}
