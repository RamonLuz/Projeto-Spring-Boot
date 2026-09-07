package com.livrotech.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EmployeeRequestDTO {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "CPF is required")
    private String cpf;

    private int registrationNumber;

    @NotBlank(message = "Position is required")
    private String position;

    @NotBlank(message = "Status is required")
    private String status;
}
