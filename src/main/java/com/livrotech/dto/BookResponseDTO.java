package com.livrotech.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record BookResponseDTO(
        Long id,
        String title,
        String author,
        BigDecimal price
) {
}
