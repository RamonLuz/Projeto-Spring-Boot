package com.livrotech.dto;

import java.util.List;

import com.livrotech.entity.Book;
import com.livrotech.entity.Person;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CustomerRequestDTO extends Person {

    @NotBlank(message = "Status is required")
    private String status;

    private List<Book> purchases;
}
