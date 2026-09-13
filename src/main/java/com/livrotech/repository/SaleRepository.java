package com.livrotech.repository;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.livrotech.entity.Sale;

public interface SaleRepository extends JpaRepository<Sale, Long>{

    Page<Sale> findBySaleDate(LocalDate saleDate, Pageable pageable);
}
