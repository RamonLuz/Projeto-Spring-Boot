package com.livrotech.entity;

import java.util.Objects;
import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "books", uniqueConstraints = {
        @jakarta.persistence.UniqueConstraint(name = "uk_book_title_author", columnNames = {"title", "author"}),
        @jakarta.persistence.UniqueConstraint(name = "uk_book_isbn", columnNames = {"isbn"})
})
@Getter
@Setter
@NoArgsConstructor
public class Book extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 150)
    @Column(nullable = false, length = 150)
    private String title;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String author;

    @Size(max = 20)
    @Column(length = 20, unique = true)
    private String isbn;

    @Size(max = 80)
    @Column(length = 80)
    private String category = "Geral";

    @Size(max = 500)
    @Column(length = 500)
    private String description;

    @NotNull
    @DecimalMin("0.01")
    @Digits(integer = 10, fraction = 2)
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @NotNull
    @jakarta.validation.constraints.Min(0)
    @Column(nullable = false)
    private Integer stock = 0;

    @Column(nullable = false)
    private boolean featured = false;

    @Column(nullable = false)
    private boolean active = true;

    @jakarta.persistence.Version
    private Long version;

    public Book(Long id, String title, String author, BigDecimal price) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.price = price;
        this.stock = 0;
        this.category = "Geral";
    }

    @Override
    public int hashCode() {
        return Objects.hash(author, title);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Book other = (Book) obj;
        return Objects.equals(author, other.author) && Objects.equals(title, other.title);
    }
}