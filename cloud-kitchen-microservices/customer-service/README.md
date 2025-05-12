# Customer Service

This module provides customer management functionality for the Cloud Kitchen Microservices application.

## Prerequisites

- Java 8 or higher
- Maven 3.6 or higher
- Discovery Server (Eureka) running on port 8761

## Building and Running

### Option 1: Using the Run Script

The easiest way to run the Customer Service is to use the provided run script:

1. Navigate to the customer-service directory:
   ```
   cd cloud-kitchen-microservices/customer-service
   ```

2. Run the script:
   ```
   run-customer-service.bat
   ```

This script will:
- Set the Maven path if needed
- Build the Customer Service
- Run the Customer Service

### Option 2: Using Maven Directly

If you prefer to use Maven commands directly:

1. Navigate to the customer-service directory:
   ```
   cd cloud-kitchen-microservices/customer-service
   ```

2. Build the project:
   ```
   mvn clean package
   ```

3. Run the application:
   ```
   java -jar target/customer-service-1.0-SNAPSHOT.jar
   ```

### Option 3: Using Maven Wrapper

If you've set up Maven Wrapper:

1. Navigate to the customer-service directory:
   ```
   cd cloud-kitchen-microservices/customer-service
   ```

2. Build and run the project:
   ```
   ./mvnw spring-boot:run
   ```

### Option 4: Using IntelliJ IDEA

1. Open the project in IntelliJ IDEA
2. Navigate to the `CustomerServiceApplication` class
3. Right-click and select "Run CustomerServiceApplication"

## API Endpoints

The Customer Service provides the following endpoints:

- `POST /api/customers/register` - Register a new customer
- `POST /api/customers/login` - Authenticate a customer
- `GET /api/customers/{id}` - Get customer by ID
- `GET /api/customers/username/{username}` - Get customer by username
- `GET /api/customers` - Get all customers
- `PUT /api/customers/{id}` - Update customer
- `DELETE /api/customers/{id}` - Delete customer

## Verifying the Service is Running

Once the service is running, you can:

1. Check the Eureka dashboard at http://localhost:8761 to see if the service is registered
2. Access the H2 console at http://localhost:8081/h2-console to view the database
3. Test the API endpoints using Postman or curl

## Troubleshooting

### Port Already in Use

If port 8081 is already in use, you can change the port in `application.properties`:
```
server.port=8091
```

### Build Failures

If you encounter build failures:

1. Make sure you have the correct Java version (8 or higher)
2. Check that Maven is properly installed and in your PATH
3. Try deleting the `target` directory and rebuilding
4. Check for dependency conflicts in the POM files

### Runtime Errors

If the application fails to start:

1. Check the console output for specific error messages
2. Verify that all required dependencies are in the classpath
3. Make sure the Discovery Server (Eureka) is running
4. Check the database configuration in application.properties
