package com.livrotech.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserUpdateRequestDTO(
        @NotBlank(message = "Username is required") String username,
        @Pattern(regexp = "(?i)(ADMIN|USER)", message = "Role must be ADMIN or USER")
        String role) {
}
