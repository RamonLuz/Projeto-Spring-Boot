package com.livrotech.entity;

import java.time.LocalDate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Venda")
public class Sale {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	private Customer cliente;

	@ManyToOne
	private Employee funcionario;

	@ManyToOne
	private Book livro;

	@Column(nullable = false)
	private LocalDate dataVenda = LocalDate.now();

	public Sale() {
	}

	public Sale(Long id, Customer cliente, Employee funcionario, Book livro) {
		this.id = id;
		this.cliente = cliente;
		this.funcionario = funcionario;
		this.livro = livro;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
