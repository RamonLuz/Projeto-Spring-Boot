package com.livrotech.mapper;

import com.livrotech.dto.BookResponseDTO;
import com.livrotech.entity.Book;

public final class BookMapper {

    private BookMapper() {
    }

    public static BookResponseDTO toResponse(Book book) {
        return new BookResponseDTO(book.getId(), book.getTitle(), book.getAuthor(), book.getPrice());
    }
}
