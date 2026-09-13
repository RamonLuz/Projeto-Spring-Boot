package com.livrotech.dto;

import com.livrotech.entity.AppUser;

public record UserResponseDTO(Long id, String username, String role) {
    public static UserResponseDTO from(AppUser appUser) {
        return new UserResponseDTO(appUser.getId(), appUser.getUsername(), appUser.getRole().name());
    }
}
