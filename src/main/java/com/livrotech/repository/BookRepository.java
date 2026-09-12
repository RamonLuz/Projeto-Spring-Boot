package com.livrotech.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

import com.livrotech.entity.Book;

public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByTitleIgnoreCaseAndAuthorIgnoreCase(String title, String author);
}