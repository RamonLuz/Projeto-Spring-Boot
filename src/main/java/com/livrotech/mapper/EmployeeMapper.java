package com.livrotech.mapper;

import com.livrotech.dto.EmployeeResponseDTO;
import com.livrotech.entity.Employee;

public final class EmployeeMapper {

    private EmployeeMapper() {
    }

    public static EmployeeResponseDTO toResponse(Employee employee) {
        return new EmployeeResponseDTO(
                employee.getId(),
                employee.getName(),
                employee.getCpf(),
                employee.getRegistrationNumber(),
                employee.getPosition(),
                employee.getStatus()
        );
    }
}
