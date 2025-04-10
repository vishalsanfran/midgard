# Poker Engine

A Spring Boot based poker game engine service.

## Prerequisites

- Java 21
- Maven 3.x (or use included Maven wrapper)

## Building the Project

Using Maven wrapper:
```bash
./mvnw clean install
 ```

## Running the Application
```bash
./mvnw spring-boot:run
 ```

The application will start on http://localhost:8080

## Running tests

```bash
./mvnw test
 ```
 
## Development
This project uses:

- Spring Boot 3.2.0
- Java 21
- JUnit 5 for testing
- JaCoCo for test coverage
- Checkstyle for code formatting

## API Documentation

Once the application is running, you can access the API documentation at:
- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI Spec: http://localhost:8080/v3/api-docs