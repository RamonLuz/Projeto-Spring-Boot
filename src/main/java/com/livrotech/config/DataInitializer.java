package com.livrotech.config;

import java.math.BigDecimal;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.livrotech.entity.Book;
import com.livrotech.repository.BookRepository;

@Configuration
public class DataInitializer {

    @Bean
    @ConditionalOnProperty(name = "app.seed-data.enabled", havingValue = "true")
    CommandLineRunner seedData(BookRepository bookRepository) {
        return args -> {
            if (bookRepository.count() == 0) {
                bookRepository.save(new Book(null, "O Pequeno Principe", "Antoine de Saint-Exupery",
                        new BigDecimal("29.90")));
                bookRepository.save(new Book(null, "Dom Casmurro", "Machado de Assis",
                        new BigDecimal("24.90")));
            }
        };
    }
}
