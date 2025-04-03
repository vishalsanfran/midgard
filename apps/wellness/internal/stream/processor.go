package stream

import (
	"context"
	"log"
	"sync"
	"time"

	"github.com/midgard/apps/wellness/internal/sentiment"
)

// TextItem represents a text item to be processed
type TextItem struct {
	ID        string    `json:"id"`
	Text      string    `json:"text"`
	Timestamp time.Time `json:"timestamp"`
}

// AnalysisResult represents the result of sentiment analysis
type AnalysisResult struct {
	Item      TextItem              `json:"item"`
	Result    sentiment.SentimentResult `json:"result"`
	Processed time.Time             `json:"processed"`
}

// StreamProcessor handles concurrent processing of text items
type StreamProcessor struct {
	inputChan  chan TextItem
	outputChan chan AnalysisResult
	done       chan struct{}
	wg         sync.WaitGroup
	analyzer   *sentiment.Analyzer
	workers    int
}

// NewStreamProcessor creates a new stream processor
func NewStreamProcessor(workers int) *StreamProcessor {
	if workers <= 0 {
		workers = 3 // Default number of workers
	}

	return &StreamProcessor{
		inputChan:  make(chan TextItem, 100),
		outputChan: make(chan AnalysisResult, 100),
		done:       make(chan struct{}),
		analyzer:   sentiment.NewAnalyzer(),
		workers:    workers,
	}
}

// Start begins processing items
func (sp *StreamProcessor) Start(ctx context.Context) {
	log.Printf("Starting stream processor with %d workers", sp.workers)
	
	// Start worker goroutines
	for i := 0; i < sp.workers; i++ {
		sp.wg.Add(1)
		go sp.worker(ctx, i)
	}
	
	// Start a goroutine to close the output channel when all workers are done
	go func() {
		sp.wg.Wait()
		close(sp.outputChan)
	}()
}

// Stop gracefully shuts down the processor
func (sp *StreamProcessor) Stop() {
	close(sp.done)
	sp.wg.Wait()
	log.Println("Stream processor stopped")
}

// Process adds an item to the processing queue
func (sp *StreamProcessor) Process(item TextItem) {
	log.Printf("Processing item: %s with text: %s", item.ID, item.Text)
	
	select {
	case sp.inputChan <- item:
		// Item sent successfully
		log.Printf("Item %s sent to input channel successfully", item.ID)
	default:
		log.Printf("Warning: Input channel full, dropping item: %s", item.ID)
	}
}

// Results returns the channel of analysis results
func (sp *StreamProcessor) Results() <-chan AnalysisResult {
	return sp.outputChan
}

// worker processes items from the input channel
func (sp *StreamProcessor) worker(ctx context.Context, id int) {
	defer sp.wg.Done()
	
	log.Printf("Worker %d started", id)
	
	for {
		select {
		case <-ctx.Done():
			log.Printf("Worker %d received context cancellation", id)
			return
			
		case <-sp.done:
			log.Printf("Worker %d received done signal", id)
			return
			
		case item, ok := <-sp.inputChan:
			if !ok {
				log.Printf("Worker %d: input channel closed", id)
				return
			}
			
			log.Printf("Worker %d processing item: %s", id, item.ID)
			
			// Process the item
			result := sp.analyzer.Analyze(item.Text)
			
			log.Printf("Worker %d analyzed item: %s, sentiment: %s, score: %f", 
				id, item.ID, result.Sentiment, result.Score)
			
			// Send the result
			analysisResult := AnalysisResult{
				Item:      item,
				Result:    result,
				Processed: time.Now(),
			}
			
			select {
			case sp.outputChan <- analysisResult:
				// Result sent successfully
				log.Printf("Worker %d sent result for item: %s", id, item.ID)
			default:
				log.Printf("Warning: Output channel full, dropping result for item: %s", item.ID)
			}
		}
	}
} 