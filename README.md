# Projeto de Locação de Veículos

Este projeto é uma aplicação de locação de veículos baseada em microsserviços, utilizando uma arquitetura escalável e independente entre os serviços. Cada serviço é containerizado e gerenciado com Docker, facilitando o deploy e a comunicação entre eles.

## Tecnologias Utilizadas

- **Spring Boot**: Framework para criar e gerenciar os microsserviços.
- **Eureka Server**: Serviço de descoberta para que os microsserviços possam se registrar e se comunicar dinamicamente.
- **MySQL**: Banco de dados relacional para armazenamento de dados.
- **Docker e Docker Compose**: Ferramentas para containerização e orquestração dos serviços.

## Estrutura do Projeto

O projeto é dividido em três serviços principais:

1. **User Service**: Gerencia dados dos usuários.
2. **Rental Service**: Controla as operações de locação de veículos.
3. **Vehicle Service**: Gerencia o catálogo de veículos.

Esses serviços estão conectados a um **Eureka Server** para descoberta de serviços, permitindo que se comuniquem facilmente entre si.

## Funcionamento

1. **Eureka Server** inicia e fica disponível para registrar os outros serviços.
2. Cada serviço (User, Rental e Vehicle) se registra no Eureka Server ao iniciar, permitindo que os outros serviços o encontrem e comuniquem-se através do Eureka.
3. **Docker Compose** é usado para gerenciar os containers de cada serviço, permitindo iniciar e parar todos de forma centralizada.

