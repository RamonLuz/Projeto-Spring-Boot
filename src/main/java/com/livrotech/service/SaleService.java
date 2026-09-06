package com.livrotech.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

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

    public Sale save(Sale sale) {
        if (sale == null) {
            throw new ApiException(400, "Sale is invalid", "Body");
        }

        if (sale.getCustomer() == null || sale.getCustomer().getCpf() == null || sale.getCustomer().getCpf().isBlank()) {
            throw new ApiException(400, "Customer CPF is required", "Body");
        }

        if (sale.getBook() == null || sale.getBook().getId() == null) {
            throw new ApiException(400, "Book is required", "Body");
        }

        if (sale.getEmployee() == null || sale.getEmployee().getId() == null) {
            throw new ApiException(400, "Employee is required", "Body");
        }

        Customer customer = customerRepository.findByCpf(sale.getCustomer().getCpf())
                .orElseThrow(() -> new ApiException(400, "Customer does not exist", "Body"));

        Book book = bookRepository.findById(sale.getBook().getId())
                .orElseThrow(() -> new ApiException(400, "Book does not exist", "Body"));

        Employee employee = employeeRepository.findById(sale.getEmployee().getId())
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
