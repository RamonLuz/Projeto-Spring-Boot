package com.livrotech.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.livrotech.entity.Sale;
import com.livrotech.service.SaleService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/Sales")
public class SaleController {

	private final SaleService vendaService;

	public SaleController(SaleService vendaService) {
		this.vendaService = vendaService;
	}
	
	@GetMapping
	public ResponseEntity<List<Sale>> listar() {
		List<Sale> venda = vendaService.listar();
		return ResponseEntity.ok(venda);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Sale> buscarPorId(@PathVariable Long id) {

		Optional<Sale> venda = vendaService.buscarPorId(id);

		if (venda.isPresent()) {
			return ResponseEntity.ok(venda.get());
		}

		return ResponseEntity.notFound().build();
	}
	
	@PostMapping
	public ResponseEntity<Sale> salvar(@Valid @RequestBody Sale venda) {
		
		Sale vendaSalva = vendaService.salvar(venda);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(vendaSalva);
	}
}
