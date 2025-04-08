from fastapi import FastAPI, BackgroundTasks, HTTPException
from pydantic import BaseModel
import joblib
import numpy as np
from datetime import datetime
import json
from pathlib import Path
import logging
from typing import Dict, Optional

# Setup logging
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

class ModelService:
    def __init__(self):
        self.models: Dict[str, any] = {}
        self.current_version = "v1"
        self.metrics = {
            "predictions": 0,
            "errors": 0,
            "avg_confidence": 0.0,
            "last_updated": None
        }
        self.load_models()

    def load_models(self):
        models_dir = Path("models")
        for model_path in models_dir.glob("*.joblib"):
            version = model_path.stem.split("_")[-1]
            self.models[version] = joblib.load(model_path)
            logger.info(f"Loaded model version: {version}")

    def get_model(self, version: Optional[str] = None):
        return self.models[version or self.current_version]

    def update_metrics(self, confidence: float):
        self.metrics["predictions"] += 1
        n = self.metrics["predictions"]
        self.metrics["avg_confidence"] = (
            (self.metrics["avg_confidence"] * (n-1) + confidence) / n
        )

class PredictionRequest(BaseModel):
    text: str
    model_version: Optional[str] = None

class PredictionResponse(BaseModel):
    prediction: float
    confidence: float
    model_version: str
    timestamp: str

app = FastAPI(
    title="CounselAssist API",
    description="Mental health counseling response prediction API",
    version="1.0.0"
)

model_service = ModelService()

@app.post("/predict", 
    response_model=PredictionResponse,
    tags=["predictions"])
async def predict(
    request: PredictionRequest,
    background_tasks: BackgroundTasks
):
    try:
        model = model_service.get_model(request.model_version)
        probability = model.predict_proba([request.text])[0][1]
        confidence = np.max(model.predict_proba([request.text])[0])
        
        background_tasks.add_task(model_service.update_metrics, confidence)
        
        return PredictionResponse(
            prediction=float(probability),
            confidence=float(confidence),
            model_version=request.model_version or model_service.current_version,
            timestamp=datetime.now().isoformat()
        )
    except Exception as e:
        logger.error(f"Prediction error: {str(e)}")
        model_service.metrics["errors"] += 1
        raise HTTPException(status_code=500, detail=str(e))

@app.get("/metrics", tags=["monitoring"])
async def get_metrics():
    return model_service.metrics

@app.post("/models/update", tags=["model-management"])
async def update_model(version: str):
    try:
        new_model_path = f"models/counselor_response_classifier_{version}.joblib"
        model_service.models[version] = joblib.load(new_model_path)
        model_service.current_version = version
        model_service.metrics["last_updated"] = datetime.now().isoformat()
        return {"message": f"Successfully updated to model version {version}"}
    except Exception as e:
        raise HTTPException(status_code=400, detail=str(e))

@app.get("/models/versions", tags=["model-management"])
async def list_models():
    return {
        "current_version": model_service.current_version,
        "available_versions": list(model_service.models.keys())
    }

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=8000)