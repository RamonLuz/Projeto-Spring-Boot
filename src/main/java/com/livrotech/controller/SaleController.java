package com.livrotech.controller;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.livrotech.dto.SaleRequestDTO;
import com.livrotech.dto.SaleResponseDTO;
import com.livrotech.entity.Book;
import com.livrotech.entity.Customer;
import com.livrotech.entity.Employee;
import com.livrotech.entity.Sale;
import com.livrotech.mapper.SaleMapper;
import com.livrotech.service.SaleService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;

@RestController
@Validated
@RequestMapping("/sales")
@Tag(name = "Vendas", description = "Operacoes de vendas")
public class SaleController {

    private final SaleService saleService;

    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    @GetMapping
    @Operation(summary = "Lista vendas", description = "Lista vendas com paginacao e filtro opcional por data.")
    public ResponseEntity<Page<SaleResponseDTO>> getAll(
            @PageableDefault(size = 20, sort = "id") Pageable pageable,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) java.time.LocalDate date) {
        return ResponseEntity.ok(saleService.listPage(pageable, date).map(SaleMapper::toResponse));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca venda por ID")
    public ResponseEntity<SaleResponseDTO> getById(@PathVariable @Positive Long id) {
        Optional<Sale> sale = saleService.findById(id);

        if (sale.isPresent()) {
            return ResponseEntity.ok(SaleMapper.toResponse(sale.get()));
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(summary = "Registra venda")
    public ResponseEntity<SaleResponseDTO> create(@Valid @RequestBody SaleRequestDTO dto) {
        Sale savedSale = saleService.save(SaleMapper.toEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(SaleMapper.toResponse(savedSale));
    }
}
