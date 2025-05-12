@echo off
echo Building all microservices...

cd discovery-server
call mvn clean package -DskipTests
cd ..

cd api-gateway
call mvn clean package -DskipTests
cd ..

cd customer-service
call mvn clean package -DskipTests
cd ..

cd food-catalog-service
call mvn clean package -DskipTests
cd ..

cd order-service
call mvn clean package -DskipTests
cd ..

cd delivery-service
call mvn clean package -DskipTests
cd ..

cd inventory-service
call mvn clean package -DskipTests
cd ..

echo All microservices built successfully!
