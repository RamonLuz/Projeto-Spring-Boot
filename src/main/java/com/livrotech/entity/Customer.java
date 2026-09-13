package com.livrotech.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "customers")
@Getter
@Setter
@NoArgsConstructor
public class Customer extends Person {

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Transient
    private List<Book> purchases = new ArrayList<>();

    public Customer(String name, String cpf, Status status, List<Book> purchases) {
        super(name, cpf);
        this.status = status;
        this.purchases = purchases;
    }

    public Customer(String name, String cpf) {
        super(name, cpf);
    }
}
