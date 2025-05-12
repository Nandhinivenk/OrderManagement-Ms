# Cloud Kitchen Frontend

This is the frontend application for the Cloud Kitchen Order Management System.

## Prerequisites

- Node.js (v14 or higher)
- npm (v6 or higher)

## Installation

1. Install dependencies:
```bash
npm install
```

## Running the Frontend

1. Start the frontend server:
```bash
npm start
```

2. Open your browser and navigate to:
```
http://localhost:3000
```

## Features

The frontend provides a user interface for:

- Customer management
- Food item management
- Order processing
- Delivery tracking
- Inventory management with QR code generation

## Troubleshooting

### Connection Issues

If you're experiencing connection issues with the backend services:

1. Make sure all microservices are running:
   - Discovery Server (Eureka): http://localhost:8761
   - API Gateway: http://localhost:8080
   - Customer Service: http://localhost:8081
   - Food Catalog Service: http://localhost:8082
   - Order Service: http://localhost:8083
   - Delivery Service: http://localhost:8084
   - Inventory Service: http://localhost:8085

2. Check the API Gateway CORS configuration:
   - The API Gateway should allow requests from http://localhost:3000

3. Verify network connectivity:
   - Open browser developer tools (F12)
   - Check the Network tab for failed requests
   - Look for CORS errors in the Console tab

### Alternative Connection Methods

If you continue to have connection issues:

1. Try using the simple HTML version:
   - Open `index.html` directly in your browser
   - This bypasses the Express server

2. Update the API base URL in the JavaScript files:
   - Open `app.js` or `app-fixed.js`
   - Change the `API_BASE_URL` constant to match your API Gateway URL
