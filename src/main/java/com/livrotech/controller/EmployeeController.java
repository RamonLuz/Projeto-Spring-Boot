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
import org.springframework.web.bind.annotation.PatchMapping;
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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;

@RestController
@Validated
@RequestMapping("/employees")
@Tag(name = "Funcionarios", description = "Operacoes de funcionarios")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    @Operation(summary = "Lista funcionarios", description = "Lista funcionarios com paginacao e filtro opcional por nome.")
    public ResponseEntity<Page<EmployeeResponseDTO>> getAll(
            @ParameterObject @Parameter(description = "Use page, size e sort=campo,asc|desc") @PageableDefault(size = 20, sort = "id") Pageable pageable,
            @RequestParam(required = false) String name) {
        return ResponseEntity.ok(employeeService.listPage(pageable, name).map(EmployeeMapper::toResponse));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca funcionario por ID")
    public ResponseEntity<EmployeeResponseDTO> getById(@PathVariable @Positive Long id) {
        Optional<Employee> employee = employeeService.findById(id);

        if (employee.isPresent()) {
            return ResponseEntity.ok(EmployeeMapper.toResponse(employee.get()));
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(summary = "Cadastra funcionario")
    public ResponseEntity<EmployeeResponseDTO> create(@Valid @RequestBody EmployeeRequestDTO dto) {
        Employee savedEmployee = employeeService.save(EmployeeMapper.toEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(EmployeeMapper.toResponse(savedEmployee));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza status do funcionario")
    public ResponseEntity<EmployeeResponseDTO> update(@PathVariable @Positive Long id,
            @Valid @RequestBody EmployeeStatusUpdateRequestDTO dto) {
        Optional<Employee> updatedEmployee = employeeService.update(id, EmployeeMapper.toStatusEntity(dto));

        if (updatedEmployee.isPresent()) {
            return ResponseEntity.ok(EmployeeMapper.toResponse(updatedEmployee.get()));
        }

        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Atualiza parcialmente o status do funcionario")
    public ResponseEntity<EmployeeResponseDTO> patchStatus(@PathVariable @Positive Long id,
            @Valid @RequestBody EmployeeStatusUpdateRequestDTO dto) {
        return update(id, dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove funcionario")
    public ResponseEntity<Void> delete(@PathVariable @Positive Long id) {
        boolean removed = employeeService.delete(id);

        if (removed) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}
