package com.livrotech.mapper;

import com.livrotech.dto.BookResponseDTO;
import com.livrotech.dto.BookRequestDTO;
import com.livrotech.entity.Book;

public final class BookMapper {

    private BookMapper() {
    }

    public static BookResponseDTO toResponse(Book book) {
        return new BookResponseDTO(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getIsbn(),
                book.getCategory(),
                book.getDescription(),
                book.getPrice(),
                book.getStock(),
                book.isFeatured(),
                book.isActive());
    }

    public static Book toEntity(BookRequestDTO dto) {
        Book book = new Book(null, dto.getTitle(), dto.getAuthor(), dto.getPrice());
        book.setIsbn(dto.getIsbn());
        book.setCategory(dto.getCategory() == null || dto.getCategory().isBlank() ? "Geral" : dto.getCategory().trim());
        book.setDescription(dto.getDescription());
        book.setStock(dto.getStock() == null ? 0 : dto.getStock());
        book.setFeatured(dto.isFeatured());
        book.setActive(dto.isActive());
        return book;
    }
}
