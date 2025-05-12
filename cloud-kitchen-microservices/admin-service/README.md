# Admin Service

This microservice provides administrative functionality for the Cloud Kitchen Order Management System.

## Overview

The Admin Service serves as a centralized dashboard for administrators to manage and monitor all aspects of the Cloud Kitchen system. It integrates with all other microservices to provide a comprehensive view of the system's operations.

## Features

- **Admin Authentication**: Secure login and registration for administrators
- **Order Management**: View and update order statuses
- **Kitchen Flow Monitoring**: Track orders through the kitchen workflow
- **Delivery Management**: Assign delivery partners and track deliveries
- **Customer Management**: View customer information
- **Inventory Management**: Monitor inventory levels and update quantities
- **Dashboard**: Get a summary of system status and key metrics
- **Audit Logging**: Track all administrative actions for accountability

## Prerequisites

- Java 8 or higher
- Maven 3.6 or higher
- Discovery Server (Eureka) running on port 8761
- All other microservices should be running for full functionality

## Building and Running

### Using the Run Script

The easiest way to run the Admin Service is to use the provided run script:

```
run-admin-service.bat
```

### Using Maven Directly

```
mvn clean package
java -jar target/admin-service-1.0-SNAPSHOT.jar
```

### Using Maven Spring Boot Plugin

```
mvn spring-boot:run
```

## API Endpoints

### Authentication

- `POST /api/admin/auth/register` - Register a new admin
- `POST /api/admin/auth/login` - Authenticate an admin and get a JWT token

### Admin Management

- `GET /api/admin/admins` - Get all admins
- `GET /api/admin/admins/{id}` - Get admin by ID
- `GET /api/admin/admins/username/{username}` - Get admin by username
- `PUT /api/admin/admins/{id}` - Update admin
- `DELETE /api/admin/admins/{id}` - Delete admin

### Audit Logs

- `GET /api/admin/audit-logs` - Get all audit logs
- `GET /api/admin/audit-logs/{id}` - Get audit log by ID
- `GET /api/admin/audit-logs/admin/{adminUsername}` - Get audit logs by admin
- `GET /api/admin/audit-logs/entity` - Get audit logs by entity type and ID
- `GET /api/admin/audit-logs/date-range` - Get audit logs by date range

### Dashboard

- `GET /api/admin/dashboard/summary` - Get dashboard summary
- `GET /api/admin/dashboard/customers` - Get all customers
- `GET /api/admin/dashboard/customers/{id}` - Get customer by ID
- `GET /api/admin/dashboard/orders` - Get all orders
- `GET /api/admin/dashboard/orders/{id}` - Get order by ID
- `PATCH /api/admin/dashboard/orders/{id}/status` - Update order status
- `GET /api/admin/dashboard/kitchen-flow` - Get all kitchen flows
- `GET /api/admin/dashboard/kitchen-flow/{orderId}` - Get kitchen flow by order ID
- `GET /api/admin/dashboard/kitchen-flow/status/{status}` - Get kitchen flows by status
- `PATCH /api/admin/dashboard/kitchen-flow/{orderId}/status` - Update kitchen flow status
- `GET /api/admin/dashboard/deliveries` - Get all deliveries
- `GET /api/admin/dashboard/deliveries/{id}` - Get delivery by ID
- `GET /api/admin/dashboard/deliveries/order/{orderId}` - Get delivery by order ID
- `PATCH /api/admin/dashboard/deliveries/{id}/assign` - Assign delivery person
- `PATCH /api/admin/dashboard/deliveries/{id}/status` - Update delivery status
- `GET /api/admin/dashboard/inventory` - Get all inventory items
- `GET /api/admin/dashboard/inventory/{id}` - Get inventory item by ID
- `PATCH /api/admin/dashboard/inventory/{id}/quantity` - Update inventory quantity

## Security

The Admin Service uses JWT (JSON Web Token) for authentication and authorization. All endpoints except for authentication endpoints require a valid JWT token in the Authorization header.

## Database

The service uses an H2 in-memory database with the following tables:

- **admins** - Stores admin user information
- **admin_roles** - Stores admin roles
- **audit_logs** - Stores audit logs of admin actions

You can access the H2 console at: http://localhost:8088/h2-console
