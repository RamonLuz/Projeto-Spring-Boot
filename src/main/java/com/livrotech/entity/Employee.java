package com.livrotech.entity;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "employees")
@Getter
@Setter
@NoArgsConstructor
public class Employee extends Person {

    @Column(nullable = false, unique = true)
    private Integer registrationNumber;

    @Column(nullable = false, length = 80)
    private String position;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    public Employee(String name, String cpf, Integer registrationNumber, String position, Status status) {
        super(name, cpf);
        this.registrationNumber = registrationNumber;
        this.position = position;
        this.status = status;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(registrationNumber);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Employee other = (Employee) obj;
        return Objects.equals(registrationNumber, other.registrationNumber);
    }
}
