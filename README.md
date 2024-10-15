# VacuumCleaners



Application is a Spring Boot-based project designed to manage users and their roles. It provides functionalities such as user registration, role assignment, and user authentication. The application leverages various Spring Boot modules and integrates with several external libraries and tools to provide a comprehensive solution for user management.

## Features
- User registration and management
- Role-based access control
- User authentication and authorization
- Integration with Redis for caching
- RESTful API endpoints
- Unit and integration testing with JUnit and Cucumber
- API documentation with Swagger

## Technologies Used
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

## Prerequisites
To run this project locally, you need to have the following programs installed on your local machine:
- **Java Development Kit (JDK)**: Version 11 or higher.
- **Maven**: For building and managing dependencies.
- **Redis**: For caching and session management.
- **MySQL**: For persistent data storage (if applicable).

## Installation
1. Clone the repository:  
   ```bash
   git clone https://github.com/mpopovic9720rn/VacuumCleaners-Springboot-Backend.git
   cd VacuumCleaners-Springboot-Backend

    Install dependencies:

    bash

mvn clean install

Set up Redis:

    Download and install Redis from Redis.io.
    Start the Redis server:

    bash

    redis-server

Set up MySQL (if applicable):

    Download and install MySQL from MySQL.com.
    Create a database and configure the connection settings in application.properties.
