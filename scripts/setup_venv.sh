#!/bin/bash

# Get the directory from where the script is called
CALLING_DIR=$(pwd)

# Create virtual environment if it doesn't exist
if [ ! -d ".venv" ]; then
    echo "Creating virtual environment in $CALLING_DIR..."
    python3 -m venv .venv
else
    echo "Virtual environment already exists in $CALLING_DIR"
fi

# Activate virtual environment
source .venv/bin/activate

# Check for requirements.txt in current directory, then check root if not found
if [ -f "requirements.txt" ]; then
    REQUIREMENTS_FILE="requirements.txt"
elif [ -f "../../requirements.txt" ]; then
    REQUIREMENTS_FILE="../../requirements.txt"
else
    echo "No requirements.txt found in current directory or root"
    exit 1
fi

# Install requirements
echo "Installing requirements from $REQUIREMENTS_FILE..."
pip install -r $REQUIREMENTS_FILE

echo "Setup complete! Use 'source .venv/bin/activate' to activate the virtual environment"