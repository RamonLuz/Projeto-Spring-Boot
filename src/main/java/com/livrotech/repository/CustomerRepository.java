package com.livrotech.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.livrotech.entity.Customer;
import com.livrotech.entity.Status;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByCpf(String cpf);

    Page<Customer> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<Customer> findByStatus(Status status, Pageable pageable);

    Page<Customer> findByNameContainingIgnoreCaseAndStatus(String name, Status status, Pageable pageable);
}
