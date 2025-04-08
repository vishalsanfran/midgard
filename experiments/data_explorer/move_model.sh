#!/bin/bash

# Get git hash
GIT_HASH=$(git rev-parse --short HEAD)

# Source and target paths
SOURCE_MODEL="counselor_response_classifier.joblib"
TARGET_DIR="../../apps/counselassist/models"
TARGET_MODEL="counselor_response_classifier_${GIT_HASH}.joblib"

# Create target directory if it doesn't exist
mkdir -p "$TARGET_DIR"

# Copy the model file
if [ -f "$SOURCE_MODEL" ]; then
    cp "$SOURCE_MODEL" "${TARGET_DIR}/${TARGET_MODEL}"
    echo "Model moved successfully!"
    echo "Source: $SOURCE_MODEL"
    echo "Target: ${TARGET_DIR}/${TARGET_MODEL}"
else
    echo "Error: Source model file not found: $SOURCE_MODEL"
    exit 1
fi