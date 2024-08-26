Simulação de Pagamento
Este projeto consiste em uma aplicação de simulação de pagamento, desenvolvida utilizando Spring Boot e Gradle. A aplicação é capaz de realizar a simulação de um processo de compra, onde o usuário pode adicionar itens ao carrinho, aplicar cupons de desconto e processar o pagamento. O projeto segue as boas práticas de desenvolvimento, utilizando uma arquitetura em camadas que separa as responsabilidades de acesso a dados, lógica de negócios e apresentação.

Funcionalidades
Cadastro de Usuários: Permite o cadastro e a gestão de usuários que podem realizar compras na plataforma.

Carrinho de Compras: Implementação de um carrinho onde o usuário pode adicionar, remover e visualizar itens.

Cupons de Desconto: Funcionalidade para criação, aplicação e validação de cupons de desconto. O sistema verifica a validade do cupom, bem como se ele já foi utilizado o número máximo de vezes permitido.

Processamento de Pagamento: Integração com um módulo de pagamento que permite a simulação do processo de pagamento, com cálculo do valor total, aplicação de descontos e confirmação da compra.

Documentação com Swagger: A API está completamente documentada com o Swagger, permitindo a visualização e teste das funcionalidades diretamente por uma interface web.

Arquitetura e Padrões de Desenvolvimento
Arquitetura em Camadas: O projeto segue uma arquitetura em camadas, com separação clara entre as responsabilidades:

Controller: Camada responsável por receber as requisições HTTP e interagir com a camada de serviço para processar as lógicas de negócios.
Service: Camada onde está implementada a lógica de negócios. É responsável por validar as regras de negócio, como a aplicação e validação de cupons de desconto.
Repository: Camada responsável pelo acesso aos dados. Utiliza Spring Data JPA para a comunicação com o banco de dados PostgreSQL.
Padrão DTO (Data Transfer Object): Utilizado para a transferência de dados entre as camadas da aplicação. Isso ajuda a encapsular e abstrair as entidades diretamente da camada de persistência.

Uso de RestTemplate: A aplicação faz uso de RestTemplate para realizar chamadas a serviços externos ou a outros endpoints da própria aplicação, como no caso da aplicação de cupons de desconto. Isso facilita a comunicação HTTP entre serviços e a implementação de arquiteturas baseadas em microserviços.

Tecnologias Utilizadas
Spring Boot: Framework principal utilizado para o desenvolvimento da aplicação.
Gradle: Ferramenta de automação de compilação utilizada para gerenciar as dependências e construir o projeto.
PostgreSQL: Banco de dados utilizado para armazenar as informações da aplicação, como usuários, itens do carrinho e cupons de desconto.
Swagger: Ferramenta utilizada para documentar e testar a API REST.
RestTemplate: Utilizado para realizar chamadas HTTP para outros serviços ou endpoints dentro da própria aplicação.
Regras de Negócio Implementadas
Aplicação de Cupons de Desconto: O cupom de desconto só pode ser aplicado se for válido e ativo. Além disso, cada cupom tem um número máximo de utilizações, após o qual ele não pode mais ser utilizado. Ao aplicar o cupom, o sistema valida esses requisitos e calcula o valor de desconto, subtraindo-o do valor total do carrinho.

Processamento de Pagamento: O sistema simula o processamento do pagamento, verificando a validade do cartão de crédito informado, aplicando os descontos pertinentes e, finalmente, confirmando a compra. Se alguma das regras de negócio for violada (como um cupom inválido ou cartão de crédito expirado), o sistema retorna uma mensagem de erro apropriada.

Documentação da API
A aplicação inclui uma documentação completa da API utilizando Swagger. Para acessar a documentação, você pode iniciar a aplicação e navegar até http://localhost:8080/swagger-ui.html em seu navegador. A partir dessa interface, você pode explorar todos os endpoints disponíveis, visualizar os modelos de dados utilizados e até mesmo executar chamadas de API diretamente.
