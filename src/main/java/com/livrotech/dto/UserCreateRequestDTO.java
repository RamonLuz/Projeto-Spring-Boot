package com.livrotech.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserCreateRequestDTO(
        @NotBlank(message = "Username is required") String username,
        @NotBlank(message = "Password is required") String password,
        @Pattern(regexp = "(?i)(ADMIN|USER)", message = "Role must be ADMIN or USER")
        String role) {
}
