package com.livrotech.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Pattern;
import com.livrotech.entity.Status;
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
    @Pattern(regexp = "\\d{11}", message = "CPF must have 11 digits")
    private String cpf;

    @NotNull
    @Positive
    private Integer registrationNumber;

    @NotBlank(message = "Position is required")
    private String position;

    @NotNull(message = "Status is required")
    private Status status;
}
