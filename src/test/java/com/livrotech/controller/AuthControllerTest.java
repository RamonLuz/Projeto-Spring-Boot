package com.livrotech.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.server.ResponseStatusException;

import com.livrotech.config.JwtService;
import com.livrotech.dto.AuthRequestDTO;
import com.livrotech.dto.AuthResponseDTO;
import com.livrotech.dto.RefreshTokenRequestDTO;

class AuthControllerTest {

    @Test
    void shouldGenerateJwtTokenWhenUserAuthenticates() {
        AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
        JwtService jwtService = mock(JwtService.class);
        UserDetailsService userDetailsService = mock(UserDetailsService.class);
        AuthController controller = new AuthController(authenticationManager, jwtService, userDetailsService);

        Authentication authentication = mock(Authentication.class);
        UserDetails principal = User.withUsername("admin")
                .password("secret")
                .roles("ADMIN")
                .build();

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(principal);
        when(jwtService.generateToken(principal)).thenReturn("jwt-token");
        when(jwtService.generateRefreshToken(principal)).thenReturn("refresh-token");

        ResponseEntity<AuthResponseDTO> response = controller.login(new AuthRequestDTO("admin", "admin123"));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("jwt-token", response.getBody().accessToken());
        assertEquals("refresh-token", response.getBody().refreshToken());
        assertEquals("Bearer", response.getBody().tokenType());
    }

    @Test
    void shouldRefreshExistingToken() {
        AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
        JwtService jwtService = mock(JwtService.class);
        UserDetailsService userDetailsService = mock(UserDetailsService.class);
        AuthController controller = new AuthController(authenticationManager, jwtService, userDetailsService);

        UserDetails principal = User.withUsername("admin")
                .password("secret")
                .roles("ADMIN")
                .build();

        when(jwtService.extractUsername("refresh-token")).thenReturn("admin");
        when(userDetailsService.loadUserByUsername("admin")).thenReturn(principal);
        when(jwtService.isRefreshTokenValid("refresh-token", principal)).thenReturn(true);
        when(jwtService.generateToken(principal)).thenReturn("new-access-token");
        when(jwtService.generateRefreshToken(principal)).thenReturn("new-refresh-token");

        ResponseEntity<AuthResponseDTO> response = controller.refresh(new RefreshTokenRequestDTO("refresh-token"));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("new-access-token", response.getBody().accessToken());
        assertEquals("new-refresh-token", response.getBody().refreshToken());
    }

    @Test
    void shouldPropagateAuthenticationFailure() {
        AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
        JwtService jwtService = mock(JwtService.class);
        UserDetailsService userDetailsService = mock(UserDetailsService.class);
        AuthController controller = new AuthController(authenticationManager, jwtService, userDetailsService);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        assertThrows(BadCredentialsException.class,
                () -> controller.login(new AuthRequestDTO("admin", "wrong-password")));
    }
}
