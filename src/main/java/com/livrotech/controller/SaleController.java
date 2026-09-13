package com.livrotech.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.livrotech.dto.SaleRequestDTO;
import com.livrotech.dto.SaleResponseDTO;
import com.livrotech.entity.Book;
import com.livrotech.entity.Customer;
import com.livrotech.entity.Employee;
import com.livrotech.entity.Sale;
import com.livrotech.mapper.SaleMapper;
import com.livrotech.service.SaleService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;

@RestController
@Validated
@RequestMapping("/sales")
public class SaleController {

    private final SaleService saleService;

    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    @GetMapping
    public ResponseEntity<List<SaleResponseDTO>> getAll() {
        return ResponseEntity.ok(saleService.listAll().stream().map(SaleMapper::toResponse).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SaleResponseDTO> getById(@PathVariable @Positive Long id) {
        Optional<Sale> sale = saleService.findById(id);

        if (sale.isPresent()) {
            return ResponseEntity.ok(SaleMapper.toResponse(sale.get()));
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<SaleResponseDTO> create(@Valid @RequestBody SaleRequestDTO dto) {
        Customer customer = new Customer();
        if (dto.getCustomerId() != null) {
            customer.setId(dto.getCustomerId());
        } else if (dto.getCustomerCpf() != null && !dto.getCustomerCpf().isBlank()) {
            customer.setCpf(dto.getCustomerCpf());
        }

        Employee employee = new Employee();
        employee.setId(dto.getEmployeeId());

        Book book = new Book();
        book.setId(dto.getBookId());

        Sale sale = new Sale();
        sale.setCustomer(customer);
        sale.setEmployee(employee);
        sale.setBook(book);
        if (dto.getSaleDate() != null) {
            sale.setSaleDate(dto.getSaleDate());
        }

        Sale savedSale = saleService.save(sale);
        return ResponseEntity.status(HttpStatus.CREATED).body(SaleMapper.toResponse(savedSale));
    }
}
