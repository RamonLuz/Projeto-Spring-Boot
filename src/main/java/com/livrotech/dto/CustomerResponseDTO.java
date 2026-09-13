package com.livrotech.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import com.livrotech.entity.Status;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CustomerResponseDTO(
        Long id,
        String name,
        String cpf,
        Status status,
        List<BookResponseDTO> purchases
) {
}
