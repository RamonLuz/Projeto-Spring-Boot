# 📚 Livrotech API

API REST desenvolvida em **Java + Spring Boot** para gerenciamento de livros, clientes, funcionários e vendas.

O projeto foi criado com o objetivo de praticar conceitos de desenvolvimento backend utilizando Spring Boot, arquitetura em camadas e persistência de dados com JPA.

---

## 🚀 Tecnologias utilizadas

* Java 21
* Spring Boot
* Spring Web
* Spring Data JPA
* Bean Validation
* H2 Database
* Maven

---

## 📂 Estrutura do Projeto

```
src
└── main
    ├── java
    │   └── com.livrotech
    │       ├── controller
    │       ├── dto
    │       ├── entity
    │       ├── exception
    │       ├── repository
    │       ├── service
    │       └── LivrotechApplication
    └── resources
        └── application.yml
```

---

## 📖 Funcionalidades

### Livros

* Cadastrar livro
* Listar livros
* Buscar livro por ID
* Atualizar livro
* Remover livro

### Clientes

* Cadastrar cliente
* Listar clientes
* Buscar cliente por ID
* Atualizar status do cliente
* Remover cliente

### Funcionários

* Cadastrar funcionário
* Listar funcionários
* Buscar funcionário por ID

### Vendas

* Registrar venda
* Listar vendas
* Buscar venda por ID
* Associar livros comprados ao cliente

---

## 🏛️ Arquitetura

O projeto segue uma arquitetura em camadas:

```
Controller
    ↓
Service
    ↓
Repository
    ↓
Banco de Dados
```

---

## 🔗 Relacionamentos

* Um cliente pode possuir vários livros comprados.
* Uma venda possui:

  * Cliente
  * Funcionário
  * Livro
  * Data da venda

---

## ⚠️ Tratamento de exceções

A aplicação utiliza um tratamento global de exceções através do:

```java
@ControllerAdvice
```

Fornecendo respostas padronizadas para:

* Campos obrigatórios não preenchidos;
* Body da requisição ausente;
* Tipos inválidos;
* Regras de negócio da aplicação.

Exemplo:

```json
{
  "status": 400,
  "message": "Cpf deve ter 11 digitos",
  "field": "CPF"
}
```

---

## 🗄️ Banco de dados

O projeto utiliza banco de dados em memória H2.

### Configuração

```yaml
spring:
  datasource:
    url: jdbc:h2:mem:livrotech
    driver-class-name: org.h2.Driver
    username: sa
    password:
```

  As configurações são separadas por ambiente nos perfis `dev`, `test` e `prod`.
  O perfil `dev` é usado por padrão. Para iniciar com outro perfil:

  ```bash
  ./mvnw spring-boot:run -Dspring-boot.run.profiles=test
  ./mvnw spring-boot:run -Dspring-boot.run.profiles=prod
  ```

### Console H2

  Disponível somente no perfil `dev`:

```
http://localhost:8080/h2-console
```

---

## 📌 Endpoints

### Livros

| Método | Endpoint    |
| ------ | ----------- |
| GET    | /Books      |
| GET    | /Books/{id} |
| POST   | /Books      |
| PUT    | /Books/{id} |
| DELETE | /Books/{id} |

---

### Clientes

| Método | Endpoint       |
| ------ | -------------- |
| GET    | /Customer      |
| GET    | /Customer/{id} |
| POST   | /Customer      |
| PUT    | /Customer/{id} |
| DELETE | /Customer/{id} |

---

### Funcionários

| Método | Endpoint       |
| ------ | -------------- |
| GET    | /Employee      |
| GET    | /Employee/{id} |
| POST   | /Employee      |

---

### Vendas

| Método | Endpoint    |
| ------ | ----------- |
| GET    | /Sales      |
| GET    | /Sales/{id} |
| POST   | /Sales      |

---

## ▶️ Executando o projeto

Clone o repositório:

```bash
git clone https://github.com/RamonLuz/Projeto-Spring-Boot.git
```

Entre na pasta:

```bash
cd livrotech
```

Execute:

```bash
./mvnw spring-boot:run
```

A aplicação será iniciada em:

```
http://localhost:8080
```

---

## 🎯 Objetivo

Projeto desenvolvido para estudos de Java e Spring Boot, com foco em:

* APIs REST;
* Spring Data JPA;
* Relacionamentos entre entidades;
* DTOs;
* Validações;
* Tratamento de exceções;
* Organização em camadas;
* Boas práticas de desenvolvimento backend.

---

## 👨‍💻 Autor

**Ramon Luz**

QA Engineer | Software Quality Analyst

* Java
* Spring Boot
* Selenium
* Playwright
* Cypress
* Rest Assured
* Python
* SQL
* CI/CD
