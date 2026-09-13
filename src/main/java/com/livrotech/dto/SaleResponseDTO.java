package com.livrotech.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record SaleResponseDTO(
        Long id,
        Long customerId,
        String customerCpf,
        Long employeeId,
        Long bookId,
        @JsonFormat(pattern = "yyyy-MM-dd") LocalDate saleDate
) {
}
