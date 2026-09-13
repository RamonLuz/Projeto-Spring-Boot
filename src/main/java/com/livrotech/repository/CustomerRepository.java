package com.livrotech.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import com.livrotech.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

	Optional<Customer> findByCpf(String cpf);

	Page<Customer> findByNameContainingIgnoreCase(
            String name, org.springframework.data.domain.Pageable pageable);

}
