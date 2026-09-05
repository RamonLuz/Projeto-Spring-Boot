package com.livrotech.service;

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
        return saleRepository.findById(id);
    }

    public Sale save(Sale sale) {
        Customer customer = customerRepository.findByCpf(sale.getCustomer().getCpf())
                .orElseThrow(() -> new ApiException(400, "Customer does not exist", "Body"));

        Book book = bookRepository.findAll().stream()
                .filter(currentBook -> currentBook.equals(sale.getBook()))
                .findFirst()
                .orElseThrow(() -> new ApiException(400, "Book does not exist", "Body"));

        Employee employee = employeeRepository.findAll().stream()
                .filter(currentEmployee -> currentEmployee.equals(sale.getEmployee()))
                .findFirst()
                .orElseThrow(() -> new ApiException(400, "Employee does not exist", "Body"));

        sale.setCustomer(customer);
        sale.setBook(book);
        sale.setEmployee(employee);

        syncCustomerPurchases(sale);
        return saleRepository.save(sale);
    }

    private void syncCustomerPurchases(Sale sale) {
        Optional<Customer> existingCustomer = customerRepository.findByCpf(sale.getCustomer().getCpf());

        if (existingCustomer.isPresent()) {
            List<Book> books = bookRepository.findByTitle(sale.getBook().getTitle());
            if (!books.isEmpty()) {
                Book book = books.get(0);
                Customer customer = existingCustomer.get();
                if (customer.getPurchases() != null) {
                    customer.getPurchases().add(book);
                }
            }
        }
    }
}
