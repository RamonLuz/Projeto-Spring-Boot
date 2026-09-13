package com.livrotech.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.livrotech.entity.Book;
import com.livrotech.entity.Customer;
import com.livrotech.entity.Employee;
import com.livrotech.entity.Sale;
import com.livrotech.entity.Status;
import com.livrotech.service.BookService;
import com.livrotech.service.CustomerService;
import com.livrotech.service.EmployeeService;
import com.livrotech.service.SaleService;

@WebMvcTest({
        BookController.class,
        CustomerController.class,
        EmployeeController.class,
        SaleController.class
})
class ControllerValidationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookService bookService;

    @MockBean
    private CustomerService customerService;

    @MockBean
    private EmployeeService employeeService;

    @MockBean
    private SaleService saleService;

    @Test
    void shouldRejectBookWithInvalidRequest() throws Exception {
        mockMvc.perform(post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "title": "",
                          "author": "",
                          "price": null
                        }
                        """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.length()").value(3));
    }

    @Test
    void shouldUpdateCustomerStatus() throws Exception {
        Customer customer = new Customer("Ana", "12345678901", Status.INACTIVE, new ArrayList<>());
        customer.setId(1L);
        when(customerService.update(eq(1L), any(Customer.class))).thenReturn(Optional.of(customer));

        mockMvc.perform(put("/customers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "status": "INACTIVE"
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("INACTIVE"));
    }

    @Test
    void shouldUpdateEmployeeStatus() throws Exception {
        Employee employee = new Employee("Joao", "10987654321", 10, "Seller", Status.INACTIVE);
        employee.setId(1L);
        when(employeeService.update(eq(1L), any(Employee.class))).thenReturn(Optional.of(employee));

        mockMvc.perform(put("/employees/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "status": "INACTIVE"
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("INACTIVE"));
    }

    @Test
    void shouldCreateSale() throws Exception {
        Customer customer = new Customer("Ana", "12345678901", Status.ACTIVE, new ArrayList<>());
        customer.setId(1L);
        Employee employee = new Employee("Joao", "10987654321", 10, "Seller", Status.ACTIVE);
        employee.setId(2L);
        Book book = new Book(3L, "Book", "Author", BigDecimal.TEN);

        Sale sale = new Sale();
        sale.setId(4L);
        sale.setCustomer(customer);
        sale.setEmployee(employee);
        sale.setBook(book);
        sale.setSaleDate(LocalDate.of(2026, 9, 13));
        when(saleService.save(any(Sale.class))).thenReturn(sale);

        mockMvc.perform(post("/sales")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new SaleRequestBody(1L, 2L, 3L))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(4))
                .andExpect(jsonPath("$.customerId").value(1))
                .andExpect(jsonPath("$.employeeId").value(2))
                .andExpect(jsonPath("$.bookId").value(3));
    }

    private record SaleRequestBody(Long customerId, Long employeeId, Long bookId) {
    }
}
