package com.livrotech.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.livrotech.entity.Book;
import com.livrotech.entity.ApiException;
import com.livrotech.repository.BookRepository;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @Test
    void shouldListBooks() {
        Book book = new Book(
                1L,
                "Clean Code",
                "Robert Martin",
                java.math.BigDecimal.valueOf(100.0)
        );

        when(bookRepository.findAll())
                .thenReturn(List.of(book));

        List<Book> result = bookService.listAll();

        assertEquals(1, result.size());
        assertEquals("Clean Code", result.get(0).getTitle());
    }

    @Test
    void shouldRejectBookWithInvalidPrice() {
        Book book = new Book(null, "Clean Code", "Robert Martin", BigDecimal.ZERO);

        ApiException exception = assertThrows(ApiException.class, () -> bookService.save(book));

        assertEquals(400, exception.getStatus());
        assertEquals("Price is invalid", exception.getMessage());
    }

    @Test
    void shouldRejectDuplicateBook() {
        Book book = new Book(null, "Clean Code", "Robert Martin", BigDecimal.TEN);
        when(bookRepository.findByTitleIgnoreCaseAndAuthorIgnoreCase("Clean Code", "Robert Martin"))
                .thenReturn(Optional.of(new Book(1L, "Clean Code", "Robert Martin", BigDecimal.TEN)));

        ApiException exception = assertThrows(ApiException.class, () -> bookService.save(book));

        assertEquals(409, exception.getStatus());
        verify(bookRepository).findByTitleIgnoreCaseAndAuthorIgnoreCase("Clean Code", "Robert Martin");
    }

    @Test
    void shouldTrimBookTextBeforeSaving() {
        Book book = new Book(null, "  Clean Code  ", "  Robert Martin ", BigDecimal.TEN);
        when(bookRepository.findByTitleIgnoreCaseAndAuthorIgnoreCase("Clean Code", "Robert Martin"))
                .thenReturn(Optional.empty());
        when(bookRepository.save(book)).thenReturn(book);

        Book savedBook = bookService.save(book);

        assertEquals("Clean Code", savedBook.getTitle());
        assertEquals("Robert Martin", savedBook.getAuthor());
        verify(bookRepository).save(book);
    }

    @Test
    void shouldFindBookById() {
        Book book = new Book(1L, "Clean Code", "Robert Martin", BigDecimal.TEN);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        assertEquals(Optional.of(book), bookService.findById(1L));
    }

    @Test
    void shouldReturnEmptyWhenBookIdIsNull() {
        assertEquals(Optional.empty(), bookService.findById(null));
        verify(bookRepository, never()).findById(null);
    }

    @Test
    void shouldUpdateBook() {
        Book existing = new Book(1L, "Old title", "Old author", BigDecimal.ONE);
        Book updated = new Book(null, "New title", "New author", BigDecimal.TEN);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(bookRepository.findByTitleIgnoreCaseAndAuthorIgnoreCase("New title", "New author"))
                .thenReturn(Optional.empty());

        Optional<Book> result = bookService.update(1L, updated);

        assertEquals(Optional.of(existing), result);
        assertEquals("New title", existing.getTitle());
        assertEquals(BigDecimal.TEN, existing.getPrice());
        verify(bookRepository).save(existing);
    }

    @Test
    void shouldReturnEmptyWhenUpdatingMissingBook() {
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        assertEquals(Optional.empty(), bookService.update(99L,
                new Book(null, "Title", "Author", BigDecimal.TEN)));
    }

    @Test
    void shouldDeleteBookWhenItExists() {
        when(bookRepository.existsById(1L)).thenReturn(true);

        assertEquals(true, bookService.delete(1L));

        verify(bookRepository).deleteById(1L);
    }

    @Test
    void shouldReturnFalseWhenDeletingMissingBook() {
        when(bookRepository.existsById(99L)).thenReturn(false);

        assertEquals(false, bookService.delete(99L));
        verify(bookRepository, never()).deleteById(99L);
    }
}