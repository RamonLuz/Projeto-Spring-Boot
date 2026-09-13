package com.livrotech.mapper;

import com.livrotech.dto.SaleResponseDTO;
import com.livrotech.dto.SaleRequestDTO;
import com.livrotech.entity.Book;
import com.livrotech.entity.Customer;
import com.livrotech.entity.Employee;
import com.livrotech.entity.Sale;

public final class SaleMapper {

    private SaleMapper() {
    }

    public static SaleResponseDTO toResponse(Sale sale) {
        return new SaleResponseDTO(
                sale.getId(),
                sale.getCustomer().getId(),
                sale.getCustomer().getCpf(),
                sale.getEmployee().getId(),
                sale.getBook().getId(),
                sale.getSaleDate()
        );
    }

    public static Sale toEntity(SaleRequestDTO dto) {
        Customer customer = new Customer();
        if (dto.getCustomerId() != null) {
            customer.setId(dto.getCustomerId());
        } else {
            customer.setCpf(dto.getCustomerCpf());
        }

        Employee employee = new Employee();
        employee.setId(dto.getEmployeeId());

        Book book = new Book();
        book.setId(dto.getBookId());

        Sale sale = new Sale();
        sale.setCustomer(customer);
        sale.setEmployee(employee);
        sale.setBook(book);
        sale.setSaleDate(dto.getSaleDate());
        return sale;
    }
}
