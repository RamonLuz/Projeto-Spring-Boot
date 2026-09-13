package com.livrotech.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.AssertTrue;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.livrotech.validation.ValidCpf;

@Getter
@Setter
@NoArgsConstructor
public class SaleRequestDTO {

    /** Either customerId or customerCpf must be provided. Service will prefer id when present. */
    @Pattern(regexp = "\\d{11}", message = "CPF must have 11 digits")
    @ValidCpf
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

    @AssertTrue(message = "Customer id or customer CPF is required")
    public boolean hasCustomerReference() {
        return customerId != null || (customerCpf != null && !customerCpf.isBlank());
    }
}
