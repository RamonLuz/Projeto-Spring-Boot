package com.livrotech.dto;

import java.time.LocalDate;

public record SaleResponseDTO(
        Long id,
        Long customerId,
        String customerCpf,
        Long employeeId,
        Long bookId,
        LocalDate saleDate
) {
}
