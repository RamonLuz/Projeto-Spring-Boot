package com.livrotech.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SaleRequestDTO {

    /** Either customerId or customerCpf must be provided. Service will prefer id when present. */
    private String customerCpf;

    @Positive(message = "Customer id must be a positive number")
    private Long customerId;

    @NotNull(message = "Employee is required")
    @Positive(message = "Employee id must be a positive number")
    private Long employeeId;

    @NotNull(message = "Book is required")
    @Positive(message = "Book id must be a positive number")
    private Long bookId;

    @PastOrPresent(message = "Sale date cannot be in the future")
    private LocalDate saleDate;
}
