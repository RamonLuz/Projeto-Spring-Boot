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

	public SaleService(SaleRepository vendaRepository, CustomerRepository customeRepository,
			BookRepository livroRepository, EmployeeRepository funcionarioRepository) {
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

		Customer cliente = clienteRepository.findByCpf(venda.getCliente().getCpf())
				.orElseThrow(() -> new ApiException(400, "Cliente não existe", "Body"));

		Book livro = livroRepository.findAll().stream().filter(l -> l.equals(venda.getLivro())).findFirst()
				.orElseThrow(() -> new ApiException(400, "Livro não existe", "Body"));

		Employee funcionario = funcionarioRepository.findAll().stream().filter(f -> f.equals(venda.getFuncionario()))
				.findFirst().orElseThrow(() -> new ApiException(400, "Funcionario não existe", "Body"));

		venda.setCliente(cliente);
		venda.setLivro(livro);
		venda.setFuncionario(funcionario);

		sincronizarCliente(venda);

		return vendaRepository.save(venda);
	}

	private void sincronizarCliente(Sale venda) {
		Optional<Customer> clienteExiste = clienteRepository.findByCpf(venda.getCliente().getCpf());

		List<Book> livros = livroRepository.findByTitulo(venda.getLivro().getTitulo());

		Book livro = livros.get(0);

		if (clienteExiste.isPresent()) {

			Customer cliente = clienteExiste.get();

			cliente.getCompras().add(livro);

		}

	}

}
