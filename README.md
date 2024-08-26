# Simulação de Pagamento

## Descrição do Projeto

Este projeto é uma aplicação Java desenvolvida com Spring Boot que simula um processo de compra, desde a visualização de itens no carrinho até a finalização de uma compra fictícia. A aplicação foi estruturada seguindo boas práticas de desenvolvimento, utilizando padrões de projeto e arquitetura em camadas.

## Funcionalidades

- **Cadastro de Usuário:** Criação de perfis de usuário para realizar compras.
- **Gerenciamento de Carrinho de Compras:** Adição, remoção e listagem de itens no carrinho.
- **Aplicação de Cupons de Desconto:** Aplicação de cupons de desconto na compra, com verificação de validade e limite de uso.
- **Processamento de Pagamento:** Simulação do processamento de pagamento utilizando dados fictícios de cartão de crédito.
- **Consulta de Pagamentos:** Listagem de pagamentos realizados por um usuário.

## Arquitetura e Padrões de Desenvolvimento

A aplicação segue uma arquitetura em camadas, dividida da seguinte forma:

- **Camada de Apresentação:** Controllers que expõem as APIs REST utilizando Spring MVC.
- **Camada de Serviço:** Contém a lógica de negócio, implementada em serviços que são chamados pelos controllers.
- **Camada de Persistência:** Utiliza Spring Data JPA para o acesso a dados, com um banco de dados PostgreSQL.
- **Camada de Domínio:** Contém as entidades JPA que representam as tabelas no banco de dados.

### Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3.x**
- **Spring Data JPA**
- **PostgreSQL**
- **Gradle**
- **Swagger** para documentação das APIs
- **RestTemplate** para chamadas a APIs REST externas
- **DBeaver** para gerenciamento do banco de dados

## Regras de Negócio Implementadas

- **Validação de Cupons de Desconto:**
    - Um cupom só pode ser aplicado se estiver ativo e dentro do limite de usos.
    - Cada cupom tem um percentual de desconto associado, que é aplicado ao valor total da compra.
    - O sistema impede a aplicação de cupons expirados ou que já tenham atingido o limite de uso.
- **Invalidação Automática:**
    - Um cupom é automaticamente invalidado após ser utilizado 10 vezes.
- **Processamento de Pagamento:**
    - O sistema calcula o valor total do carrinho e aplica o desconto do cupom, caso seja válido.
    - Após o processamento, o sistema associa os itens do carrinho ao pagamento e limpa o carrinho do usuário.
    - Cada pagamento é registrado no banco de dados para futuras consultas.
- **Manipular Itens ao Carrinho:**
    - Permite inserir um item no carrinho vinculando 
    - Listar itens do carrinho
    - Deletar um item do carrinho
## Documentação

A documentação das APIs REST está disponível através do Swagger. Você pode acessá-la em:

http://localhost:8080/swagger-ui/index.html

Essa documentação fornece detalhes sobre os endpoints disponíveis, os parâmetros necessários e as respostas esperadas.

## Configuração do Banco de Dados com Docker

Para facilitar a configuração e o acesso ao banco de dados, utilizamos o **Docker**. Com isso, você pode rodar um container do PostgreSQL ou MySQL localmente, sem precisar instalar o banco de dados diretamente na sua máquina.

### Subindo o Banco de Dados com Docker

1. **PostgreSQL:**

   Se estiver utilizando o PostgreSQL, crie um container com o seguinte comando:

   ```bash
   docker run --name simulacao_pagamento_db -e POSTGRES_PASSWORD=senha_secreta -e POSTGRES_DB=simulacao_pagamento -p 5432:5432 -d postgres
2. **MYSQl:**
   ```bash
   docker run --name simulacao_pagamento_db -e MYSQL_ROOT_PASSWORD=senha_secreta -e MYSQL_DATABASE=simulacao_pagamento -p 3306:3306 -d mysql
3. **Configuração da Aplicação:**

   Após subir o container, configure a conexão ao banco de dados no arquivo application.properties da seguinte forma:

   Para PostgreSQL:
    
       spring.datasource.url=jdbc:postgresql://localhost:5432/simulacao_pagamento
       spring.datasource.username=postgres
       spring.datasource.password=senha_secreta

   Para MySQL:

       spring.datasource.url=jdbc:mysql://localhost:3306/simulacao_pagamento
       spring.datasource.username=root
       spring.datasource.password=senha_secreta


4. **Executando a Aplicação:**

    Com o banco de dados configurado e rodando no Docker, você pode iniciar a aplicação normalmente usando:
   ```bash
      ./gradlew bootRun
## Uso do RestTemplate

A aplicação utiliza o `RestTemplate` para realizar chamadas a serviços REST externos. Por exemplo, durante o processo de aplicação de um cupom de desconto, o `RestTemplate` é usado para chamar o endpoint `/cupom/aplicar` e validar o cupom em um serviço externo.

## Como Executar o Projeto

### Pré-requisitos

- Java 17 ou superior
- PostgreSQL
- Gradle

### Passos para Execução

1. Clone o repositório:
   ```bash
   git clone <URL do repositório>
   cd simulacao_pagamento
