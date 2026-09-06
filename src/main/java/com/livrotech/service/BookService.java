package com.livrotech.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.livrotech.entity.ApiException;
import com.livrotech.entity.Book;
import com.livrotech.repository.BookRepository;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> listAll() {
        return bookRepository.findAll();
    }

    public Book save(Book book) {
        if (book == null) {
            throw new ApiException(400, "Book is invalid", "Body");
        }

        if (book.getTitle() == null || book.getTitle().isBlank()) {
            throw new ApiException(400, "Title is required", "Body");
        }

        if (book.getAuthor() == null || book.getAuthor().isBlank()) {
            throw new ApiException(400, "Author is required", "Body");
        }

        if (book.getPrice() == null || book.getPrice() <= 0) {
            throw new ApiException(400, "Price is invalid", "Body");
        }

        List<Book> books = bookRepository.findAll();
        boolean alreadyExists = books.stream()
                .anyMatch(existingBook -> existingBook.getTitle().equalsIgnoreCase(book.getTitle())
                        && existingBook.getAuthor().equalsIgnoreCase(book.getAuthor()));

        if (alreadyExists) {
            throw new ApiException(400, "Book already exists for this title and author", "Body");
        }

        return bookRepository.save(book);
    }

    public Optional<Book> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return bookRepository.findById(id);
    }

    public Optional<Book> update(Long id, Book updatedBook) {
        if (id == null) {
            return Optional.empty();
        }

        if (updatedBook == null) {
            throw new ApiException(400, "Book is invalid", "Body");
        }

        if (updatedBook.getTitle() == null || updatedBook.getTitle().isBlank()) {
            throw new ApiException(400, "Title is required", "Body");
        }

        if (updatedBook.getAuthor() == null || updatedBook.getAuthor().isBlank()) {
            throw new ApiException(400, "Author is required", "Body");
        }

        if (updatedBook.getPrice() == null || updatedBook.getPrice() <= 0) {
            throw new ApiException(400, "Price is invalid", "Body");
        }

        Optional<Book> existingBook = bookRepository.findById(id);

        if (existingBook.isPresent()) {
            Book book = existingBook.get();
            book.setTitle(updatedBook.getTitle());
            book.setAuthor(updatedBook.getAuthor());
            book.setPrice(updatedBook.getPrice());

            bookRepository.save(book);
            return Optional.of(book);
        }

        return Optional.empty();
    }

    public boolean delete(Long id) {
        if (id == null) {
            return false;
        }

        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
            return true;
        }

        return false;
    }
}