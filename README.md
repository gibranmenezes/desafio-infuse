# Desafio INFUSE

Repositório da minha solução para desafio técnico da INFUSE TECNOLOGIA.

## Sobre o projeto

Esta aplicação implementa um sistema de gerenciamento de créditos fiscais utilizando arquitetura hexagonal para o backend (Java/Spring Boot) e Angular para o frontend. O sistema permite consultar, cadastrar e gerenciar créditos de ISSQN e outros tributos.

## Data de entrega
2025-06-14 13:23:27

## Autor
Gibran Menezes ([@gibranmenezes](https://github.com/gibranmenezes))

## Tecnologias utilizadas

### Backend
- Java 21
- Spring Boot 3.2
- Flyway
- Maven
- Arquitetura Hexagonal
- PostgreSQL
- Kafka para processamento de eventos assíncronos
- Spring Security

### Frontend
- Node.js v22.15.1
- Angular 20
- TypeScript
- SCSS

### Infraestrutura
- Docker
- Docker Compose
- Kafka
- PostgreSQL

## Como executar o projeto

### Pré-requisitos
- Docker (versão 24.0.0 ou superior)
- Docker Compose (versão 2.20.0 ou superior)
- Git

### Passos para execução

1. Clone o repositório:
   ```bash
   git clone https://github.com/gibranmenezes/desafio-infuse.git
   cd desafio-infuse

2. Acesse o diretório docker e crie ou edite os arquivos de ambiente:
   ```bash
   cd docker

3. Configure as credenciais do banco de dados no arquivo db.env
    # Exemplo de configuração
    POSTGRES_USER=postgres
    POSTGRES_PASSWORD=admin
    POSTGRES_DB=infuse_db

4. Ajuste as configurações da API no arquivo api.env:
    # Configurações do Spring Boot
    SPRING_APPLICATION_NAME=infuse-api
    SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/infuse_db
    SPRING_DATASOURCE_USERNAME=postgres
    SPRING_DATASOURCE_PASSWORD=admin

    # Configurações do Kafka
    SPRING_KAFKA_BOOTSTRAP_SERVERS=kafka:9092
    APP_KAFKA_TOPIC_CONSULTA_CREDITOS=consulta-creditos

    # Outras configurações
    SERVER_SERVLET_CONTEXT_PATH=/infuse-api

5. Inicie os containers
    ```bash
    docker-compose up -d

6. Verifique se todos os serviços estão em execução:
    ```bash
    docker-compose ps

7. Acesse as aplicações:
    ([Frontend]: http://localhost:4200)
    ([API]: http://localhost:8080/infuse-api)
    ([Kafka]: http://localhost:9092)
    

