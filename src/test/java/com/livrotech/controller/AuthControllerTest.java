package com.livrotech.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import com.livrotech.config.JwtService;
import com.livrotech.dto.AuthRequestDTO;
import com.livrotech.dto.AuthResponseDTO;

class AuthControllerTest {

    @Test
    void shouldGenerateJwtTokenWhenUserAuthenticates() {
        AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
        JwtService jwtService = mock(JwtService.class);
        AuthController controller = new AuthController(authenticationManager, jwtService);

        Authentication authentication = mock(Authentication.class);
        UserDetails principal = User.withUsername("admin")
                .password("secret")
                .roles("ADMIN")
                .build();

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(principal);
        when(jwtService.generateToken(principal)).thenReturn("jwt-token");

        ResponseEntity<AuthResponseDTO> response = controller.login(new AuthRequestDTO("admin", "admin123"));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("jwt-token", response.getBody().token());
    }
}
