package com.livrotech.dto;

public record BookResponseDTO(
        Long id,
        String title,
        String author,
        Double price
) {
}
