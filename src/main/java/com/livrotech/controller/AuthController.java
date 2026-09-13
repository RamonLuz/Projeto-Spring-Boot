package com.livrotech.controller;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.livrotech.config.JwtService;
import com.livrotech.dto.AuthRequestDTO;
import com.livrotech.dto.AuthResponseDTO;
import com.livrotech.dto.RefreshTokenRequestDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
@ConditionalOnProperty(name = "app.security.enabled", havingValue = "true")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService,
            UserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody AuthRequestDTO request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password()));

        UserDetails principal = (UserDetails) authentication.getPrincipal();
        String accessToken = jwtService.generateToken(principal);
        String refreshToken = jwtService.generateRefreshToken(principal);

        return ResponseEntity.ok(new AuthResponseDTO(accessToken, refreshToken, "Bearer"));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDTO> refresh(@Valid @RequestBody RefreshTokenRequestDTO request) {
        try {
            String username = jwtService.extractUsername(request.refreshToken());
            UserDetails principal = userDetailsService.loadUserByUsername(username);
            if (!jwtService.isRefreshTokenValid(request.refreshToken(), principal)) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token");
            }

            String accessToken = jwtService.generateToken(principal);
            String refreshToken = jwtService.generateRefreshToken(principal);

            return ResponseEntity.ok(new AuthResponseDTO(accessToken, refreshToken, "Bearer"));
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token", exception);
        }
    }
}
