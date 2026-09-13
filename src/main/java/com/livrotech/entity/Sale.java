package com.livrotech.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

@Entity
@Table(name = "sales")
@Getter
@Setter
@NoArgsConstructor
public class Sale extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @jakarta.persistence.JoinColumn(nullable = false)
    private Customer customer;

    @ManyToOne(optional = false)
    @jakarta.persistence.JoinColumn(nullable = false)
    private Employee employee;

    @ManyToOne(optional = false)
    @jakarta.persistence.JoinColumn(nullable = false)
    private Book book;

    @NotNull
    @Column(nullable = false)
    @PastOrPresent
    private LocalDate saleDate = LocalDate.now();

    public Sale(Long id, Customer customer, Employee employee, Book book) {
        this.id = id;
        this.customer = customer;
        this.employee = employee;
        this.book = book;
    }
}
