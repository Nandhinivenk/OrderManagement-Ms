# Cloud Kitchen Order Management System

A comprehensive order management system for cloud kitchens with modules for customer management, order processing, food item management, delivery tracking, inventory management with QR code generation, and administrative functions.

## Features

- **Customer Module**: Customer registration, login, and profile management
- **Order Module**: Order creation, tracking, and management
- **Food Item Module**: Food item creation, categorization, and menu management
- **Food Item Mapping Module**: Mapping food items to categories/menus
- **Delivery Module**: Delivery tracking and management
- **Inventory Module**: Inventory management with QR code generation for items
- **Admin Module**: Administrative functions and dashboard

## Requirements

- Java 11 or higher
- Maven 3.6 or higher

## Setup and Installation

1. Clone the repository:
   ```
   git clone https://github.com/yourusername/cloud-kitchen-order-management.git
   cd cloud-kitchen-order-management
   ```

2. Build the project with Maven:
   ```
   mvn clean package
   ```

3. Run the application:
   ```
   java -jar target/order-management-1.0-SNAPSHOT.jar
   ```

## Usage

### Customer

1. Register a new account or log in with existing credentials
2. Browse the menu and place orders
3. Track order status
4. View order history

### Admin

1. Log in with admin credentials (default: username: `admin`, password: `admin`)
2. Manage food items, categories, and menus
3. Process and track orders
4. Manage inventory with QR code generation
5. Track deliveries
6. Manage customer accounts

## Database

The application uses an embedded H2 database for data storage. The database file is created in the `data` directory when the application is first run.

## QR Code Generation

The application can generate QR codes for inventory items. The QR codes are saved in the `qrcodes` directory.

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Contributors

- Your Name <your.email@example.com>
