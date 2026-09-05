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
        Optional<Customer> existingCustomer = customerRepository.findByCpf(customer.getCpf());

        if (existingCustomer.isPresent() && existingCustomer.get().equals(customer)) {
            throw new ApiException(400, "CPF already registered", "CPF");
        }

        if (customer.getCpf() == null || customer.getCpf().length() < 11) {
            throw new ApiException(400, "CPF must have 11 digits", "CPF");
        }

        return customerRepository.save(customer);
    }

    public List<Customer> listAll() {
        return customerRepository.findAll();
    }

    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }

    public Optional<Customer> findByCpf(String cpf) {
        return customerRepository.findByCpf(cpf);
    }

    public Optional<Customer> update(Long id, Customer updatedCustomer) {
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
        if (customerRepository.existsById(id)) {
            customerRepository.deleteById(id);
            return true;
        }

        return false;
    }
}