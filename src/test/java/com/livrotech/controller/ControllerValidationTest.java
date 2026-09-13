package com.livrotech.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.ActiveProfiles;

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
@ActiveProfiles("test")
@TestPropertySource(properties = "app.security.enabled=false")
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
    void shouldRejectCpfWithInvalidCheckDigits() throws Exception {
        mockMvc.perform(post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "name": "Ana",
                          "cpf": "12345678901",
                          "status": "ACTIVE"
                        }
                        """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.field").value("cpf"));
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
    void shouldPatchCustomerStatus() throws Exception {
        Customer customer = new Customer("Ana", "12345678901", Status.INACTIVE, new ArrayList<>());
        customer.setId(1L);
        when(customerService.update(eq(1L), any(Customer.class))).thenReturn(Optional.of(customer));

        mockMvc.perform(patch("/customers/1")
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
    void shouldDifferentiateMalformedJson() throws Exception {
        mockMvc.perform(post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.field").value("body"))
                .andExpect(jsonPath("$.message").value("JSON malformado"));
    }

    @Test
    void shouldIdentifyInvalidEnumValue() throws Exception {
        mockMvc.perform(put("/customers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "status": "UNKNOWN"
                        }
                        """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.field").value("status"))
                .andExpect(jsonPath("$.message").value("Valor inválido para o campo 'status'"));
    }

    @Test
    void shouldIdentifyMissingBody() throws Exception {
        mockMvc.perform(put("/employees/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.field").value("body"))
                .andExpect(jsonPath("$.message").value("Body da requisição é obrigatório"));
    }

    @Test
    void shouldExplainRelatedBookDeletionFailure() throws Exception {
        when(bookService.delete(1L)).thenThrow(new DataIntegrityViolationException(
                "integrity error",
                new RuntimeException("FK_SALES_BOOK")
        ));

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete("/books/1"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.field").value("bookId"))
                .andExpect(jsonPath("$.message")
                        .value("Não é possível remover um livro que possui vendas"));
    }

    @Test
    void shouldReturnConflictForOptimisticLockingFailure() throws Exception {
        when(bookService.delete(1L)).thenThrow(new ObjectOptimisticLockingFailureException(Book.class, 1L));

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete("/books/1"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.field").value("data"))
                .andExpect(jsonPath("$.message")
                        .value("O registro foi alterado por outro usuário. Tente novamente"));
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

    @Test
    void shouldPaginateAndFilterBooks() throws Exception {
        Book book = new Book(1L, "Java Basics", "Author", BigDecimal.TEN);
        when(bookService.listPage(any(Pageable.class), eq("java")))
                .thenReturn(new PageImpl<>(List.of(book), PageRequest.of(1, 1), 3));

        mockMvc.perform(get("/books")
                .param("title", "java")
                .param("page", "1")
                .param("size", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number").value(1))
                .andExpect(jsonPath("$.size").value(1))
                .andExpect(jsonPath("$.totalElements").value(3))
                .andExpect(jsonPath("$.content[0].title").value("Java Basics"));
    }

    @Test
    void shouldFilterCustomersByName() throws Exception {
        Customer customer = new Customer("Ana", "12345678901", Status.ACTIVE, new ArrayList<>());
        customer.setId(1L);
        when(customerService.listPage(any(Pageable.class), eq("ana")))
                .thenReturn(new PageImpl<>(List.of(customer)));

        mockMvc.perform(get("/customers").param("name", "ana"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Ana"));
    }

    @Test
    void shouldFilterEmployeesByName() throws Exception {
        Employee employee = new Employee("Joao", "10987654321", 10, "Seller", Status.ACTIVE);
        employee.setId(1L);
        when(employeeService.listPage(any(Pageable.class), eq("joao")))
                .thenReturn(new PageImpl<>(List.of(employee)));

        mockMvc.perform(get("/employees").param("name", "joao"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Joao"));
    }

    @Test
    void shouldFilterSalesByDate() throws Exception {
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
        when(saleService.listPage(any(Pageable.class), eq(LocalDate.of(2026, 9, 13))))
                .thenReturn(new PageImpl<>(List.of(sale)));

        mockMvc.perform(get("/sales").param("date", "2026-09-13"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(4));
    }

    private record SaleRequestBody(Long customerId, Long employeeId, Long bookId) {
    }
}
