#!/bin/bash

# Variables
IMAGE_NAME="rubencamach0/bookhive:1.0.0"

# Build
echo "Building Docker image: $IMAGE_NAME"
docker build -t $IMAGE_NAME .