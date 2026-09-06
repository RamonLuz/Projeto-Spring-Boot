package com.livrotech.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.livrotech.entity.ApiException;
import com.livrotech.entity.Employee;
import com.livrotech.repository.EmployeeRepository;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Employee save(Employee employee) {
        if (employee == null) {
            throw new ApiException(400, "Employee is invalid", "Body");
        }

        if (employee.getName() == null || employee.getName().isBlank()) {
            throw new ApiException(400, "Name is required", "Name");
        }

        if (employee.getCpf() == null || employee.getCpf().isBlank() || employee.getCpf().length() != 11) {
            throw new ApiException(400, "CPF must have 11 digits", "CPF");
        }

        if (employee.getPosition() == null || employee.getPosition().isBlank()) {
            throw new ApiException(400, "Position is required", "Position");
        }

        if (employee.getStatus() == null || employee.getStatus().isBlank()) {
            throw new ApiException(400, "Status is required", "Status");
        }

        return employeeRepository.save(employee);
    }

    public List<Employee> listAll() {
        return employeeRepository.findAll();
    }

    public Optional<Employee> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return employeeRepository.findById(id);
    }

    public Optional<Employee> update(Long id, Employee updatedEmployee) {
        if (id == null) {
            return Optional.empty();
        }

        if (updatedEmployee == null) {
            throw new ApiException(400, "Employee is invalid", "Body");
        }

        if (updatedEmployee.getStatus() == null || updatedEmployee.getStatus().isBlank()) {
            throw new ApiException(400, "Status is required", "Status");
        }

        Optional<Employee> existingEmployee = employeeRepository.findById(id);

        if (existingEmployee.isPresent()) {
            Employee employee = existingEmployee.get();
            employee.setStatus(updatedEmployee.getStatus());
            employeeRepository.save(employee);
            return Optional.of(employee);
        }

        return Optional.empty();
    }

    public boolean delete(Long id) {
        if (id == null) {
            return false;
        }

        if (employeeRepository.existsById(id)) {
            employeeRepository.deleteById(id);
            return true;
        }

        return false;
    }
}
