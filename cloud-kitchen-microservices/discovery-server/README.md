# Discovery Server (Eureka)

This module provides service discovery for the Cloud Kitchen Microservices application using Netflix Eureka.

## Prerequisites

- Java 8 or higher
- Maven 3.6 or higher

## Building and Running

### Option 1: Using the Fix Script

The easiest way to run the Discovery Server is to use the provided fix script:

1. Navigate to the project root directory:
   ```
   cd cloud-kitchen-microservices
   ```

2. Run the fix script:
   ```
   fix-discovery-server.bat
   ```

This script will:
- Set the Maven path if needed
- Build the Discovery Server
- Run the Discovery Server

### Option 2: Using Maven Directly

If you prefer to use Maven commands directly:

1. Navigate to the discovery-server directory:
   ```
   cd cloud-kitchen-microservices/discovery-server
   ```

2. Build the project:
   ```
   mvn clean package
   ```

3. Run the application:
   ```
   java -jar target/discovery-server-1.0-SNAPSHOT.jar
   ```

### Option 3: Using IntelliJ IDEA

1. Open the project in IntelliJ IDEA
2. Navigate to the `DiscoveryServerApplication` class
3. Right-click and select "Run DiscoveryServerApplication"

## Verifying the Server is Running

Once the server is running, you can access the Eureka dashboard at:
```
http://localhost:8761
```

You should see the Eureka dashboard with no instances registered initially.

## Troubleshooting

### Port Already in Use

If port 8761 is already in use, you can change the port in `application.properties`:
```
server.port=8762
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
3. Make sure no other instance of the Discovery Server is already running
