package com.livrotech.entity;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import com.livrotech.validation.ValidCpf;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
public abstract class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    protected String name;

    @NotBlank
    @Pattern(regexp = "\\d{11}")
    @ValidCpf
    @Column(nullable = false, unique = true, length = 11)
    protected String cpf;

    @jakarta.persistence.Version
    private Long version;

    public Person(String name, String cpf) {
        this.name = name;
        this.cpf = cpf;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cpf);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Person other = (Person) obj;
        return Objects.equals(cpf, other.cpf);
    }
}
