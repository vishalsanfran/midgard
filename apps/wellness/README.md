# Wellness API

A Go-based REST API for the wellness application using the Gin framework.

## Project Structure

```
wellness/
├── cmd/
│   └── api/
│       └── main.go           # Application entry point
├── internal/
│   ├── handlers/
│   │   ├── health.go         # Health check handlers
│   │   ├── sentiment.go      # Sentiment analysis handlers
│   │   ├── stream.go         # Streaming handlers
│   │   └── topics.go         # Topic analysis handlers
│   ├── middleware/
│   │   └── logger.go         # Custom middleware
│   ├── routes/
│   │   └── routes.go         # Route definitions
│   ├── sentiment/
│   │   └── model.go          # Sentiment analysis model
│   ├── stream/
│   │   └── processor.go       # Streaming processor
│   └── topics/
│       └── analyzer.go        # Topic analysis model
├── go.mod                    # Go module file
└── README.md                 # This file
```

## Running Tests

The project includes comprehensive unit tests for various components. To run the tests, use the following commands:

### Run All Tests
```bash
go test ./... -v
```

### Run Tests for Specific Packages
```bash
# Test sentiment analysis
go test ./internal/sentiment -v

# Test handlers
go test ./internal/handlers -v

# Test stream processor
go test ./internal/stream -v

# Test topic analyzer
go test ./internal/topics -v
```

### Test Coverage
To run tests with coverage reporting:
```bash
go test ./... -v -cover
```

For a detailed HTML coverage report:
```bash
go test ./... -coverprofile=coverage.out
go tool cover -html=coverage.out
```

## Features

- RESTful API using Gin framework
- Request logging middleware
- Request ID tracking
- Health check endpoint
- API versioning
- Structured error handling
- Simple sentiment analysis model
- Real-time streaming with Go channels
- Wellness topic analysis

## API Endpoints

### Health Check
- `GET /health`
  - Returns service health status
  - Response:
    ```json
    {
      "status": "healthy",
      "timestamp": "2024-03-21T10:00:00Z",
      "version": "1.0.0"
    }
    ```

### API v1
- `GET /api/v1/ping`
  - Basic ping endpoint
  - Response:
    ```json
    {
      "message": "pong",
      "time": "2024-03-21T10:00:00Z"
    }
    ```

- `POST /api/v1/sentiment`
  - Analyzes the sentiment of the provided text
  - Request:
    ```json
    {
      "text": "I really enjoyed this product, it's amazing!"
    }
    ```
  - Response:
    ```json
    {
      "result": {
        "text": "I really enjoyed this product, it's amazing!",
        "sentiment": "positive",
        "score": 0.6,
        "confidence": 0.6
      }
    }
    ```

- `POST /api/v1/topics`
  - Analyzes text to identify wellness-related topics
  - Request:
    ```json
    {
      "text": "I've been feeling stressed lately and need to exercise more."
    }
    ```
  - Response:
    ```json
    {
      "result": {
        "text": "I've been feeling stressed lately and need to exercise more.",
        "topics": ["mental_health", "physical_health"],
        "confidence": 0.25
      }
    }
    ```

- `POST /api/v1/stream/submit`
  - Submits text for asynchronous sentiment analysis
  - Request:
    ```json
    {
      "text": "I really enjoyed this product, it's amazing!"
    }
    ```
  - Response:
    ```json
    {
      "id": "item-1234567890"
    }
    ```

- `GET /api/v1/stream/results`
  - Streams sentiment analysis results using Server-Sent Events (SSE)
  - Response: A stream of events in the format:
    ```
    event: message
    data: {"id":"item-1234567890","text":"I really enjoyed this product, it's amazing!","sentiment":"positive","score":0.6,"timestamp":"2024-03-21T10:00:00Z"}
    ```

## API Usage Examples

Here are curl examples for each API endpoint:

### Health Check
```bash
curl -X GET http://localhost:8080/health
```

### Ping
```bash
curl -X GET http://localhost:8080/api/v1/ping
```

### Sentiment Analysis
```bash
curl -X POST http://localhost:8080/api/v1/sentiment \
  -H "Content-Type: application/json" \
  -d '{"text": "I really enjoyed this product, it'\''s amazing!"}'
```

### Topic Analysis
```bash
curl -X POST http://localhost:8080/api/v1/topics \
  -H "Content-Type: application/json" \
  -d '{"text": "I'\''ve been feeling stressed lately and need to exercise more."}'
```

