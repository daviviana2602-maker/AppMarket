# Controle de Compras Familiar

Aplicativo mobile para controle completo das compras de mercado de uma família.

A proposta é permitir que os membros de uma família registrem suas compras diretamente pelo celular, mantendo um histórico organizado de **produtos, preços, quantidades, mercados e gastos**.

Mais do que uma lista de compras, o sistema funciona como um histórico financeiro das compras de mercado da família, permitindo consultar quanto foi gasto e comparar preços de produtos ao longo do tempo.

## Funcionalidades

* Cadastro e autenticação de usuários
* Criação e gerenciamento de famílias
* Convites entre usuários para participação em uma família
* Registro de compras feitas pela família
* Classificação das compras em:

    * `MONTHLY` — compras do mês
    * `CASUAL` — compras ocasionais
* Cadastro de mercado onde a compra foi realizada
* Catálogo de produtos
* Busca de produtos utilizando Elasticsearch
* Adição de produtos à compra
* Registro de quantidade e preço unitário
* Criação de produtos personalizados quando necessário
* Histórico de preços dos produtos
* Comparação com compras anteriores
* Histórico completo das compras da família
* Consulta de gastos por período
* Consulta de gastos por produto
* Consulta de gastos por mercado
* Consulta separada entre compras mensais e ocasionais
* Participação de múltiplos membros da mesma família nas compras

## Exemplo de uso

Durante uma compra, o usuário procura por um produto:

> Banana

Ao selecionar o produto e informar o preço atual, o aplicativo pode apresentar informações de compras anteriores:

| Compra           | Data       | Mercado   |    Preço |
| ---------------- | ---------- | --------- | -------: |
| Compra do mês    | 14/07/2026 | Mercado X | R$ 13,00 |
| Compra ocasional | 19/07/2026 | Mercado Y |  R$ 9,83 |
| Compra atual     | 01/10/2026 | Mercado Z | R$ 13,47 |

Dessa forma, cada compra registrada gera informações que podem ser utilizadas posteriormente pela família.

## Arquitetura

O projeto é dividido entre um aplicativo mobile e uma API backend.

```text
┌──────────────────────┐
│     Flutter App      │
│      Dart / UI       │
└──────────┬───────────┘
           │
           │ HTTP / JSON
           ▼
┌──────────────────────┐
│    Spring Boot API   │
│   Java 21 / REST     │
└───────┬────────┬─────┘
        │        │
        ▼        ▼
┌────────────┐ ┌───────────────┐
│ PostgreSQL │ │ Elasticsearch │
│            │ │               │
│ Dados      │ │ Busca de      │
│ principais │ │ produtos      │
└────────────┘ └───────────────┘
```

O **PostgreSQL** é a fonte principal dos dados da aplicação.

O **Elasticsearch** será utilizado principalmente para busca rápida no catálogo de produtos.

## Tecnologias

### Mobile

* Flutter
* Dart

### Backend

* Java 21
* Spring Boot
* Spring Security
* Spring Data JPA
* Bean Validation
* Flyway
* REST API

### Banco e busca

* PostgreSQL
* Elasticsearch

### Testes

* JUnit
* Mockito
* Testcontainers

### Infraestrutura e desenvolvimento

* Docker
* Docker Compose
* Git
* GitHub

## Principais entidades

```text
User
 │
 ├── FamilyMember
 │        │
 │        ▼
 │      Family
 │        │
 │        ▼
 │      Purchase
 │        │
 │        ▼
 │   PurchaseItem
 │        │
 │        ▼
 │      Product
```

### User

Representa um usuário da aplicação.

### Family

Representa uma família e concentra os dados compartilhados entre seus membros.

### FamilyMember

Relaciona usuários às famílias e permite controlar a participação de cada usuário.

### FamilyInvitation

Representa convites enviados entre usuários para participação em uma família.

### Product

Representa um produto do catálogo.

### Purchase

Representa uma compra realizada pela família.

Uma compra possui:

* Tipo (`MONTHLY` ou `CASUAL`)
* Mercado
* Data
* Usuário responsável pelo registro
* Família
* Itens da compra

### PurchaseItem

Representa um produto dentro de uma compra.

Possui:

* Produto
* Quantidade
* Preço unitário
* Preço total

## Organização do desenvolvimento

O projeto será desenvolvido por duas pessoas, com responsabilidades técnicas separadas entre backend e mobile.

### Backend

**Java + Spring Boot**

Responsável pela:

* API REST
* Regras de negócio
* Autenticação e autorização
* Persistência
* PostgreSQL
* Elasticsearch
* Testes do backend

### Mobile

**Flutter + Dart**

Responsável pela:

* Interface
* Navegação
* Experiência de uso
* Consumo da API
* Estado da aplicação
* Validações relacionadas à interface

### Git

Cada funcionalidade será desenvolvida em uma branch própria.

```text
main
 ├── feature/auth
 ├── feature/family
 ├── feature/purchase
 ├── feature/product
 └── feature/history
```

O desenvolvimento será organizado através de:

* Issues para funcionalidades e tarefas
* Branches separadas
* Pull Requests
* Comunicação direta entre os desenvolvedores
* Testes antes da integração

A revisão técnica fica principalmente sob responsabilidade de quem desenvolveu cada tecnologia. A integração entre mobile e backend será validada em conjunto.

## Contrato da API

Antes da integração entre as partes, os contratos principais da API serão definidos para estabelecer claramente:

* Endpoints
* Métodos HTTP
* Request bodies
* Response bodies
* Códigos HTTP
* Formato dos erros
* Autenticação
* Paginação e filtros

Exemplo:

```http
POST /api/purchases
```

```json
{
  "type": "MONTHLY",
  "market": "Mercado X",
  "items": [
    {
      "productId": 123,
      "quantity": 2,
      "unitPrice": 13.47
    }
  ]
}
```

## Objetivo do projeto

Criar uma aplicação mobile simples de utilizar durante uma compra, mas capaz de manter um histórico completo e estruturado das compras da família.

A ideia central é:

> **Cada compra registrada hoje gera informações úteis para as próximas compras.**

Com o histórico acumulado, a família pode entender melhor seus gastos, acompanhar preços e ter uma visão centralizada de todas as compras realizadas por seus membros.
