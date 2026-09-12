package com.livrotech.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record BookResponseDTO(
        @NotNull Long id,
        @NotBlank String title,
        @NotBlank String author,
        @NotNull @PositiveOrZero BigDecimal price
) {
}
