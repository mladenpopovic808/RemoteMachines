# VacuumCleaners



The Vacuum Cleaner Management Application is a Spring Boot-based project designed to manage vacuum cleaners. It provides functionalities such as adding, creating, and starting vacuum cleaners. The application also uses a scheduler to automate the starting of vacuum cleaners at specified times. 

## Features
- User registration and management
- Role-based access control
- User authentication and authorization
- Integration with Redis for caching
- RESTful API endpoints
- Unit and integration testing with JUnit and Cucumber
- API documentation with Swagger

## Technologies Used
Backend:
- **Java**: The primary programming language.
- **Spring Boot**: Framework for building the application.
- **Spring Boot Starter Security**
- **Spring Boot Starter Thymeleaf**
- **Spring Boot Starter Data Redis**
- **Maven**: Build and dependency management tool.
- **Redis**: In-memory data structure store, used as a database, cache, and message broker.
- **JUnit**: Testing framework for unit tests.
- **Cucumber**: Testing framework for behavior-driven development (BDD).
- **Swagger**: Tool for API documentation.
- **Lombok**: Java library that helps to reduce boilerplate code.

Frontend:
- **TypeScript**
- **npm (Node Package Manager)**
- **Angular**
  
## Prerequisites
To run this project locally, you need to have the following programs installed on your local machine:
- **Java Development Kit (JDK)**: Version 11 or higher.
- **Maven**: For building and managing dependencies.
- **Redis**: For caching and session management.
- **MySQL**: For persistent data storage (if applicable).

Set up Redis:

    Download and install Redis from Redis.io.
    Start the Redis server:

    bash

    redis-server

Set up MySQL (if applicable):

    Download and install MySQL from MySQL.com.
    Create a database and configure the connection settings in application.properties.
