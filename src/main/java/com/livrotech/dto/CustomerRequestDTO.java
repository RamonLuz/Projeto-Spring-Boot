package com.livrotech.dto;

import java.util.List;

import com.livrotech.entity.Person;
import com.livrotech.entity.Sale;

import jakarta.validation.constraints.NotBlank;

public class CustomerRequestDTO extends Person {

	@NotBlank(message = "Status é obrigatório")
	private String status;
	
	private List<Sale> compras;

	public CustomerRequestDTO() {}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<Sale> getCompras() {
		return compras;
	}

	public void setCompras(List<Sale> compras) {
		this.compras = compras;
	}

}
