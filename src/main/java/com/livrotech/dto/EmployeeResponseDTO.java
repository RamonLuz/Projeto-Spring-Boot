package com.livrotech.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.livrotech.entity.Status;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record EmployeeResponseDTO(
        Long id,
        String name,
        String cpf,
        Integer registrationNumber,
        String position,
        Status status
) {
}
