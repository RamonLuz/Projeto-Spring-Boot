package com.livrotech.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

import com.livrotech.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByCpf(String cpf);

    Optional<Employee> findByRegistrationNumber(Integer registrationNumber);
}
