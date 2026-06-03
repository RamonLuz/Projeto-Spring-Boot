package com.livrotech.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "Cliente")
public class Customer extends Person {

	@Column(nullable = false)
	private String status;
	
	@OneToMany
	private List<Book> compras;

	public Customer(String nome, String cpf, String status, List<Book> compras) {
		super(nome, cpf);
		this.status = status;
		this.compras = compras;
	}

	public Customer(String nome, String cpf) {
		super(nome, cpf);
	}

	public Customer() {
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<Book> getCompras() {
		return compras;
	}

	public void setCompras(List<Book> compras) {
		this.compras = compras;
	}
	

}
