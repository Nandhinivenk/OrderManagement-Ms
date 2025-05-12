#!/bin/bash
echo "Starting all microservices with Docker Compose..."

# Build all services
./build-all.sh

# Start Docker Compose
docker-compose up -d

echo "All services started successfully!"
echo "Eureka Dashboard: http://localhost:8761"
echo "API Gateway: http://localhost:8080"
