# Midgard

A collection of microservices and applications built with Go.

## Project Structure

```
midgard/
├── apps/
│   └── wellness/           # Wellness API application
│       ├── cmd/
│       │   └── api/
│       │       └── main.go # Application entry point
│       ├── internal/
│       │   ├── handlers/   # HTTP handlers
│       │   ├── middleware/ # Custom middleware
│       │   ├── routes/     # Route definitions
│       │   ├── sentiment/  # Sentiment analysis model
│       │   └── stream/     # Streaming processor
│       ├── go.mod          # Go module file
│       └── README.md       # Application-specific documentation
└── README.md               # This file
```

## Applications

### Wellness API

A Go-based REST API for sentiment analysis using the Gin framework. It provides:

- RESTful API endpoints for sentiment analysis
- Real-time streaming with Server-Sent Events (SSE)
- Request logging and tracking
- Health check endpoints
- API versioning

#### Features

- Simple dictionary-based sentiment analysis
- Concurrent processing with Go channels
- Non-blocking operations
- Graceful shutdown
- Structured error handling

#### API Endpoints

- `GET /health` - Health check endpoint
- `GET /api/v1/ping` - Basic ping endpoint
- `POST /api/v1/sentiment` - Synchronous sentiment analysis
- `POST /api/v1/stream/submit` - Submit text for asynchronous analysis
- `GET /api/v1/stream/results` - Stream analysis results using SSE

For detailed documentation on the Wellness API, see [apps/wellness/README.md](apps/wellness/README.md).

## Getting Started

### Prerequisites

- Go 1.22 or later
- Git

### Running the Wellness API

1. Navigate to the wellness app directory:
   ```bash
   cd apps/wellness
   ```

2. Install dependencies:
   ```bash
   go mod tidy
   ```

3. Start the server:
   ```bash
   go run cmd/api/main.go
   ```

4. The server will start on `http://localhost:8080`

## Development

### Adding New Applications

1. Create a new directory under `apps/`
2. Initialize a new Go module
3. Add your application code
4. Create a README.md with documentation

### Adding New Features

1. Create a new branch for your feature
2. Make your changes
3. Run tests
4. Submit a pull request

## Testing

To run tests for all applications:
```bash
go test ./...
```

## License

[Add your license here] 