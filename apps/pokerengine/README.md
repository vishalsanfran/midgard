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

Run tests and generate coverage report: `./mvnw verify`

Open coverage report in browser: `open target/site/jacoco/index.html`
Minimum coverage property is defined in `pom.xml` as `coverage.minimum`

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

### API Versioning
All endpoints are versioned and accessible under `/api/v1/`
Example endpoints:
- Health check: http://localhost:8080/api/v1/actuator/health
- API Documentation: http://localhost:8080/api/v1/api-docs
- Swagger UI: http://localhost:8080/api/v1/swagger-ui.html