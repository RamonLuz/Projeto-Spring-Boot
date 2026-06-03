package com.livrotech.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.livrotech.entity.ApiException;
import com.livrotech.entity.Book;
import com.livrotech.entity.Customer;
import com.livrotech.entity.Employee;
import com.livrotech.entity.Sale;
import com.livrotech.repository.BookRepository;
import com.livrotech.repository.CustomerRepository;
import com.livrotech.repository.EmployeeRepository;
import com.livrotech.repository.SaleRepository;

@Service
public class SaleService {

	private final CustomerRepository clienteRepository;
	private final EmployeeRepository funcionarioRepository;
	private final BookRepository livroRepository;
	private final SaleRepository vendaRepository;

	public SaleService(SaleRepository vendaRepository, CustomerRepository customeRepository, BookRepository livroRepository, EmployeeRepository funcionarioRepository) {
		this.funcionarioRepository = funcionarioRepository;
		this.livroRepository = livroRepository;
		this.vendaRepository = vendaRepository;
		this.clienteRepository = customeRepository;
	}

	public List<Sale> listar() {
		return vendaRepository.findAll();
	}

	public Optional<Sale> buscarPorId(Long id) {
		return vendaRepository.findById(id);
	}

	public Sale salvar(Sale venda) {
		
		List<Book> existeLivro = livroRepository.findAll();
		List<Customer> existeCliente = clienteRepository.findAll();
		List<Employee> existeFuncionario = funcionarioRepository.findAll();
		
		if(!existeLivro.contains(venda.getLivro())) {
			throw new ApiException(400, "Livro não existe", "Body");
		}
		if(!existeCliente.contains(venda.getCliente())) {
			throw new ApiException(400, "Cliente não existe", "Body");
		}
		if(!existeFuncionario.contains(venda.getFuncionario())) {
			throw new ApiException(400, "Funcionario não existe", "Body");
		}
		
		sincronizarCliente(venda);
		Sale vendaSalva = vendaRepository.save(venda);

		return vendaSalva;
	}
	
	private void sincronizarCliente(Sale venda) {
		Optional<Customer> clienteExiste = clienteRepository.findByCpf(venda.getCliente().getCpf());

		if (clienteExiste.isPresent()) {

			Customer cliente = clienteExiste.get();
			cliente.getCompras().add(venda.getLivro());

		}
		
	}

}
