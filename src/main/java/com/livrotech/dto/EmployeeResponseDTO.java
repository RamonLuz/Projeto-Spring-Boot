package com.livrotech.dto;

public record EmployeeResponseDTO(
        Long id,
        String name,
        String cpf,
        int registrationNumber,
        String position,
        String status
) {
}
