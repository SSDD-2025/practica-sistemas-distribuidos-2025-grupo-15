#!/bin/bash

# Variables
IMAGE_NAME="rubencamach0/bookhive"

# Build
echo "Building Docker image: $IMAGE_NAME"
docker build -t $IMAGE_NAME .
