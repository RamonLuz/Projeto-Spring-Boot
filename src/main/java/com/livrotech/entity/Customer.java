package com.livrotech.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "customers")
@Getter
@Setter
@NoArgsConstructor
public class Customer extends Person {

    @Column(nullable = false)
    private String status;

    @OneToMany
    private List<Book> purchases;

    public Customer(String name, String cpf, String status, List<Book> purchases) {
        super(name, cpf);
        this.status = status;
        this.purchases = purchases;
    }

    public Customer(String name, String cpf) {
        super(name, cpf);
    }
}
