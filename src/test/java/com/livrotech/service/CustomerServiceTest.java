package com.livrotech.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.livrotech.entity.ApiException;
import com.livrotech.entity.Customer;
import com.livrotech.entity.Status;
import com.livrotech.repository.CustomerRepository;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @Test
    void shouldRejectCustomerWithInvalidCpf() {
        Customer customer = new Customer("Ana", "123", Status.ACTIVE, null);

        ApiException exception = assertThrows(ApiException.class, () -> customerService.save(customer));

        assertEquals(400, exception.getStatus());
        assertEquals("CPF must have 11 digits", exception.getMessage());
    }

    @Test
    void shouldRejectDuplicateCustomerCpf() {
        Customer customer = new Customer("Ana", "12345678901", Status.ACTIVE, null);
        when(customerRepository.findByCpf("12345678901")).thenReturn(Optional.of(customer));

        ApiException exception = assertThrows(ApiException.class, () -> customerService.save(customer));

        assertEquals(409, exception.getStatus());
        assertEquals("CPF already registered", exception.getMessage());
    }

    @Test
    void shouldTrimCustomerNameAndCpfBeforeSaving() {
        Customer customer = new Customer("  Ana  ", " 12345678901 ", Status.ACTIVE, null);
        when(customerRepository.findByCpf("12345678901")).thenReturn(Optional.empty());
        when(customerRepository.save(customer)).thenReturn(customer);

        Customer savedCustomer = customerService.save(customer);

        assertEquals("Ana", savedCustomer.getName());
        assertEquals("12345678901", savedCustomer.getCpf());
    }

    @Test
    void shouldListCustomers() {
        Customer customer = new Customer("Ana", "12345678901", Status.ACTIVE, null);
        when(customerRepository.findAll()).thenReturn(List.of(customer));

        assertEquals(1, customerService.listAll().size());
    }

    @Test
    void shouldFindCustomerByIdAndCpf() {
        Customer customer = new Customer("Ana", "12345678901", Status.ACTIVE, null);
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerRepository.findByCpf("12345678901")).thenReturn(Optional.of(customer));

        assertEquals(Optional.of(customer), customerService.findById(1L));
        assertEquals(Optional.of(customer), customerService.findByCpf("12345678901"));
    }

    @Test
    void shouldReturnEmptyForInvalidCustomerSearch() {
        assertEquals(Optional.empty(), customerService.findById(null));
        assertEquals(Optional.empty(), customerService.findByCpf(" "));
        verify(customerRepository, never()).findById(null);
        verify(customerRepository, never()).findByCpf(" ");
    }

    @Test
    void shouldUpdateCustomerStatus() {
        Customer customer = new Customer("Ana", "12345678901", Status.ACTIVE, null);
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        Customer update = new Customer();
        update.setStatus(Status.INACTIVE);

        Optional<Customer> result = customerService.update(1L, update);

        assertEquals(Optional.of(customer), result);
        assertEquals(Status.INACTIVE, customer.getStatus());
        verify(customerRepository).save(customer);
    }

    @Test
    void shouldDeleteCustomerWhenItExists() {
        when(customerRepository.existsById(1L)).thenReturn(true);

        assertEquals(true, customerService.delete(1L));

        verify(customerRepository).deleteById(1L);
    }

    @Test
    void shouldReturnFalseWhenDeletingMissingCustomer() {
        when(customerRepository.existsById(99L)).thenReturn(false);

        assertEquals(false, customerService.delete(99L));
        verify(customerRepository, never()).deleteById(99L);
    }
}
