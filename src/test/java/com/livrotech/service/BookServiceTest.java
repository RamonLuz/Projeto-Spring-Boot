package com.livrotech.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.livrotech.entity.Book;
import com.livrotech.repository.BookRepository;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository livroRepository;

    @InjectMocks
    private BookService livroService;

    @Test
    void deveListarLivros() {

        // Arrange
        Book livro = new Book(
                1L,
                "Clean Code",
                "Robert Martin",
                100.0
        );

        when(livroRepository.findAll())
                .thenReturn(List.of(livro));

        // Act
        List<Book> resultado = livroService.listar();

        // Assert
        assertEquals(1, resultado.size());
        assertEquals("Clean Code", resultado.get(0).getTitulo());
    }
}