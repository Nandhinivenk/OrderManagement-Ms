# Food Ingredient Mapping Service

This microservice manages the mapping between food items and their required inventory ingredients for the Cloud Kitchen system.

## Overview

The Food Ingredient Mapping Service maintains the relationship between food items in the menu and the inventory ingredients required to prepare them. It helps track how much of each inventory item is needed to prepare a food item, enabling inventory management and availability checks.

## Prerequisites

- Java 8 or higher
- Maven 3.6 or higher
- Discovery Server (Eureka) running on port 8761
- Food Catalog Service running on port 8082
- Inventory Service running on port 8085

## Building and Running

### Using the Run Script

The easiest way to run the Food Ingredient Mapping Service is to use the provided run script:

```
run-foodingredientmapping-service.bat
```

### Using Maven Directly

```
mvn clean package
java -jar target/foodingredientmapping-service-1.0-SNAPSHOT.jar
```

### Using Maven Spring Boot Plugin

```
mvn spring-boot:run
```

## API Endpoints

### Create Mapping
- `POST /api/food-ingredient-mappings`
- Creates a new mapping between a food item and an inventory ingredient
- Request body:
  ```json
  {
    "foodItemId": 1,
    "inventoryItemId": 2,
    "quantityRequired": 0.5
  }
  ```

### Get Mapping by ID
- `GET /api/food-ingredient-mappings/{id}`
- Retrieves a specific mapping by its ID

### Get Mappings for Food Item
- `GET /api/food-ingredient-mappings/food-item/{foodItemId}`
- Lists all ingredients required for a specific food item

### Get Mappings for Inventory Item
- `GET /api/food-ingredient-mappings/inventory-item/{inventoryItemId}`
- Lists all food items that use a specific inventory item

### Get All Mappings
- `GET /api/food-ingredient-mappings`
- Lists all mappings in the system

### Update Mapping
- `PUT /api/food-ingredient-mappings/{id}`
- Updates an existing mapping
- Request body similar to create mapping

### Delete Mapping
- `DELETE /api/food-ingredient-mappings/{id}`
- Removes a mapping from the system

### Calculate Required Inventory
- `GET /api/food-ingredient-mappings/calculate/{foodItemId}?quantity=5`
- Calculates how much of each inventory item is needed to prepare a specific quantity of a food item

### Check Inventory Availability
- `GET /api/food-ingredient-mappings/check-availability/{foodItemId}?quantity=5`
- Checks if there is enough inventory to prepare a specific quantity of a food item

### Update Inventory After Order
- `POST /api/food-ingredient-mappings/update-inventory/{foodItemId}?quantity=5`
- Updates inventory quantities after an order is placed

## Integration with Other Services

The Food Ingredient Mapping Service integrates with:

1. **Food Catalog Service** - To fetch food item details
2. **Inventory Service** - To fetch inventory item details and update quantities

## Database

The service uses an H2 in-memory database with the following table:

- **food_ingredient_mappings** - Stores the mappings between food items and inventory ingredients

You can access the H2 console at: http://localhost:8087/h2-console
