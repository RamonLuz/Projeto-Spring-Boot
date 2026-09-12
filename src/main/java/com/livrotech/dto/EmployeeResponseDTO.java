package com.livrotech.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record EmployeeResponseDTO(
        @NotNull Long id,
        @NotBlank String name,
        @NotBlank String cpf,
        @NotNull @Positive Integer registrationNumber,
        @NotBlank String position,
        @NotBlank String status
) {
}
