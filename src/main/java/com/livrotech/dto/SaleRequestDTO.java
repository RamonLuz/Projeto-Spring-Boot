package com.livrotech.dto;

import java.time.LocalDate;

import com.livrotech.entity.Book;
import com.livrotech.entity.Customer;
import com.livrotech.entity.Employee;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SaleRequestDTO {

    private Customer customer;

    private Employee employee;

    private Book book;

    private LocalDate saleDate;
}
