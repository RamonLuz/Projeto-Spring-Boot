package com.livrotech.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.livrotech.entity.ApiException;
import com.livrotech.entity.Book;
import com.livrotech.entity.Customer;
import com.livrotech.entity.Employee;
import com.livrotech.entity.Sale;
import com.livrotech.entity.Status;
import com.livrotech.repository.BookRepository;
import com.livrotech.repository.CustomerRepository;
import com.livrotech.repository.EmployeeRepository;
import com.livrotech.repository.SaleRepository;

@ExtendWith(MockitoExtension.class)
class SaleServiceTest {

    @Mock
    private SaleRepository saleRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private SaleService saleService;

    @Test
    void shouldRejectSaleWithFutureDate() {
        Sale sale = new Sale();
        sale.setSaleDate(LocalDate.now().plusDays(1));

        ApiException exception = assertThrows(ApiException.class, () -> saleService.save(sale));

        assertEquals(400, exception.getStatus());
        assertEquals("Sale date cannot be in the future", exception.getMessage());
    }

    @Test
    void shouldRejectSaleWithoutCustomer() {
        Sale sale = new Sale();
        sale.setBook(new Book(1L, "Book", "Author", BigDecimal.TEN));
        sale.setEmployee(new Employee());
        sale.getEmployee().setId(1L);

        ApiException exception = assertThrows(ApiException.class, () -> saleService.save(sale));

        assertEquals(400, exception.getStatus());
        assertEquals("Customer is required (id or cpf)", exception.getMessage());
    }

    @Test
    void shouldSaveSaleAndSyncCustomerPurchases() {
        Customer customer = new Customer("Ana", "12345678901", Status.ACTIVE, new ArrayList<>());
        customer.setId(1L);
        Book book = new Book(2L, "Book", "Author", BigDecimal.TEN);
        Employee employee = new Employee("Joao", "10987654321", 10, "Seller", Status.ACTIVE);
        employee.setId(3L);

        Sale sale = new Sale();
        sale.setCustomer(customer);
        sale.setBook(book);
        sale.setEmployee(employee);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(bookRepository.findById(2L)).thenReturn(Optional.of(book));
        when(employeeRepository.findById(3L)).thenReturn(Optional.of(employee));
        when(saleRepository.save(sale)).thenReturn(sale);

        Sale savedSale = saleService.save(sale);

        assertEquals(LocalDate.now(), savedSale.getSaleDate());
        assertEquals(customer, savedSale.getCustomer());
        assertEquals(book, customer.getPurchases().get(0));
    }

    @Test
    void shouldListAndFindSales() {
        Sale sale = new Sale();
        when(saleRepository.findAll()).thenReturn(List.of(sale));
        when(saleRepository.findById(1L)).thenReturn(Optional.of(sale));

        assertEquals(1, saleService.listAll().size());
        assertEquals(Optional.of(sale), saleService.findById(1L));
        assertEquals(Optional.empty(), saleService.findById(null));
    }

    @Test
    void shouldRejectNullSale() {
        ApiException exception = assertThrows(ApiException.class, () -> saleService.save(null));

        assertEquals(400, exception.getStatus());
        assertEquals("Sale is invalid", exception.getMessage());
    }

    @Test
    void shouldRejectSaleWithoutBookOrEmployee() {
        Sale withoutBook = new Sale();
        Customer customer = new Customer();
        customer.setId(1L);
        withoutBook.setCustomer(customer);

        ApiException bookException = assertThrows(ApiException.class, () -> saleService.save(withoutBook));
        assertEquals("Book is required", bookException.getMessage());

        Sale withoutEmployee = new Sale();
        withoutEmployee.setCustomer(customer);
        withoutEmployee.setBook(new Book(2L, "Book", "Author", BigDecimal.TEN));

        ApiException employeeException = assertThrows(ApiException.class, () -> saleService.save(withoutEmployee));
        assertEquals("Employee is required", employeeException.getMessage());
    }

    @Test
    void shouldFindCustomerByCpfWhenCreatingSale() {
        Customer customer = new Customer("Ana", "12345678901", Status.ACTIVE, new ArrayList<>());
        Book book = new Book(2L, "Book", "Author", BigDecimal.TEN);
        Employee employee = new Employee();
        employee.setId(3L);

        Customer requestedCustomer = new Customer();
        requestedCustomer.setCpf("12345678901");
        Sale sale = new Sale();
        sale.setCustomer(requestedCustomer);
        sale.setBook(book);
        sale.setEmployee(employee);

        when(customerRepository.findByCpf("12345678901")).thenReturn(Optional.of(customer));
        when(bookRepository.findById(2L)).thenReturn(Optional.of(book));
        when(employeeRepository.findById(3L)).thenReturn(Optional.of(employee));
        when(saleRepository.save(sale)).thenReturn(sale);

        assertEquals(customer, saleService.save(sale).getCustomer());
        verify(customerRepository).findByCpf("12345678901");
    }

    @Test
    void shouldRejectSaleWhenReferencedEntityDoesNotExist() {
        Customer customer = new Customer();
        customer.setId(1L);
        Book book = new Book(2L, "Book", "Author", BigDecimal.TEN);
        Employee employee = new Employee();
        employee.setId(3L);
        Sale sale = new Sale();
        sale.setCustomer(customer);
        sale.setBook(book);
        sale.setEmployee(employee);

        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        ApiException customerException = assertThrows(ApiException.class, () -> saleService.save(sale));
        assertEquals("Customer does not exist", customerException.getMessage());

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(bookRepository.findById(2L)).thenReturn(Optional.empty());

        ApiException bookException = assertThrows(ApiException.class, () -> saleService.save(sale));
        assertEquals("Book does not exist", bookException.getMessage());

        when(bookRepository.findById(2L)).thenReturn(Optional.of(book));
        when(employeeRepository.findById(3L)).thenReturn(Optional.empty());

        ApiException employeeException = assertThrows(ApiException.class, () -> saleService.save(sale));
        assertEquals("Employee does not exist", employeeException.getMessage());
    }

    @Test
    void shouldNotDuplicateBookInCustomerPurchases() {
        Book book = new Book(2L, "Book", "Author", BigDecimal.TEN);
        Customer customer = new Customer("Ana", "12345678901", Status.ACTIVE, new ArrayList<>(List.of(book)));
        customer.setId(1L);
        Employee employee = new Employee();
        employee.setId(3L);
        Sale sale = new Sale();
        sale.setCustomer(customer);
        sale.setBook(book);
        sale.setEmployee(employee);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(bookRepository.findById(2L)).thenReturn(Optional.of(book));
        when(employeeRepository.findById(3L)).thenReturn(Optional.of(employee));
        when(saleRepository.save(sale)).thenReturn(sale);

        saleService.save(sale);

        verify(customerRepository, never()).save(customer);
    }
}
