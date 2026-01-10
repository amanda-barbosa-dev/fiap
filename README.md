# Tech Challenge - Fase 03  
**Pós Tech FIAP | Arquitetura e Desenvolvimento Java**

[![Java](https://img.shields.io/badge/Java-17-orange)](https://www.java.com/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-green)](https://spring.io/projects/spring-boot)
[![GraphQL](https://img.shields.io/badge/GraphQL-purple)](https://graphql.org/)
[![Apache Kafka](https://img.shields.io/badge/Apache%20Kafka-black)](https://kafka.apache.org/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-blue)](https://www.postgresql.org/)
[![Docker](https://img.shields.io/badge/Docker-blue)](https://www.docker.com/)

**Autor:** Amanda Barbosa  
**Repositório:** [https://github.com/amanda-barbosa-dev/fiap](https://github.com/amanda-barbosa-dev/fiap)  
**Data de entrega:** Janeiro 2026

## Visão Geral do Projeto

Sistema hospitalar backend simplificado e modular para gerenciamento de **agendamentos de consultas**, **histórico médico** e **envio de lembretes automáticos** aos pacientes.

O sistema atende diferentes perfis de usuários com controle de acesso rigoroso:
- **Médicos**: visualizam e editam histórico de consultas
- **Enfermeiros**: registram e acessam histórico de consultas
- **Pacientes**: visualizam apenas suas próprias consultas

### Tecnologias Utilizadas
- **Java 17** + **Spring Boot 3**
- **Spring Security** (autenticação básica + autorização por roles)
- **GraphQL** (consultas flexíveis ao histórico)
- **Apache Kafka** (comunicação assíncrona entre serviços)
- **PostgreSQL** (persistência)
- **Docker** + **Docker Compose** (containerização)
- **Arquitetura Hexagonal** (Ports & Adapters) → separação clara de domínios, aplicação e infraestrutura
- **JUnit 5 + Mockito + Testcontainers** → cobertura de testes > 80%
- **Maven** (build)

### Objetivos Atendidos (Requisitos da Fase 3)
- Autenticação e autorização com Spring Security
- Níveis de acesso diferenciados por perfil
- GraphQL para consultas flexíveis (todas consultas ou apenas futuras)
- Separação em microsserviços: Agendamento + Notificações
- Comunicação assíncrona via Kafka (evento de criação/edição de consulta → envio de lembrete)
- Boas práticas, código organizado e modular

## Arquitetura

O projeto segue a **Arquitetura Hexagonal** (Ports and Adapters), garantindo:
- Domínio isolado de frameworks externos
- Fácil troca de tecnologias (ex: trocar Kafka por RabbitMQ)
- Testabilidade alta

### Estrutura de Pastas (Principais)

fiap/
├── medicalappointmentsservice/     # Serviço de Agendamento (core)
│   ├── src/main/java/com/.../domain        # Entidades, value objects, regras de negócio
│   ├── src/main/java/com/.../application   # Use cases / Services
│   ├── src/main/java/com/.../adapter       # Controllers (REST + GraphQL), Kafka Producer, Repository impl
│   ├── src/test/...                        # Testes unitários + integração
│
├── notificationservice/            # Serviço de Notificações
│   ├── ...                                 # Consumer Kafka + lógica de envio (simulado)
│
├── docker-compose.yml              # Orquestra Postgres + Kafka + Zookeeper + 2 serviços Java
└── README.md



### Diagrama Conceitual (Fluxo Principal)
1. Usuário autenticado (Médico/Enfermeiro) → cria/edita consulta (REST)
2. Scheduling Service → salva no PostgreSQL + publica evento no Kafka
3. Notification Service → consome evento → envia lembrete (log/simulação)
4. Qualquer usuário → consulta histórico via GraphQL (com autorização)

## Endpoints da API

### Autenticação (Basic Auth)
- `GET /auth/login` → retorna token ou usa Basic Auth em todas as chamadas

**Usuários padrão (criados via data.sql):**
INSERT INTO tb_users (id, name, medical_specialty, password, username, role)
VALUES (1, 'JOAO SILVA SANTOS', 'CARDIOLOGIST', 'JSCARDIO', 'JSCARDIO', 'DOCTOR');

INSERT INTO tb_users (id, name, medical_specialty, password, username, role)
VALUES (2, 'AMANDA GOMES BARBOSA', NULL, 'AMANDAGB', 'AMANDAGB', 'PATIENT');

INSERT INTO tb_users (id, name, medical_specialty, password, username, role)
VALUES (3, 'ANA LUISA SOUZA', NULL, 'ANASOUZA', 'ANASOUZA', 'NURSE');

INSERT INTO tb_users (id, name, medical_specialty, password, username, role)
VALUES (4, 'MARCOS COSTA RODRIGUES', 'ORTHOPEDIST', 'MARCOSORTHO', 'MARCOSORTHO', 'DOCTOR');

INSERT INTO tb_users (id, name, , medical_specialty, password, username, role)
VALUES (5, 'ALESSANDRA MARIA SUZANA', NULL, 'ALESUZ', 'ALESUZ', 'PATIENT');


Como Configurar e Executar
Pré-requisitos

Docker + Docker Compose
Java 17 (caso queira rodar sem Docker)
Git

Passo a passo (Recomendado: Docker)

Clone o repositórioBashgit clone https://github.com/amanda-barbosa-dev/fiap.git
cd fiap
Suba o ambiente completoBashdocker compose up --build
Isso levanta:
PostgreSQL (5432)
Kafka + Zookeeper (9092)
medicalappointmentsservice (8080)
notificationservice (8081)


Aguarde ~30-60 segundos até os serviços estarem prontos
Acesse:
Swagger/OpenAPI (se configurado): http://localhost:8080/swagger-ui.html
GraphQL Playground: http://localhost:8080/graphql (use o Altair ou Postman)


Executar sem Docker (desenvolvimento)

Configure application.yml com banco local
Rode cada serviço separadamente:Bashcd medicalappointmentsservice && mvn spring-boot:run
cd ../notificationservice && mvn spring-boot:run

