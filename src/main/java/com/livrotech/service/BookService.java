package com.livrotech.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.livrotech.entity.Book;
import com.livrotech.exception.ApiException;
import com.livrotech.repository.BookRepository;

import jakarta.persistence.criteria.Predicate;

@Service
public class BookService {

    private static final Logger log = LoggerFactory.getLogger(BookService.class);
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Transactional(readOnly = true)
    public List<Book> listAll() {
        return bookRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Page<Book> listPage(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<Book> listPage(Pageable pageable, String title, String author, String category, Boolean availableOnly) {
        Specification<Book> specification = (root, query, cb) -> {
            Predicate predicate = cb.conjunction();

            if (title != null && !title.isBlank()) {
                predicate = cb.and(predicate, cb.like(cb.lower(root.get("title")), "%" + title.trim().toLowerCase() + "%"));
            }

            if (author != null && !author.isBlank()) {
                predicate = cb.and(predicate, cb.like(cb.lower(root.get("author")), "%" + author.trim().toLowerCase() + "%"));
            }

            if (category != null && !category.isBlank()) {
                predicate = cb.and(predicate, cb.equal(cb.lower(root.get("category")), category.trim().toLowerCase()));
            }

            if (Boolean.TRUE.equals(availableOnly)) {
                predicate = cb.and(predicate, cb.greaterThan(root.get("stock"), 0));
                predicate = cb.and(predicate, cb.isTrue(root.get("active")));
            }

            return predicate;
        };

        return bookRepository.findAll(specification, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Book> listPage(Pageable pageable, String title) {
        return listPage(pageable, title, null, null, false);
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
        if (book.getIsbn() != null) {
            book.setIsbn(book.getIsbn().trim());
        }
        if (book.getCategory() != null) {
            book.setCategory(book.getCategory().trim());
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

        if (book.getStock() == null || book.getStock() < 0) {
            throw new ApiException(400, "Stock cannot be negative", "stock");
        }

        if (book.getCategory() == null || book.getCategory().isBlank()) {
            book.setCategory("Geral");
        }

        if (book.getIsbn() != null && !book.getIsbn().isBlank()) {
            bookRepository.findByIsbn(book.getIsbn())
                    .ifPresent(existing -> {
                        if (!existing.getId().equals(book.getId())) {
                            throw new ApiException(409, "ISBN already registered", "isbn");
                        }
                    });
        }

        if (bookRepository.findByTitleIgnoreCaseAndAuthorIgnoreCase(book.getTitle(), book.getAuthor()).isPresent()) {
            throw new ApiException(409, "Book already exists for this title and author", "title");
        }

        Book savedBook = bookRepository.save(book);
        log.info("Book created with id {}", savedBook.getId());
        return savedBook;
    }

    @Transactional(readOnly = true)
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
        if (updatedBook.getIsbn() != null) {
            updatedBook.setIsbn(updatedBook.getIsbn().trim());
        }
        if (updatedBook.getCategory() != null) {
            updatedBook.setCategory(updatedBook.getCategory().trim());
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

        if (updatedBook.getStock() == null || updatedBook.getStock() < 0) {
            throw new ApiException(400, "Stock cannot be negative", "stock");
        }

        if (updatedBook.getCategory() == null || updatedBook.getCategory().isBlank()) {
            updatedBook.setCategory("Geral");
        }

        Optional<Book> existingBook = bookRepository.findById(id);

        if (existingBook.isPresent()) {
            Book book = existingBook.get();

            Optional<Book> duplicateBook = bookRepository
                    .findByTitleIgnoreCaseAndAuthorIgnoreCase(updatedBook.getTitle(), updatedBook.getAuthor());
            if (duplicateBook.isPresent() && !duplicateBook.get().getId().equals(id)) {
                throw new ApiException(409, "Book already exists for this title and author", "title");
            }

            if (updatedBook.getIsbn() != null && !updatedBook.getIsbn().isBlank()) {
                Optional<Book> isbnConflict = bookRepository.findByIsbn(updatedBook.getIsbn());
                if (isbnConflict.isPresent() && !isbnConflict.get().getId().equals(id)) {
                    throw new ApiException(409, "ISBN already registered", "isbn");
                }
            }

            book.setTitle(updatedBook.getTitle());
            book.setAuthor(updatedBook.getAuthor());
            book.setIsbn(updatedBook.getIsbn());
            book.setCategory(updatedBook.getCategory());
            book.setDescription(updatedBook.getDescription());
            book.setPrice(updatedBook.getPrice());
            book.setStock(updatedBook.getStock());
            book.setFeatured(updatedBook.isFeatured());
            book.setActive(updatedBook.isActive());

            bookRepository.save(book);
            log.info("Book updated with id {}", id);
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
            log.info("Book deleted with id {}", id);
            return true;
        }

        return false;
    }
}