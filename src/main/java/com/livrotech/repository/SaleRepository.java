package com.livrotech.repository;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.livrotech.entity.Book;
import com.livrotech.entity.Sale;

public interface SaleRepository extends JpaRepository<Sale, Long>{

    Page<Sale> findBySaleDate(LocalDate saleDate, Pageable pageable);

    @Query("select distinct s.book from Sale s where s.customer.id = :customerId")
    java.util.List<Book> findBooksByCustomerId(@Param("customerId") Long customerId);
}
