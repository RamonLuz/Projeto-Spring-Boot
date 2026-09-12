package com.livrotech.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import com.livrotech.entity.Status;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CustomerResponseDTO(
        @NotNull Long id,
        @NotBlank String name,
        @NotBlank String cpf,
        @NotNull Status status,
        @NotNull @Valid List<BookResponseDTO> purchases
) {
}
