import React, { useState } from 'react';
import { PredictionRequest, PredictionResponse } from '../types/api';

const CounselChat: React.FC = () => {
  const [input, setInput] = useState<string>('');
  const [response, setResponse] = useState<PredictionResponse | null>(null);
  const [loading, setLoading] = useState<boolean>(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);

    try {
      const res = await fetch('http://localhost:8000/predict', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ text: input })
      });
      
      if (!res.ok) throw new Error('API request failed');
      const data: PredictionResponse = await res.json();
      setResponse(data);
    } catch (error) {
      console.error('Error:', error);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="counsel-chat">
      <h1>Mental Health Assistant</h1>
      <form onSubmit={handleSubmit}>
        <textarea
          value={input}
          onChange={(e) => setInput(e.target.value)}
          placeholder="Share what's on your mind..."
          rows={4}
        />
        <button type="submit" disabled={loading}>
          {loading ? 'Analyzing...' : 'Get Response'}
        </button>
      </form>

      {response && (
        <div className="response">
          <div className="interpretation">
            <h3>Analysis Result:</h3>
            <p>{response.interpretation}</p>
          </div>
          <div className="details">
            <h4>Technical Details:</h4>
            <ul>
              <li>Model Version: {response.model_version}</li>
              <li>Timestamp: {new Date(response.timestamp).toLocaleString()}</li>
            </ul>
          </div>
        </div>
      )}
    </div>
  );
};

export default CounselChat;