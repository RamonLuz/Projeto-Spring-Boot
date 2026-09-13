package com.livrotech.mapper;

import com.livrotech.dto.EmployeeResponseDTO;
import com.livrotech.dto.EmployeeRequestDTO;
import com.livrotech.dto.EmployeeStatusUpdateRequestDTO;
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

    public static Employee toEntity(EmployeeRequestDTO dto) {
        return new Employee(
                dto.getName(),
                dto.getCpf(),
                dto.getRegistrationNumber(),
                dto.getPosition(),
                dto.getStatus()
        );
    }

    public static Employee toStatusEntity(EmployeeStatusUpdateRequestDTO dto) {
        Employee employee = new Employee();
        employee.setStatus(dto.status());
        return employee;
    }
}
