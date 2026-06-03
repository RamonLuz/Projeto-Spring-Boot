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
import com.livrotech.dto.EmployeeRequestDTO;
import com.livrotech.entity.Customer;
import com.livrotech.entity.Employee;
import com.livrotech.service.EmployeeService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/Employee")
public class EmployeeController {

	private final EmployeeService funcionarioService;

	public EmployeeController(EmployeeService funcionarioService) {
		this.funcionarioService = funcionarioService;
	}

	@GetMapping
	public ResponseEntity<List<Employee>> listar() {
		List<Employee> funcionario = funcionarioService.listar();
		return ResponseEntity.ok(funcionario);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Employee> buscarPorId(@PathVariable Long id) {

		Optional<Employee> funcionario = funcionarioService.buscarPorId(id);

		if (funcionario.isPresent()) {
			return ResponseEntity.ok(funcionario.get());
		}

		return ResponseEntity.notFound().build();
	}

	@PostMapping
	public ResponseEntity<Employee> salvar(@Valid @RequestBody EmployeeRequestDTO dto) {

		Employee funcionario = new Employee();

		funcionario.setNome(dto.getNome());
		funcionario.setCpf(dto.getCpf());
		funcionario.setMatricula(dto.getMatricula());
		funcionario.setCargo(dto.getCargo());
		funcionario.setStatus(dto.getStatus());

		Employee funcionarioSalvo = funcionarioService.salvar(funcionario);

		return ResponseEntity.status(HttpStatus.CREATED).body(funcionarioSalvo);
	}
	
	 @PutMapping("/{id}")
	    public ResponseEntity<Employee> atualizar(
	            @PathVariable Long id,
	            @Valid @RequestBody EmployeeRequestDTO dto) {

	        Employee funcionario = new Employee();

	        funcionario.setStatus(dto.getStatus());

	        Optional<Employee> funcionarioAtualizado = funcionarioService.atualizar(id, funcionario);

	        if (funcionarioAtualizado.isPresent()) {
	            return ResponseEntity.ok(funcionarioAtualizado.get());
	        }

	        return ResponseEntity.notFound().build();
	    }
	    
	    @DeleteMapping("/{id}")
	    public ResponseEntity<Void> deletar(@PathVariable Long id) {

	        boolean removido = funcionarioService.deletar(id);

	        if (removido) {
	            return ResponseEntity.noContent().build();
	        }

	        return ResponseEntity.notFound().build();
	    }
}
