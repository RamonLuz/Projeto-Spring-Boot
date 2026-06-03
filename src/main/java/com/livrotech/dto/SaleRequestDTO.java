package com.livrotech.dto;

import java.time.LocalDate;

import com.livrotech.entity.Book;
import com.livrotech.entity.Customer;
import com.livrotech.entity.Employee;

public class SaleRequestDTO {

	
	private Customer cliente;
	
	private Employee funcionario;
	
	private Book livro;
	
	private LocalDate dataVenda;

	public SaleRequestDTO() {
	}

	public Customer getCliente() {
		return cliente;
	}

	public void setCliente(Customer cliente) {
		this.cliente = cliente;
	}

	public Employee getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Employee funcionario) {
		this.funcionario = funcionario;
	}

	public Book getLivro() {
		return livro;
	}

	public void setLivro(Book livro) {
		this.livro = livro;
	}

	public LocalDate getDataVenda() {
		return dataVenda;
	}

	public void setDataVenda(LocalDate dataVenda) {
		this.dataVenda = dataVenda;
	}

}
