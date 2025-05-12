# Cloud Kitchen Order Management Microservices

This project is a microservices-based implementation of a Cloud Kitchen Order Management System.

## Architecture

The application is built using a microservices architecture with the following components:

- **Discovery Server**: Eureka server for service discovery
- **API Gateway**: Spring Cloud Gateway for routing requests to appropriate services
- **Customer Service**: Manages customer data and authentication
- **Food Catalog Service**: Manages food items and categories
- **Order Service**: Handles order processing and management
- **Delivery Service**: Manages delivery tracking and assignments
- **Inventory Service**: Manages inventory items with QR code generation
- **Admin Service**: Provides administrative functions

## Prerequisites

- Java 8 or higher
- Maven
- Node.js and npm (for the frontend)
- Docker and Docker Compose (optional, for containerized deployment)

## Building the Application

To build all services, run the following command from the root directory:

```bash
mvn clean package
```

## Running the Application

### Using Maven

You can run each service individually using Maven:

1. Start the Discovery Server first:
```bash
cd discovery-server
mvn spring-boot:run
```

2. Start the API Gateway:
```bash
cd api-gateway
mvn spring-boot:run
```

3. Start the Customer Service:
```bash
cd customer-service
mvn spring-boot:run
```

4. Start the Food Catalog Service:
```bash
cd food-catalog-service
mvn spring-boot:run
```

5. Start other services as needed...

### Using Docker Compose

To run all services using Docker Compose:

```bash
docker-compose up -d
```

## Service Endpoints

### Customer Service (Port 8081)
- POST /api/customers/register - Register a new customer
- POST /api/customers/login - Authenticate a customer
- GET /api/customers/{id} - Get customer by ID
- GET /api/customers/username/{username} - Get customer by username
- GET /api/customers - Get all customers
- PUT /api/customers/{id} - Update customer
- DELETE /api/customers/{id} - Delete customer

### Food Catalog Service (Port 8082)
- POST /api/categories - Create a new category
- GET /api/categories/{id} - Get category by ID
- GET /api/categories/name/{name} - Get category by name
- GET /api/categories - Get all categories
- PUT /api/categories/{id} - Update category
- DELETE /api/categories/{id} - Delete category

- POST /api/food-items - Create a new food item
- GET /api/food-items/{id} - Get food item by ID
- GET /api/food-items - Get all food items
- GET /api/food-items/category/{categoryId} - Get food items by category
- GET /api/food-items/available - Get available food items
- GET /api/food-items/search?keyword={keyword} - Search food items
- PUT /api/food-items/{id} - Update food item
- PATCH /api/food-items/{id}/availability?available={boolean} - Update food item availability
- DELETE /api/food-items/{id} - Delete food item

## API Gateway

All services can be accessed through the API Gateway at http://localhost:8080.

## Service Discovery

The Eureka dashboard is available at http://localhost:8761.

## Running the Frontend

To run the frontend application:

```bash
cd frontend

# Install dependencies
npm install

# Start the frontend server
# On Linux/Mac
./start-frontend.sh
# On Windows
start-frontend.bat
```

Then open your browser and navigate to http://localhost:3000

The frontend provides a user interface for:
- Customer management
- Food item management
- Order processing
- Delivery tracking
- Inventory management with QR code generation

### Order Service (Port 8083)
- POST /api/orders - Create a new order
- GET /api/orders/{id} - Get order by ID
- GET /api/orders - Get all orders
- GET /api/orders/customer/{customerId} - Get orders by customer ID
- GET /api/orders/status/{status} - Get orders by status
- PATCH /api/orders/{id}/status - Update order status
- PATCH /api/orders/{id}/payment-status - Update payment status
- POST /api/orders/{id}/items - Add item to order
- DELETE /api/orders/{orderId}/items/{itemId} - Remove item from order
- POST /api/orders/{id}/cancel - Cancel order
- DELETE /api/orders/{id} - Delete order

### Delivery Service (Port 8084)
- POST /api/deliveries - Create a new delivery
- GET /api/deliveries/{id} - Get delivery by ID
- GET /api/deliveries/order/{orderId} - Get delivery by order ID
- GET /api/deliveries - Get all deliveries
- GET /api/deliveries/status/{status} - Get deliveries by status
- GET /api/deliveries/person/{deliveryPerson} - Get deliveries by delivery person
- PATCH /api/deliveries/{id}/assign - Assign delivery person
- PATCH /api/deliveries/{id}/status - Update delivery status
- POST /api/deliveries/{id}/deliver - Mark delivery as delivered
- DELETE /api/deliveries/{id} - Delete delivery

### Inventory Service (Port 8085)
- POST /api/inventory - Add a new inventory item
- GET /api/inventory/{id} - Get inventory item by ID
- GET /api/inventory/name/{name} - Get inventory item by name
- GET /api/inventory - Get all inventory items
- GET /api/inventory/reorder - Get items to reorder
- PUT /api/inventory/{id} - Update inventory item
- PATCH /api/inventory/{id}/quantity - Update inventory item quantity
- DELETE /api/inventory/{id} - Delete inventory item
- POST /api/inventory/{id}/qrcode - Generate QR code for inventory item

## Future Enhancements

- Implement Admin Service
- Add authentication and authorization with JWT
- Implement inter-service communication using messaging (Kafka/RabbitMQ)
- Add monitoring and logging with ELK stack and Prometheus
- Enhance the frontend application with more features