### Submit Text for Streaming Analysis
```bash
curl -X POST http://localhost:8080/api/v1/stream/submit \
  -H "Content-Type: application/json" \
  -d '{"text": "I really enjoyed this product, it'\''s amazing!"}'
```

### Stream Results (using curl)
```bash
curl -N http://localhost:8080/api/v1/stream/results
```

### Stream Results (using a browser)
Open the following URL in your browser to see the streaming results:
```
http://localhost:8080/api/v1/stream/results
```

### Complete Streaming Example

To properly use the streaming functionality, follow these steps:

1. First, submit some text for analysis:
```bash
curl -X POST http://localhost:8080/api/v1/stream/submit \
  -H "Content-Type: application/json" \
  -d '{"text": "I really enjoyed this product, it'\''s amazing!"}'
```

2. Then, in a separate terminal, connect to the stream to receive results:
```bash
curl -N http://localhost:8080/api/v1/stream/results
```

3. You can submit multiple items and they will be processed and streamed to all connected clients.

> **Note:** The streaming connection must be established before or shortly after submitting items. If you connect to the stream after all items have been processed, you won't see any results.

## Wellness Topic Analysis

The topic analyzer identifies wellness-related topics in text, such as:

- **Mental Health**: Anxiety, depression, stress, meditation, mindfulness, therapy
- **Physical Health**: Exercise, workout, fitness, nutrition, diet, sleep
- **Emotional Wellbeing**: Emotions, feelings, happiness, joy, sadness, anger
- **Social Connection**: Friends, family, community, support, relationships
- **Spiritual Wellness**: Spirituality, purpose, meaning, values, beliefs

The analyzer uses natural language processing techniques to recognize topics based on predefined keywords and phrases associated with different wellness categories. It returns a confidence score indicating how confident it is in its topic identification.

## Streaming SSE API Details

### Understanding Server-Sent Events (SSE)

The streaming API uses Server-Sent Events (SSE), which is a web standard that allows a server to push data to the client over HTTP. Unlike WebSockets, SSE is a one-way communication channel from server to client.

### The `-N` Flag in curl

The `-N` flag in the curl command is crucial for SSE connections:

```bash
curl -N http://localhost:8080/api/v1/stream/results
```

This flag disables buffering of the output, which is essential for SSE connections. Without it, curl would buffer the output and only display it when the connection is closed, defeating the purpose of real-time streaming.

### Running the APIs in Parallel

To effectively use the streaming API, you need to run two operations in parallel:

1. **Submit API**: Sends text for analysis
2. **Stream API**: Receives the analysis results in real-time

#### Using Two Terminal Windows

The simplest approach is to use two separate terminal windows:

**Terminal 1 (Submit API):**
```bash
# Submit multiple items
curl -X POST http://localhost:8080/api/v1/stream/submit \
  -H "Content-Type: application/json" \
  -d '{"text": "I really enjoyed this product, it'\''s amazing!"}'

# Wait a moment, then submit another item
sleep 2
curl -X POST http://localhost:8080/api/v1/stream/submit \
  -H "Content-Type: application/json" \
  -d '{"text": "This product is terrible, I hate it!"}'
```

**Terminal 2 (Stream API):**
```bash
curl -N http://localhost:8080/api/v1/stream/results
```

#### Using Background Processes

You can also run the stream API in the background:

```bash
# Start the stream in the background
curl -N http://localhost:8080/api/v1/stream/results > stream_results.log &

# Submit items
curl -X POST http://localhost:8080/api/v1/stream/submit \
  -H "Content-Type: application/json" \
  -d '{"text": "I really enjoyed this product, it'\''s amazing!"}'

# Check the results
cat stream_results.log
```

## Development

### Adding New Features

When adding new features to the API, follow these guidelines:

1. Create a new package in the `internal` directory if needed
2. Implement the feature logic in the package
3. Create a handler in the `internal/handlers` directory
4. Add the route to `internal/routes/routes.go`
5. Add tests for the new feature
6. Update this README with the new feature details

### Code Style

The project follows the standard Go code style. Use `gofmt` to format your code before committing:

```bash
gofmt -w .
```

### Testing

Always write tests for new features. The project uses the standard Go testing package and testify for assertions.

## License

This project is licensed under the MIT License - see the LICENSE file for details. 