package com.livrotech.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import com.livrotech.entity.Status;
import com.livrotech.validation.ValidCpf;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EmployeeRequestDTO {

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must have at most 100 characters")
    private String name;

    @NotBlank(message = "CPF is required")
    @Pattern(regexp = "\\d{11}", message = "CPF must have 11 digits")
    @ValidCpf
    private String cpf;

    @NotNull
    @Positive
    private Integer registrationNumber;

    @NotBlank(message = "Position is required")
    @Size(max = 80, message = "Position must have at most 80 characters")
    private String position;

    @NotNull(message = "Status is required")
    private Status status;
}
