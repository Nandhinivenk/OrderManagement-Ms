# Kitchen Flow Service

This microservice tracks the flow of orders through different statuses in the Cloud Kitchen system.

## Overview

The Kitchen Flow Service monitors the lifecycle of an order from receipt to delivery, tracking each status change and providing a comprehensive view of the order's journey through the system.

## Order Status Flow

Orders move through the following statuses:

1. **RECEIVED** - Initial status when order is placed
2. **CONFIRMED** - Order is confirmed by the kitchen
3. **PREPARING** - Food preparation has started
4. **READY** - Food is ready for pickup
5. **ASSIGNED** - Delivery partner assigned
6. **IN_TRANSIT** - Order is on the way
7. **DELIVERED** - Order has been delivered
8. **CANCELLED** - Order was cancelled (can happen at any stage before delivery)

## Prerequisites

- Java 8 or higher
- Maven 3.6 or higher
- Discovery Server (Eureka) running on port 8761
- Order Service running on port 8083
- Delivery Service running on port 8084

## Building and Running

### Using the Run Script

The easiest way to run the Kitchen Flow Service is to use the provided run script:

```
run-kitchenflow-service.bat
```

### Using Maven Directly

```
mvn clean package
java -jar target/kitchenflow-service-1.0-SNAPSHOT.jar
```

### Using Maven Spring Boot Plugin

```
mvn spring-boot:run
```

## API Endpoints

### Initialize Order Flow
- `POST /api/kitchen-flow/orders/{orderId}/initialize`
- Creates a new order flow tracking entry for the specified order

### Get Order Flow
- `GET /api/kitchen-flow/orders/{orderId}`
- Retrieves the current status and history of an order

### List All Order Flows
- `GET /api/kitchen-flow/orders`
- Lists all order flows in the system

### Filter Order Flows by Status
- `GET /api/kitchen-flow/orders/status/{status}`
- Lists all orders with the specified status

### Filter Order Flows by Customer
- `GET /api/kitchen-flow/orders/customer/{customerId}`
- Lists all orders for a specific customer

### Filter Order Flows by Delivery Person
- `GET /api/kitchen-flow/orders/delivery-person/{deliveryPerson}`
- Lists all orders assigned to a specific delivery person

### Update Order Status
- `PATCH /api/kitchen-flow/orders/{orderId}/status`
- Updates the status of an order
- Request body:
  ```json
  {
    "status": "CONFIRMED",
    "notes": "Order confirmed by kitchen staff"
  }
  ```

### Assign Delivery Person
- `PATCH /api/kitchen-flow/orders/{orderId}/assign`
- Assigns a delivery person to an order
- Request body:
  ```json
  {
    "deliveryPerson": "John Delivery"
  }
  ```

### Get Order Status History
- `GET /api/kitchen-flow/orders/{orderId}/history`
- Retrieves the complete status history of an order

### Delete Order Flow
- `DELETE /api/kitchen-flow/orders/{orderId}`
- Removes an order flow from the system

## Integration with Other Services

The Kitchen Flow Service integrates with:

1. **Order Service** - To fetch order details and update order status
2. **Delivery Service** - To assign delivery persons and update delivery status

## Database

The service uses an H2 in-memory database with the following tables:

1. **order_flows** - Stores the current state of each order
2. **status_updates** - Stores the history of status changes for each order

You can access the H2 console at: http://localhost:8086/h2-console
