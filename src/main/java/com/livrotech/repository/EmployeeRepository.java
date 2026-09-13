package com.livrotech.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.livrotech.entity.Employee;
import com.livrotech.entity.Status;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByCpf(String cpf);

    Optional<Employee> findByRegistrationNumber(Integer registrationNumber);

    Page<Employee> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<Employee> findByStatus(Status status, Pageable pageable);

    Page<Employee> findByNameContainingIgnoreCaseAndStatus(String name, Status status, Pageable pageable);
}
