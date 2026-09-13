package com.livrotech.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            throw new ApiException(400, "Book is invalid", "body");
        }

        if (book.getTitle() != null) {
            book.setTitle(book.getTitle().trim());
        }
        if (book.getAuthor() != null) {
            book.setAuthor(book.getAuthor().trim());
        }

        if (book.getTitle() == null || book.getTitle().isBlank()) {
            throw new ApiException(400, "Title is required", "title");
        }

        if (book.getAuthor() == null || book.getAuthor().isBlank()) {
            throw new ApiException(400, "Author is required", "author");
        }

        if (book.getPrice() == null || book.getPrice().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new ApiException(400, "Price is invalid", "price");
        }

        if (bookRepository.findByTitleIgnoreCaseAndAuthorIgnoreCase(book.getTitle(), book.getAuthor()).isPresent()) {
            throw new ApiException(409, "Book already exists for this title and author", "title");
        }

        return bookRepository.save(book);
    }

    public Optional<Book> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return bookRepository.findById(id);
    }

    @Transactional
    public Optional<Book> update(Long id, Book updatedBook) {
        if (id == null) {
            return Optional.empty();
        }

        if (updatedBook == null) {
            throw new ApiException(400, "Book is invalid", "body");
        }

        if (updatedBook.getTitle() != null) {
            updatedBook.setTitle(updatedBook.getTitle().trim());
        }
        if (updatedBook.getAuthor() != null) {
            updatedBook.setAuthor(updatedBook.getAuthor().trim());
        }

        if (updatedBook.getTitle() == null || updatedBook.getTitle().isBlank()) {
            throw new ApiException(400, "Title is required", "title");
        }

        if (updatedBook.getAuthor() == null || updatedBook.getAuthor().isBlank()) {
            throw new ApiException(400, "Author is required", "author");
        }

        if (updatedBook.getPrice() == null || updatedBook.getPrice().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new ApiException(400, "Price is invalid", "price");
        }

        Optional<Book> existingBook = bookRepository.findById(id);

        if (existingBook.isPresent()) {
            Book book = existingBook.get();

            Optional<Book> duplicateBook = bookRepository
                    .findByTitleIgnoreCaseAndAuthorIgnoreCase(updatedBook.getTitle(), updatedBook.getAuthor());
            if (duplicateBook.isPresent() && !duplicateBook.get().getId().equals(id)) {
                throw new ApiException(409, "Book already exists for this title and author", "title");
            }

            book.setTitle(updatedBook.getTitle());
            book.setAuthor(updatedBook.getAuthor());
            book.setPrice(updatedBook.getPrice());

            bookRepository.save(book);
            return Optional.of(book);
        }

        return Optional.empty();
    }

    @Transactional
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