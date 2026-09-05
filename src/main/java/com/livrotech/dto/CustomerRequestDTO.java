package com.livrotech.dto;

import java.util.List;

import com.livrotech.entity.Book;
import com.livrotech.entity.Person;

import jakarta.validation.constraints.NotBlank;

public class CustomerRequestDTO extends Person {

    @NotBlank(message = "Status is required")
    private String status;

    private List<Book> purchases;

    public CustomerRequestDTO() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Book> getPurchases() {
        return purchases;
    }

    public void setPurchases(List<Book> purchases) {
        this.purchases = purchases;
    }
}
