package com.livrotech.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.livrotech.entity.Customer;
import com.livrotech.exception.ApiException;
import com.livrotech.repository.CustomerRepository;

@Service
public class CustomerService {

    private static final Logger log = LoggerFactory.getLogger(CustomerService.class);
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer save(Customer customer) {
        if (customer == null) {
            throw new ApiException(400, "Customer is invalid", "body");
        }

        if (customer.getName() != null) {
            customer.setName(customer.getName().trim());
        }
        if (customer.getCpf() != null) {
            customer.setCpf(customer.getCpf().trim());
        }

        if (customer.getName() == null || customer.getName().isBlank()) {
            throw new ApiException(400, "Name is required", "name");
        }

        if (customer.getCpf() == null || !customer.getCpf().matches("\\d{11}")) {
            throw new ApiException(400, "CPF must have 11 digits", "cpf");
        }

        if (customer.getStatus() == null) {
            throw new ApiException(400, "Status is required", "status");
        }

        Optional<Customer> existingCustomer = customerRepository.findByCpf(customer.getCpf());
        if (existingCustomer.isPresent()) {
            throw new ApiException(409, "CPF already registered", "cpf");
        }

        Customer savedCustomer = customerRepository.save(customer);
        log.info("Customer created with id {}", savedCustomer.getId());
        return savedCustomer;
    }

    public List<Customer> listAll() {
        return customerRepository.findAll();
    }

    public Page<Customer> listPage(Pageable pageable) {
        return customerRepository.findAll(pageable);
    }

    public Page<Customer> listPage(Pageable pageable, String name) {
        if (name == null || name.isBlank()) {
            return listPage(pageable);
        }
        return customerRepository.findByNameContainingIgnoreCase(name.trim(), pageable);
    }

    public Optional<Customer> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return customerRepository.findById(id);
    }

    public Optional<Customer> findByCpf(String cpf) {
        if (cpf == null || cpf.isBlank()) {
            return Optional.empty();
        }
        return customerRepository.findByCpf(cpf);
    }

    @Transactional
    public Optional<Customer> update(Long id, Customer updatedCustomer) {
        if (id == null) {
            return Optional.empty();
        }

        if (updatedCustomer == null) {
            throw new ApiException(400, "Customer is invalid", "body");
        }

        if (updatedCustomer.getStatus() == null) {
            throw new ApiException(400, "Status is required", "status");
        }

        Optional<Customer> existingCustomer = customerRepository.findById(id);

        if (existingCustomer.isPresent()) {
            Customer customer = existingCustomer.get();
            customer.setStatus(updatedCustomer.getStatus());
            customerRepository.save(customer);
            log.info("Customer status updated with id {}", id);
            return Optional.of(customer);
        }

        return Optional.empty();
    }

    @Transactional
    public boolean delete(Long id) {
        if (id == null) {
            return false;
        }

        if (customerRepository.existsById(id)) {
            customerRepository.deleteById(id);
            log.info("Customer deleted with id {}", id);
            return true;
        }

        return false;
    }
}