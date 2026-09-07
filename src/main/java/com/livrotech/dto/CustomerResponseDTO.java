package com.livrotech.dto;

import java.util.List;

public record CustomerResponseDTO(
        Long id,
        String name,
        String cpf,
        String status,
        List<BookResponseDTO> purchases
) {
}
