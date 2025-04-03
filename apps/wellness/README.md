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
│   │   └── stream.go         # Streaming handlers
│   ├── middleware/
│   │   └── logger.go         # Custom middleware
│   ├── routes/
│   │   └── routes.go         # Route definitions
│   ├── sentiment/
│   │   └── model.go          # Sentiment analysis model
│   └── stream/
│       └── processor.go       # Streaming processor
├── go.mod                    # Go module file
└── README.md                 # This file
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
# Start the stream API in the background
curl -N http://localhost:8080/api/v1/stream/results > stream_results.log &

# Submit items
curl -X POST http://localhost:8080/api/v1/stream/submit \
  -H "Content-Type: application/json" \
  -d '{"text": "I really enjoyed this product, it'\''s amazing!"}'

# Check the results
cat stream_results.log
```

### SSE Event Format

The server sends events in the following format:

```
event: connected
data: Stream connection established

event: message
data: {"id":"item-1234567890","text":"I really enjoyed this product, it's amazing!","sentiment":"positive","score":0.6,"timestamp":"2024-03-21T10:00:00Z"}
```

- `event:` specifies the event type
- `data:` contains the JSON payload

### Browser Support

SSE is supported in most modern browsers. You can open the stream URL directly in a browser:

```
http://localhost:8080/api/v1/stream/results
```

For a better user experience, you can create a simple HTML page with JavaScript to handle the SSE connection:

```html
<!DOCTYPE html>
<html>
<head>
    <title>Sentiment Analysis Stream</title>
</head>
<body>
    <h1>Sentiment Analysis Results</h1>
    <div id="results"></div>

    <script>
        const resultsDiv = document.getElementById('results');
        const eventSource = new EventSource('http://localhost:8080/api/v1/stream/results');
        
        eventSource.onmessage = function(event) {
            const result = JSON.parse(event.data);
            const resultElement = document.createElement('div');
            resultElement.innerHTML = `
                <p><strong>ID:</strong> ${result.id}</p>
                <p><strong>Text:</strong> ${result.text}</p>
                <p><strong>Sentiment:</strong> ${result.sentiment}</p>
                <p><strong>Score:</strong> ${result.score}</p>
                <p><strong>Timestamp:</strong> ${result.timestamp}</p>
                <hr>
            `;
            resultsDiv.appendChild(resultElement);
        };
        
        eventSource.onerror = function(error) {
            console.error('EventSource failed:', error);
            eventSource.close();
        };
    </script>
</body>
</html>
```

## Middleware

1. **Logger**
   - Logs request method, path, client IP
   - Tracks request duration
   - Records response status
   - Logs any errors

2. **RequestID**
   - Adds unique request ID to each request
   - Sets `X-Request-ID` header
   - Useful for request tracing

## Sentiment Analysis

The API includes a **simple dictionary-based sentiment analysis model** that:
- Analyzes text to determine if it's positive, negative, or neutral
- Provides a sentiment score from -1.0 (very negative) to 1.0 (very positive)
- Includes a confidence score for the prediction
- Uses predefined lists of positive and negative words

> **Note:** This is a basic implementation for demonstration purposes only. It is not a machine learning or AI-based solution. For production use, consider integrating with a more sophisticated NLP service or ML model.

## Streaming with Go Channels

The API includes a real-time streaming feature that leverages Go's unique concurrency features:

- **Concurrent Processing**: Uses multiple worker goroutines to process text items in parallel
- **Channel-based Communication**: Uses Go channels for communication between components
- **Non-blocking Operations**: Uses select statements with default cases to prevent blocking
- **Graceful Shutdown**: Properly handles context cancellation and cleanup
- **Server-Sent Events**: Streams results to clients using SSE

This implementation demonstrates Go's powerful concurrency model and how it can be used to build efficient, scalable applications.

## Setup

1. Ensure you have Go 1.22 or later installed
2. Clone the repository
3. Navigate to the project directory:
   ```bash
   cd midgard/apps/wellness
   ```
4. Install dependencies:
   ```bash
   go mod tidy
   ```

## Running the API

Start the server:
```bash
go run cmd/api/main.go
```

The server will start on `http://localhost:8080`

## Development

### Adding New Endpoints

1. Create handler functions in `internal/handlers/`
2. Add routes in `internal/routes/routes.go`
3. Use middleware as needed from `internal/middleware/`

### Adding New Middleware

1. Create middleware functions in `internal/middleware/`
2. Register middleware in `cmd/api/main.go` or specific route groups

## Testing

To run tests:
```bash
go test ./...
```

## Contributing

1. Create a new branch for your feature
2. Make your changes
3. Run tests
4. Submit a pull request

## License

[Add your license here] 