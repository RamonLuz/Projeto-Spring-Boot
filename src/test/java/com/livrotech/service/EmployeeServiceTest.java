package com.livrotech.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.livrotech.exception.ApiException;
import com.livrotech.entity.Employee;
import com.livrotech.entity.Status;
import com.livrotech.repository.EmployeeRepository;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    @Test
    void shouldSaveEmployeeAndTrimText() {
        Employee employee = new Employee("  Joao  ", " 12345678901 ", 10, "  Seller ", Status.ACTIVE);
        when(employeeRepository.findByCpf("12345678901")).thenReturn(Optional.empty());
        when(employeeRepository.findByRegistrationNumber(10)).thenReturn(Optional.empty());
        when(employeeRepository.save(employee)).thenReturn(employee);

        Employee saved = employeeService.save(employee);

        assertEquals("Joao", saved.getName());
        assertEquals("12345678901", saved.getCpf());
        assertEquals("Seller", saved.getPosition());
        verify(employeeRepository).save(employee);
    }

    @Test
    void shouldRejectDuplicateEmployeeCpfAndRegistration() {
        Employee employee = new Employee("Joao", "12345678901", 10, "Seller", Status.ACTIVE);
        when(employeeRepository.findByCpf("12345678901")).thenReturn(Optional.of(employee));

        ApiException cpfException = assertThrows(ApiException.class, () -> employeeService.save(employee));
        assertEquals(409, cpfException.getStatus());
        assertEquals("CPF already registered", cpfException.getMessage());

        when(employeeRepository.findByCpf("12345678901")).thenReturn(Optional.empty());
        when(employeeRepository.findByRegistrationNumber(10)).thenReturn(Optional.of(employee));

        ApiException registrationException = assertThrows(ApiException.class, () -> employeeService.save(employee));
        assertEquals(409, registrationException.getStatus());
        assertEquals("Registration number already registered", registrationException.getMessage());
    }

    @Test
    void shouldListAndFindEmployee() {
        Employee employee = new Employee("Joao", "12345678901", 10, "Seller", Status.ACTIVE);
        when(employeeRepository.findAll()).thenReturn(List.of(employee));
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        assertEquals(1, employeeService.listAll().size());
        assertEquals(Optional.of(employee), employeeService.findById(1L));
        assertEquals(Optional.empty(), employeeService.findById(null));
    }

    @Test
    void shouldUpdateEmployeeStatus() {
        Employee employee = new Employee("Joao", "12345678901", 10, "Seller", Status.ACTIVE);
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        Employee update = new Employee();
        update.setStatus(Status.INACTIVE);

        Optional<Employee> result = employeeService.update(1L, update);

        assertEquals(Optional.of(employee), result);
        assertEquals(Status.INACTIVE, employee.getStatus());
        verify(employeeRepository).save(employee);
    }

    @Test
    void shouldReturnEmptyWhenUpdatingMissingEmployee() {
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());

        assertEquals(Optional.empty(), employeeService.update(99L, new Employee(null, null, null, null, Status.ACTIVE)));
    }

    @Test
    void shouldDeleteEmployeeWhenItExists() {
        when(employeeRepository.existsById(1L)).thenReturn(true);

        assertEquals(true, employeeService.delete(1L));

        verify(employeeRepository).deleteById(1L);
    }

    @Test
    void shouldReturnFalseWhenDeletingMissingEmployee() {
        when(employeeRepository.existsById(99L)).thenReturn(false);

        assertEquals(false, employeeService.delete(99L));
        verify(employeeRepository, never()).deleteById(99L);
    }
}
