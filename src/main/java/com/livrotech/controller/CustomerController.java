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

import com.livrotech.dto.CustomerRequestDTO;
import com.livrotech.entity.Customer;
import com.livrotech.service.CustomerService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/Customer")
public class CustomerController {

	private final CustomerService clienteService;

	public CustomerController(CustomerService clienteService) {
		this.clienteService = clienteService;
	}

	@GetMapping
	public ResponseEntity<List<Customer>> listar() {
		List<Customer> cliente = clienteService.listar();
		
		if(cliente.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		
		return ResponseEntity.ok(cliente);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Customer> buscarPorId(@PathVariable Long id) {

		Optional<Customer> cliente = clienteService.buscarPorId(id);

		if (cliente.isPresent()) {
			return ResponseEntity.ok(cliente.get());
		}

		return ResponseEntity.notFound().build();
	}

	@PostMapping
	public ResponseEntity<Customer> salvar(@Valid @RequestBody CustomerRequestDTO dto) {

		Customer cliente = new Customer();

		cliente.setNome(dto.getNome());
	    cliente.setCpf(dto.getCpf());
		cliente.setStatus(dto.getStatus());

		Customer clienteSalvo = clienteService.salvar(cliente);

		return ResponseEntity.status(HttpStatus.CREATED).body(clienteSalvo);
	}
	
	 @PutMapping("/{id}")
	    public ResponseEntity<Customer> atualizar(
	            @PathVariable Long id,
	            @Valid @RequestBody CustomerRequestDTO dto) {

	        Customer cliente = new Customer();

	        cliente.setStatus(dto.getStatus());

	        Optional<Customer> clienteAtualizado = clienteService.atualizar(id, cliente);

	        if (clienteAtualizado.isPresent()) {
	            return ResponseEntity.ok(clienteAtualizado.get());
	        }

	        return ResponseEntity.notFound().build();
	    }
	    
	    @DeleteMapping("/{id}")
	    public ResponseEntity<Void> deletar(@PathVariable Long id) {

	        boolean removido = clienteService.deletar(id);

	        if (removido) {
	            return ResponseEntity.noContent().build();
	        }

	        return ResponseEntity.notFound().build();
	    }

}
