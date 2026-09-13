package com.livrotech.dto;

import jakarta.validation.constraints.NotBlank;

public record PasswordChangeRequestDTO(
        @NotBlank(message = "Current password is required") String currentPassword,
        @NotBlank(message = "New password is required") String newPassword) {
}
