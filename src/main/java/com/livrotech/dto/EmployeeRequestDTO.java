package com.livrotech.dto;

import com.livrotech.entity.Person;

import jakarta.validation.constraints.NotBlank;

public class EmployeeRequestDTO extends Person {

    private int registrationNumber;

    @NotBlank(message = "Position is required")
    private String position;

    @NotBlank(message = "Status is required")
    private String status;

    public EmployeeRequestDTO() {
    }

    public int getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(int registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
