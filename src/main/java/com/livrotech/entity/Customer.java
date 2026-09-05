package com.livrotech.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "customers")
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

    public Customer() {
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
