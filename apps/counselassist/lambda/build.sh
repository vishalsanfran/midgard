#!/bin/bash

# Create deployment package
echo "Creating deployment package..."
rm -rf package
mkdir -p package

# Install dependencies
pip install -r ../requirements.txt --target ./package

# Copy application files
cp ../app.py package/
cp lambda_handler.py package/
cp -r ../models package/

# Create ZIP file
cd package
zip -r ../deployment.zip .
cd ..
echo "Build finished!"