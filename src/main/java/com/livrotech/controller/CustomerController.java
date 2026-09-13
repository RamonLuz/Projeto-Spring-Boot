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

import com.livrotech.dto.CustomerRequestDTO;
import com.livrotech.dto.BookResponseDTO;
import com.livrotech.dto.CustomerResponseDTO;
import com.livrotech.dto.CustomerStatusUpdateRequestDTO;
import com.livrotech.entity.Customer;
import com.livrotech.service.CustomerService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;

@RestController
@Validated
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponseDTO>> getAll() {
        List<Customer> customers = customerService.listAll();

        return ResponseEntity.ok(customers.stream().map(this::toResponse).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> getById(@PathVariable @Positive Long id) {
        Optional<Customer> customer = customerService.findById(id);

        if (customer.isPresent()) {
            return ResponseEntity.ok(toResponse(customer.get()));
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<CustomerResponseDTO> create(@Valid @RequestBody CustomerRequestDTO dto) {
        Customer customer = new Customer();
        customer.setName(dto.getName());
        customer.setCpf(dto.getCpf());
        customer.setStatus(dto.getStatus());

        Customer savedCustomer = customerService.save(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(savedCustomer));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> update(@PathVariable @Positive Long id,
            @Valid @RequestBody CustomerStatusUpdateRequestDTO dto) {
        Customer customer = new Customer();
        customer.setStatus(dto.status());

        Optional<Customer> updatedCustomer = customerService.update(id, customer);

        if (updatedCustomer.isPresent()) {
            return ResponseEntity.ok(toResponse(updatedCustomer.get()));
        }

        return ResponseEntity.notFound().build();
    }

    private CustomerResponseDTO toResponse(Customer customer) {
        List<BookResponseDTO> purchases = customer.getPurchases() == null
                ? List.of()
                : customer.getPurchases().stream()
                        .map(book -> new BookResponseDTO(book.getId(), book.getTitle(), book.getAuthor(), book.getPrice()))
                        .toList();

        return new CustomerResponseDTO(customer.getId(), customer.getName(), customer.getCpf(), customer.getStatus(), purchases);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable @Positive Long id) {
        boolean removed = customerService.delete(id);

        if (removed) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}
