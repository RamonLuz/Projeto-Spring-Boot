package com.livrotech.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.livrotech.entity.ApiException;
import com.livrotech.entity.Customer;
import com.livrotech.entity.Employee;
import com.livrotech.repository.EmployeeRepository;

@Service
public class EmployeeService {

	private final EmployeeRepository funcionarioRepository;

	public EmployeeService(EmployeeRepository funcionarioRepository) {
		this.funcionarioRepository = funcionarioRepository;
	}

	public Employee salvar(Employee funcionario) {

		if (funcionario.getCpf() == null || funcionario.getCpf().length() < 11) {
			throw new ApiException(400, "Cpf deve ter 11 digitos", "CPF");
		}

		return funcionarioRepository.save(funcionario);
	}

	public List<Employee> listar() {
		return funcionarioRepository.findAll();
	}

	public Optional<Employee> buscarPorId(Long id) {
		return funcionarioRepository.findById(id);
	}
	
	public Optional<Employee> atualizar(Long id, Employee funcionarioAtualizado) {

		Optional<Employee> funcionarioExistente = funcionarioRepository.findById(id);

		if (funcionarioExistente.isPresent()) {

			Employee funcionario = funcionarioExistente.get();

			funcionario.setStatus(funcionarioAtualizado.getStatus());

			funcionarioRepository.save(funcionario);

			return Optional.of(funcionario);
		}

		return Optional.empty();
	}

	public boolean deletar(Long id) {

		if (funcionarioRepository.existsById(id)) {
			funcionarioRepository.deleteById(id);
			return true;
		}

		return false;
	}
}
