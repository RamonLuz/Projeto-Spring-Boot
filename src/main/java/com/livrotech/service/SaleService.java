package com.livrotech.service;

import java.util.ArrayList;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.livrotech.entity.ApiException;
import com.livrotech.entity.Book;
import com.livrotech.entity.Customer;
import com.livrotech.entity.Employee;
import com.livrotech.entity.Sale;
import com.livrotech.repository.BookRepository;
import com.livrotech.repository.CustomerRepository;
import com.livrotech.repository.EmployeeRepository;
import com.livrotech.repository.SaleRepository;

@Service
public class SaleService {

    private final CustomerRepository customerRepository;
    private final EmployeeRepository employeeRepository;
    private final BookRepository bookRepository;
    private final SaleRepository saleRepository;

    public SaleService(SaleRepository saleRepository, CustomerRepository customerRepository,
            BookRepository bookRepository, EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
        this.bookRepository = bookRepository;
        this.saleRepository = saleRepository;
        this.customerRepository = customerRepository;
    }

    public List<Sale> listAll() {
        return saleRepository.findAll();
    }

    public Optional<Sale> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return saleRepository.findById(id);
    }

    @Transactional
    public Sale save(Sale sale) {
        if (sale == null) {
            throw new ApiException(400, "Sale is invalid", "Body");
        }

        if (sale.getSaleDate() == null) {
            sale.setSaleDate(LocalDate.now());
        } else if (sale.getSaleDate().isAfter(LocalDate.now())) {
            throw new ApiException(400, "Sale date cannot be in the future", "SaleDate");
        }

        if (sale.getCustomer() == null || (sale.getCustomer().getId() == null
                && (sale.getCustomer().getCpf() == null || sale.getCustomer().getCpf().isBlank()))) {
            throw new ApiException(400, "Customer is required (id or cpf)", "Body");
        }

        Book requestedBook = sale.getBook();
        if (requestedBook == null || requestedBook.getId() == null) {
            throw new ApiException(400, "Book is required", "Body");
        }

        Employee requestedEmployee = sale.getEmployee();
        if (requestedEmployee == null || requestedEmployee.getId() == null) {
            throw new ApiException(400, "Employee is required", "Body");
        }

        long bookId = requestedBook.getId();
        long employeeId = requestedEmployee.getId();

        Customer customer;
        if (sale.getCustomer().getId() != null) {
            customer = customerRepository.findById(sale.getCustomer().getId())
                    .orElseThrow(() -> new ApiException(400, "Customer does not exist", "Body"));
        } else {
            customer = customerRepository.findByCpf(sale.getCustomer().getCpf())
                    .orElseThrow(() -> new ApiException(400, "Customer does not exist", "Body"));
        }

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ApiException(400, "Book does not exist", "Body"));

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ApiException(400, "Employee does not exist", "Body"));

        sale.setCustomer(customer);
        sale.setBook(book);
        sale.setEmployee(employee);

        syncCustomerPurchases(customer, book);

        return saleRepository.save(sale);
    }

    private void syncCustomerPurchases(Customer customer, Book book) {
        if (customer.getPurchases() == null) {
            customer.setPurchases(new ArrayList<>());
        }

        if (!customer.getPurchases().contains(book)) {
            customer.getPurchases().add(book);
            customerRepository.save(customer);
        }
    }
}
