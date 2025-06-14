# Desafio INFUSE

Repositório da minha solução para desafio técnico da INFUSE.

## Sobre o projeto

Esta aplicação implementa um sistema de gerenciamento de créditos fiscais utilizando arquitetura hexagonal para o backend (Java/Spring Boot) e Angular para o frontend. O sistema permite consultar créditos a partir do número NFSe ou número do Crédito.

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

2. Crie um banco de dados PostgreSQL localmente:
```SQL
   CREATE DATABASE infuse_db;

3. Acesse o diretório docker e crie ou edite os arquivos de ambiente:
```bash
   cd docker

4. Configure as credenciais do banco de dados no arquivo db.env
    # Exemplo de configuração
    DB_USERNAME=seu_usuario_postgres
    DB_PASSWORD=sua_senha_postgres

5. Inicie os containers
```bash
    docker-compose up -d

6. Verifique se todos os serviços estão em execução:
```bash
    docker-compose ps

7. Acesse as aplicações:
    | Serviço | URL | Descrição |
    | --- | --- | --- |
    | Frontend | http://localhost:4200 | Interface de usuário Angular |
    | API | http://localhost:8080/infuse-api | API REST Java/Spring Boot |
    | Kafka UI | http://localhost:8090 | Interface para visualizar tópicos e mensagens do Kafka |
    | Kafka | localhost:29092 | Endereço para conexão de aplicações externas com Kafka |
    | Zookeeper | localhost:2181 | Serviço de coordenação usado pelo Kafka |

### Troubleshooting

Aguarde um tempo até todos os containers estarem de pé e as aplicações em execução

1. Se todos os containers estão em execução:
```bash
        docker-compose ps

2. Logs dos containers para identificar problemas:
    ```bash
    docker-compose logs -f api      # Logs da API
    docker-compose logs -f frontend # Logs do Frontend
    docker-compose logs -f kafka    # Logs do Kafka


### Parando a aplicação
```bash
    docker-compose dow