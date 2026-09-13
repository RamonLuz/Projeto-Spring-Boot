package com.livrotech.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.livrotech.dto.EmployeeRequestDTO;
import com.livrotech.dto.EmployeeResponseDTO;
import com.livrotech.dto.StatusUpdateRequestDTO;
import com.livrotech.entity.Employee;
import com.livrotech.service.EmployeeService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;

@RestController
@Validated
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public ResponseEntity<List<EmployeeResponseDTO>> getAll() {
        return ResponseEntity.ok(employeeService.listAll().stream().map(this::toResponse).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponseDTO> getById(@PathVariable @Positive Long id) {
        Optional<Employee> employee = employeeService.findById(id);

        if (employee.isPresent()) {
            return ResponseEntity.ok(toResponse(employee.get()));
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<EmployeeResponseDTO> create(@Valid @RequestBody EmployeeRequestDTO dto) {
        Employee employee = new Employee();
        employee.setName(dto.getName());
        employee.setCpf(dto.getCpf());
        employee.setRegistrationNumber(dto.getRegistrationNumber());
        employee.setPosition(dto.getPosition());
        employee.setStatus(dto.getStatus());

        Employee savedEmployee = employeeService.save(employee);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(savedEmployee));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponseDTO> update(@PathVariable @Positive Long id,
            @Valid @RequestBody StatusUpdateRequestDTO dto) {
        Employee employee = new Employee();
        employee.setStatus(dto.status());

        Optional<Employee> updatedEmployee = employeeService.update(id, employee);

        if (updatedEmployee.isPresent()) {
            return ResponseEntity.ok(toResponse(updatedEmployee.get()));
        }

        return ResponseEntity.notFound().build();
    }

    private EmployeeResponseDTO toResponse(Employee employee) {
        return new EmployeeResponseDTO(employee.getId(), employee.getName(), employee.getCpf(),
                employee.getRegistrationNumber(), employee.getPosition(), employee.getStatus());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable @Positive Long id) {
        boolean removed = employeeService.delete(id);

        if (removed) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}
