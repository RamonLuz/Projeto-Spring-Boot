package com.livrotech.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.livrotech.entity.ApiException;
import com.livrotech.entity.Customer;
import com.livrotech.repository.CustomerRepository;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer save(Customer customer) {
        if (customer == null) {
            throw new ApiException(400, "Customer is invalid", "Body");
        }

        if (customer.getName() == null || customer.getName().isBlank()) {
            throw new ApiException(400, "Name is required", "Name");
        }

        if (customer.getCpf() == null || customer.getCpf().isBlank() || customer.getCpf().length() != 11) {
            throw new ApiException(400, "CPF must have 11 digits", "CPF");
        }

        if (customer.getStatus() == null || customer.getStatus().isBlank()) {
            throw new ApiException(400, "Status is required", "Status");
        }

        Optional<Customer> existingCustomer = customerRepository.findByCpf(customer.getCpf());
        if (existingCustomer.isPresent()) {
            throw new ApiException(400, "CPF already registered", "CPF");
        }

        return customerRepository.save(customer);
    }

    public List<Customer> listAll() {
        return customerRepository.findAll();
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

    public Optional<Customer> update(Long id, Customer updatedCustomer) {
        if (id == null) {
            return Optional.empty();
        }

        if (updatedCustomer == null) {
            throw new ApiException(400, "Customer is invalid", "Body");
        }

        if (updatedCustomer.getStatus() == null || updatedCustomer.getStatus().isBlank()) {
            throw new ApiException(400, "Status is required", "Status");
        }

        Optional<Customer> existingCustomer = customerRepository.findById(id);

        if (existingCustomer.isPresent()) {
            Customer customer = existingCustomer.get();
            customer.setStatus(updatedCustomer.getStatus());
            customerRepository.save(customer);
            return Optional.of(customer);
        }

        return Optional.empty();
    }

    public boolean delete(Long id) {
        if (id == null) {
            return false;
        }

        if (customerRepository.existsById(id)) {
            customerRepository.deleteById(id);
            return true;
        }

        return false;
    }
}