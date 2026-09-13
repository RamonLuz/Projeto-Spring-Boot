package com.livrotech.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;

import com.livrotech.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

	Optional<Customer> findByCpf(String cpf);

	@Override
	@EntityGraph(attributePaths = "purchases")
	List<Customer> findAll();

	@Override
	@EntityGraph(attributePaths = "purchases")
	Page<Customer> findAll(Pageable pageable);

	@Override
	@EntityGraph(attributePaths = "purchases")
	Optional<Customer> findById(Long id);
}
