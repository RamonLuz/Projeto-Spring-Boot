package com.livrotech.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

import com.livrotech.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByCpf(String cpf);

    Optional<Employee> findByRegistrationNumber(Integer registrationNumber);

    Page<Employee> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
