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
* Flyway
* PostgreSQL opcional
* OpenAPI/Swagger
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
* Atualizar status do funcionário
* Remover funcionário

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
  "message": "CPF must have 11 digits",
  "field": "cpf"
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
  ./mvnw spring-boot:run -Dspring-boot.run.profiles=dev-postgres
  ```

Para testar um banco persistente com PostgreSQL, inicie o Docker Compose e use o perfil opcional:

```bash
docker compose up -d
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev-postgres
```

### Console H2

O console H2 permanece desativado por padrão para evitar acesso direto ao banco.

---

## 📌 Endpoints

### Livros

| Método | Endpoint    |
| ------ | ----------- |
| GET    | /books      |
| GET    | /books/{id} |
| POST   | /books      |
| PUT    | /books/{id} |
| DELETE | /books/{id} |

---

### Clientes

| Método | Endpoint       |
| ------ | -------------- |
| GET    | /customers      |
| GET    | /customers/{id} |
| POST   | /customers      |
| PUT    | /customers/{id} |
| DELETE | /customers/{id} |

---

### Funcionários

| Método | Endpoint       |
| ------ | -------------- |
| GET    | /employees      |
| GET    | /employees/{id} |
| POST   | /employees      |
| PUT    | /employees/{id} |
| DELETE | /employees/{id} |

---

### Vendas

| Método | Endpoint    |
| ------ | ----------- |
| GET    | /sales      |
| GET    | /sales/{id} |
| POST   | /sales      |

As listagens aceitam `page`, `size` e `sort`. Também possuem filtros:

* `/books?title=java`
* `/customers?name=ana`
* `/employees?name=joao`
* `/sales?date=2026-09-13`

As respostas de listagem são paginadas e incluem os metadados da página.

## Swagger

Com a aplicação em execução, a documentação pode ser acessada em:

```
http://localhost:8080/swagger-ui.html
```

---

## ▶️ Executando o projeto

Clone o repositório:

```bash
git clone https://github.com/RamonLuz/Projeto-Spring-Boot.git
```

Entre na pasta do projeto:

```bash
cd Projeto-Spring-Boot
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
