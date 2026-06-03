package com.livrotech.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.livrotech.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}
