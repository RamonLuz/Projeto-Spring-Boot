package com.livrotech.controller;

import java.util.List;
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

import com.livrotech.dto.CustomerRequestDTO;
import com.livrotech.dto.CustomerResponseDTO;
import com.livrotech.dto.CustomerStatusUpdateRequestDTO;
import com.livrotech.entity.Customer;
import com.livrotech.mapper.CustomerMapper;
import com.livrotech.service.CustomerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;

@RestController
@Validated
@RequestMapping("/customers")
@Tag(name = "Clientes", description = "Operacoes de clientes")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    @Operation(summary = "Lista clientes", description = "Lista clientes com paginacao e filtro opcional por nome.")
    public ResponseEntity<Page<CustomerResponseDTO>> getAll(
            @PageableDefault(size = 20, sort = "id") Pageable pageable,
            @RequestParam(required = false) String name) {
        return ResponseEntity.ok(customerService.listPage(pageable, name).map(CustomerMapper::toResponse));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca cliente por ID")
    public ResponseEntity<CustomerResponseDTO> getById(@PathVariable @Positive Long id) {
        Optional<Customer> customer = customerService.findById(id);

        if (customer.isPresent()) {
            return ResponseEntity.ok(CustomerMapper.toResponse(customer.get()));
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(summary = "Cadastra cliente")
    public ResponseEntity<CustomerResponseDTO> create(@Valid @RequestBody CustomerRequestDTO dto) {
        Customer savedCustomer = customerService.save(CustomerMapper.toEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(CustomerMapper.toResponse(savedCustomer));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza status do cliente")
    public ResponseEntity<CustomerResponseDTO> update(@PathVariable @Positive Long id,
            @Valid @RequestBody CustomerStatusUpdateRequestDTO dto) {
        Optional<Customer> updatedCustomer = customerService.update(id, CustomerMapper.toStatusEntity(dto));

        if (updatedCustomer.isPresent()) {
            return ResponseEntity.ok(CustomerMapper.toResponse(updatedCustomer.get()));
        }

        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Atualiza parcialmente o status do cliente")
    public ResponseEntity<CustomerResponseDTO> patchStatus(@PathVariable @Positive Long id,
            @Valid @RequestBody CustomerStatusUpdateRequestDTO dto) {
        return update(id, dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove cliente")
    public ResponseEntity<Void> delete(@PathVariable @Positive Long id) {
        boolean removed = customerService.delete(id);

        if (removed) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}
