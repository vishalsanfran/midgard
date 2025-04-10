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

## Generate checkstyle report
`./mvnw checkstyle:checkstyle`
View report: `open target/site/checkstyle.html`

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
- Prometheus metrics
- Actuator for health checks
- Error handling framework
- API versioning
- Rate limiter
- OpenAPI docs

## API Documentation

Once the application is running, you can access the API documentation at:
- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI Spec: http://localhost:8080/api-docs

### API Endpoints

Card model: {"suit": "HEARTS", "rank": "THREE"},

#### Check for Straight Hand
Evaluates if a given set of poker cards forms a straight.

- Endpoint: `POST /api/v1/hand/isstraight`
- Content-Type: `application/json`
- Request Body: Array of cards, each with suit and rank
- Response: Boolean indicating if the hand forms a straight

Example Request:
```bash
curl -X POST 'http://localhost:8080/api/v1/hand/isstraight' \
-H 'Content-Type: application/json' \
-d '{
  "cards": [
    {"suit": "HEARTS", "rank": "SIX"}, 
    {"suit": "HEARTS", "rank": "TWO"},
    {"suit": "HEARTS", "rank": "THREE"}, 
    {"suit": "DIAMONDS", "rank": "FOUR"},
    {"suit": "SPADES", "rank": "FIVE"}                                      
  ]
}'
```

Example Response:
```json
{
  "timestamp": "2025-04-10T11:38:27.624764",
  "status": 200,
  "message": "Successfully evaluated the hand",
  "data": true
}
```

### API Versioning
All endpoints should be versioned , by default they start at v1
- is straight API: http://localhost:8080/api/v1/hand/isstraight
