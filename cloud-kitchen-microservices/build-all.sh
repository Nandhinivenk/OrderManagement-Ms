#!/bin/bash
echo "Building all microservices..."

cd discovery-server
mvn clean package -DskipTests
cd ..

cd api-gateway
mvn clean package -DskipTests
cd ..

cd customer-service
mvn clean package -DskipTests
cd ..

cd food-catalog-service
mvn clean package -DskipTests
cd ..

cd order-service
mvn clean package -DskipTests
cd ..

cd delivery-service
mvn clean package -DskipTests
cd ..

cd inventory-service
mvn clean package -DskipTests
cd ..

echo "All microservices built successfully!"
