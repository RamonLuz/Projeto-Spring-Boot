package com.livrotech.dto;

public record AuthResponseDTO(String accessToken, String refreshToken, String tokenType) {

    public AuthResponseDTO(String token) {
        this(token, null, "Bearer");
    }

    public String token() {
        return accessToken;
    }
}
