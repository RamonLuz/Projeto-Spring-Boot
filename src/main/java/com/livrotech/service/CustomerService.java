package com.livrotech.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.livrotech.entity.ApiException;
import com.livrotech.entity.Customer;
import com.livrotech.repository.CustomerRepository;

@Service
public class CustomerService {

	private final CustomerRepository clienteRepository;

	public CustomerService(CustomerRepository clienteRepository) {
		this.clienteRepository = clienteRepository;
		
	}

	public Customer salvar(Customer cliente) {
		
		Optional<Customer> clienteExiste = clienteRepository.findByCpf(cliente.getCpf());
		
		if(clienteExiste.isPresent()) {
			Customer clienteExistente = clienteExiste.get();
			if(clienteExistente.equals(cliente)) {
				throw new ApiException(400, "Cpf ja cadastrado", "CPF");
			}
		}
		if(cliente.getCpf() == null ||cliente.getCpf().length() < 11) {
			throw new ApiException(400, "Cpf deve ter 11 digitos", "CPF");
		}

		return clienteRepository.save(cliente);
	}

	public List<Customer> listar() {
		return clienteRepository.findAll();
	}

	public Optional<Customer> buscarPorId(Long id) {
		return clienteRepository.findById(id);
	}
	
	public Optional<Customer> buscarPorCpf(String cpf) {
		return clienteRepository.findByCpf(cpf);
	}
	
	public Optional<Customer> atualizar(Long id, Customer clienteAtualizado) {

		Optional<Customer> clienteExistente = clienteRepository.findById(id);

		if (clienteExistente.isPresent()) {

			Customer cliente = clienteExistente.get();

			cliente.setStatus(clienteAtualizado.getStatus());

			clienteRepository.save(cliente);

			return Optional.of(cliente);
		}

		return Optional.empty();
	}

	public boolean deletar(Long id) {

		if (clienteRepository.existsById(id)) {
			clienteRepository.deleteById(id);
			return true;
		}

		return false;
	}
}