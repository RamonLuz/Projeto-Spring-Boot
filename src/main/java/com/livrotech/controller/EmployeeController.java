package com.livrotech.controller;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.livrotech.dto.EmployeeRequestDTO;
import com.livrotech.dto.EmployeeResponseDTO;
import com.livrotech.dto.EmployeeStatusUpdateRequestDTO;
import com.livrotech.entity.Employee;
import com.livrotech.mapper.EmployeeMapper;
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
    public ResponseEntity<Page<EmployeeResponseDTO>> getAll(
            @PageableDefault(size = 20, sort = "id") Pageable pageable,
            @RequestParam(required = false) String name) {
        return ResponseEntity.ok(employeeService.listPage(pageable, name).map(EmployeeMapper::toResponse));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponseDTO> getById(@PathVariable @Positive Long id) {
        Optional<Employee> employee = employeeService.findById(id);

        if (employee.isPresent()) {
            return ResponseEntity.ok(EmployeeMapper.toResponse(employee.get()));
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
        return ResponseEntity.status(HttpStatus.CREATED).body(EmployeeMapper.toResponse(savedEmployee));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponseDTO> update(@PathVariable @Positive Long id,
            @Valid @RequestBody EmployeeStatusUpdateRequestDTO dto) {
        Employee employee = new Employee();
        employee.setStatus(dto.status());

        Optional<Employee> updatedEmployee = employeeService.update(id, employee);

        if (updatedEmployee.isPresent()) {
            return ResponseEntity.ok(EmployeeMapper.toResponse(updatedEmployee.get()));
        }

        return ResponseEntity.notFound().build();
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
