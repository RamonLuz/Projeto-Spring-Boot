package com.livrotech.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SaleRequestDTO {

    @NotBlank(message = "Customer CPF is required")
    private String customerCpf;

    @NotNull(message = "Employee is required")
    private Long employeeId;

    @NotNull(message = "Book is required")
    private Long bookId;

    private LocalDate saleDate;
}
