package stream

import (
	"context"
	"testing"
	"time"

	"github.com/midgard/apps/wellness/internal/sentiment"
)

func TestNewStreamProcessor(t *testing.T) {
	tests := []struct {
		name    string
		workers int
		want    int
	}{
		{
			name:    "Valid number of workers",
			workers: 5,
			want:    5,
		},
		{
			name:    "Zero workers (should use default)",
			workers: 0,
			want:    3, // Default value
		},
		{
			name:    "Negative workers (should use default)",
			workers: -1,
			want:    3, // Default value
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			sp := NewStreamProcessor(tt.workers)
			if sp == nil {
				t.Fatal("NewStreamProcessor() returned nil")
			}
			if sp.workers != tt.want {
				t.Errorf("NewStreamProcessor() workers = %v, want %v", sp.workers, tt.want)
			}
			if sp.analyzer == nil {
				t.Error("NewStreamProcessor() analyzer is nil")
			}
		})
	}
}

func TestProcessAndResults(t *testing.T) {
	// Create a stream processor with 1 worker for testing
	sp := NewStreamProcessor(1)
	
	// Create a context with cancellation
	ctx, cancel := context.WithCancel(context.Background())
	defer cancel()
	
	// Start the processor
	sp.Start(ctx)
	
	// Create a test item
	item := TextItem{
		ID:        "test-item-1",
		Text:      "I really enjoyed this product, it's amazing!",
		Timestamp: time.Now(),
	}
	
	// Process the item
	sp.Process(item)
	
	// Get the results channel
	resultsChan := sp.Results()
	
	// Wait for a result with a timeout
	select {
	case result, ok := <-resultsChan:
		if !ok {
			t.Fatal("Results channel closed unexpectedly")
		}
		
		// Check the result
		if result.Item.ID != item.ID {
			t.Errorf("Process() item ID = %v, want %v", result.Item.ID, item.ID)
		}
		if result.Item.Text != item.Text {
			t.Errorf("Process() item text = %v, want %v", result.Item.Text, item.Text)
		}
		if result.Result.Sentiment != "positive" {
			t.Errorf("Process() sentiment = %v, want %v", result.Result.Sentiment, "positive")
		}
		expectedScore := 0.14285714285714285 // 2 positive words / 14 total words
		if result.Result.Score < expectedScore-0.0001 || result.Result.Score > expectedScore+0.0001 {
			t.Errorf("Process() score = %v, want %v", result.Result.Score, expectedScore)
		}
	case <-time.After(2 * time.Second):
		t.Fatal("Timeout waiting for result")
	}
	
	// Stop the processor
	sp.Stop()
}

func TestMultipleItems(t *testing.T) {
	// Create a stream processor with 2 workers for testing
	sp := NewStreamProcessor(2)
	
	// Create a context with cancellation
	ctx, cancel := context.WithCancel(context.Background())
	defer cancel()
	
	// Start the processor
	sp.Start(ctx)
	
	// Create test items
	items := []TextItem{
		{
			ID:        "test-item-1",
			Text:      "I really enjoyed this product, it's amazing!",
			Timestamp: time.Now(),
		},
		{
			ID:        "test-item-2",
			Text:      "This product is terrible, I hate it!",
			Timestamp: time.Now(),
		},
		{
			ID:        "test-item-3",
			Text:      "This is a regular product.",
			Timestamp: time.Now(),
		},
	}
	
	// Process the items
	for _, item := range items {
		sp.Process(item)
	}
	
	// Get the results channel
	resultsChan := sp.Results()
	
	// Collect results with a timeout
	results := make(map[string]sentiment.SentimentResult)
	timeout := time.After(5 * time.Second)
	
	for i := 0; i < len(items); i++ {
		select {
		case result, ok := <-resultsChan:
			if !ok {
				t.Fatal("Results channel closed unexpectedly")
			}
			results[result.Item.ID] = result.Result
		case <-timeout:
			t.Fatalf("Timeout waiting for results, got %d of %d", i, len(items))
		}
	}
	
	// Check the results
	if len(results) != len(items) {
		t.Errorf("Expected %d results, got %d", len(items), len(results))
	}
	
	// Check specific results
	if result, ok := results["test-item-1"]; ok {
		if result.Sentiment != "positive" {
			t.Errorf("Item 1 sentiment = %v, want %v", result.Sentiment, "positive")
		}
		expectedScore := 0.14285714285714285 // 2 positive words / 14 total words
		if result.Score < expectedScore-0.0001 || result.Score > expectedScore+0.0001 {
			t.Errorf("Item 1 score = %v, want %v", result.Score, expectedScore)
		}
	} else {
		t.Error("Result for item 1 not found")
	}
	
	if result, ok := results["test-item-2"]; ok {
		if result.Sentiment != "negative" {
			t.Errorf("Item 2 sentiment = %v, want %v", result.Sentiment, "negative")
		}
		expectedScore := -0.14285714285714285 // 2 negative words / 14 total words
		if result.Score < expectedScore-0.0001 || result.Score > expectedScore+0.0001 {
			t.Errorf("Item 2 score = %v, want %v", result.Score, expectedScore)
		}
	} else {
		t.Error("Result for item 2 not found")
	}
	
	if result, ok := results["test-item-3"]; ok {
		if result.Sentiment != "neutral" {
			t.Errorf("Item 3 sentiment = %v, want %v", result.Sentiment, "neutral")
		}
		expectedScore := 0.0 // No sentiment words
		if result.Score < expectedScore-0.0001 || result.Score > expectedScore+0.0001 {
			t.Errorf("Item 3 score = %v, want %v", result.Score, expectedScore)
		}
	} else {
		t.Error("Result for item 3 not found")
	}
	
	// Stop the processor
	sp.Stop()
} 