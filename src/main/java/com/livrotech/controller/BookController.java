package com.livrotech.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.livrotech.dto.BookRequestDTO;
import com.livrotech.entity.Book;
import com.livrotech.service.BookService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/Books")
public class BookController {

    private final BookService livroService;

    public BookController(BookService livroService) {
        this.livroService = livroService;
    }

    @GetMapping
    public ResponseEntity<List<Book>> listar() {
        List<Book> livros = livroService.listar();
        return ResponseEntity.ok(livros);
    }

    @PostMapping
    public ResponseEntity<Book> salvar(@Valid @RequestBody BookRequestDTO dto) {

        Book livro = new Book();

        livro.setTitulo(dto.getTitulo());
        livro.setAutor(dto.getAutor());
        livro.setPreco(dto.getPreco());

        Book livroSalvo = livroService.salvar(livro);

        return ResponseEntity.status(HttpStatus.CREATED).body(livroSalvo);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> buscarPorId(@PathVariable Long id) {

        Optional<Book> livro = livroService.buscarPorId(id);

        if (livro.isPresent()) {
            return ResponseEntity.ok(livro.get());
        }

        return ResponseEntity.notFound().build();
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Book> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody BookRequestDTO dto) {

        Book livro = new Book();

        livro.setTitulo(dto.getTitulo());
        livro.setAutor(dto.getAutor());
        livro.setPreco(dto.getPreco());

        Optional<Book> livroAtualizado = livroService.atualizar(id, livro);

        if (livroAtualizado.isPresent()) {
            return ResponseEntity.ok(livroAtualizado.get());
        }

        return ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {

        boolean removido = livroService.deletar(id);

        if (removido) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}