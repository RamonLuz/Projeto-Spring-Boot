package com.livrotech.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BookRequestDTO {

    @NotBlank(message = "Title is required")
    @Size(max = 150, message = "Title must have at most 150 characters")
    private String title;

    @NotBlank(message = "Author is required")
    @Size(max = 100, message = "Author must have at most 100 characters")
    private String author;

    @Size(max = 20, message = "ISBN must have at most 20 characters")
    private String isbn;

    @Size(max = 80, message = "Category must have at most 80 characters")
    private String category;

    @Size(max = 500, message = "Description must have at most 500 characters")
    private String description;

    @DecimalMin(value = "0.01", message = "Price must be greater than zero")
    @Digits(integer = 10, fraction = 2, message = "Price must have at most 2 decimal places")
    @NotNull(message = "Price is required")
    private BigDecimal price;

    @jakarta.validation.constraints.Min(value = 0, message = "Stock cannot be negative")
    private Integer stock = 0;

    private boolean featured;
    private boolean active = true;
}