package com.livrotech.controller;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.livrotech.dto.BookRequestDTO;
import com.livrotech.dto.BookResponseDTO;
import com.livrotech.entity.Book;
import com.livrotech.mapper.BookMapper;
import com.livrotech.service.BookService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;

@RestController
@Validated
@RequestMapping("/books")
@Tag(name = "Livros", description = "Operacoes de livros")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    @Operation(summary = "Lista livros", description = "Lista livros com paginacao e filtro opcional por titulo.")
    public ResponseEntity<Page<BookResponseDTO>> getAll(
            @ParameterObject @Parameter(description = "Use page, size e sort=campo,asc|desc") @PageableDefault(size = 20, sort = "id") Pageable pageable,
            @RequestParam(required = false) String title) {
        return ResponseEntity.ok(bookService.listPage(pageable, title).map(BookMapper::toResponse));
    }

    @PostMapping
    @Operation(summary = "Cadastra livro")
    public ResponseEntity<BookResponseDTO> create(@Valid @RequestBody BookRequestDTO dto) {
        Book savedBook = bookService.save(BookMapper.toEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(BookMapper.toResponse(savedBook));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca livro por ID")
    public ResponseEntity<BookResponseDTO> getById(@PathVariable @Positive Long id) {
        Optional<Book> book = bookService.findById(id);

        if (book.isPresent()) {
            return ResponseEntity.ok(BookMapper.toResponse(book.get()));
        }

        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza livro")
    public ResponseEntity<BookResponseDTO> update(@PathVariable @Positive Long id, @Valid @RequestBody BookRequestDTO dto) {
        Optional<Book> updatedBook = bookService.update(id, BookMapper.toEntity(dto));

        if (updatedBook.isPresent()) {
            return ResponseEntity.ok(BookMapper.toResponse(updatedBook.get()));
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove livro")
    public ResponseEntity<Void> delete(@PathVariable @Positive Long id) {
        boolean removed = bookService.delete(id);

        if (removed) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}