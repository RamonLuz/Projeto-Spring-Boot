package com.livrotech.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record SaleResponseDTO(
        @NotNull Long id,
        @NotNull Long customerId,
        @NotBlank String customerCpf,
        @NotNull Long employeeId,
        @NotNull Long bookId,
        @NotNull @JsonFormat(pattern = "yyyy-MM-dd") LocalDate saleDate
) {
}
