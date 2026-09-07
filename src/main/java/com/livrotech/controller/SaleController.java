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
import com.livrotech.service.SaleService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/sales")
public class SaleController {

    private final SaleService saleService;

    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    @GetMapping
    public ResponseEntity<List<SaleResponseDTO>> getAll() {
        return ResponseEntity.ok(saleService.listAll().stream().map(this::toResponse).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SaleResponseDTO> getById(@PathVariable Long id) {
        Optional<Sale> sale = saleService.findById(id);

        if (sale.isPresent()) {
            return ResponseEntity.ok(toResponse(sale.get()));
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<SaleResponseDTO> create(@Valid @RequestBody SaleRequestDTO dto) {
        Customer customer = new Customer();
        customer.setCpf(dto.getCustomerCpf());

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
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(savedSale));
    }

    private SaleResponseDTO toResponse(Sale sale) {
        return new SaleResponseDTO(sale.getId(), sale.getCustomer().getId(), sale.getCustomer().getCpf(),
                sale.getEmployee().getId(), sale.getBook().getId(), sale.getSaleDate());
    }
}
