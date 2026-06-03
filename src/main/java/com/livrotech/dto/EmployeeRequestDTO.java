package com.livrotech.dto;

import com.livrotech.entity.Person;

import jakarta.validation.constraints.NotBlank;

public class EmployeeRequestDTO extends Person {

	private int matricula;
	
	@NotBlank(message = "Cargo é obrigatório")
	private String cargo;
	
	@NotBlank(message = "Status é obrigatório")
	private String status;

	public EmployeeRequestDTO() {
	}

	public int getMatricula() {
		return matricula;
	}

	public void setMatricula(int matricula) {
		this.matricula = matricula;
	}

	public String getCargo() {
		return cargo;
	}

	public void setCargo(String cargo) {
		this.cargo = cargo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
