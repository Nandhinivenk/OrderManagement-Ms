// API Base URL - Change this to match your API Gateway URL
const API_BASE_URL = 'http://localhost:8080/api';

// Global loading indicator
const loadingIndicator = document.getElementById('loadingIndicator');
const connectionStatus = document.getElementById('connectionStatus');

// Show loading indicator
function showLoading() {
    loadingIndicator.style.display = 'flex';
}

// Hide loading indicator
function hideLoading() {
    loadingIndicator.style.display = 'none';
}

// Show success message
function showSuccess(elementId, message) {
    const element = document.getElementById(elementId);
    element.className = 'success-message';
    element.textContent = message;
    
    // Clear message after 3 seconds
    setTimeout(() => {
        element.textContent = '';
    }, 3000);
}

// Show error message
function showError(elementId, message) {
    const element = document.getElementById(elementId);
    element.className = 'error-message';
    element.textContent = message;
}

// Check connection to backend services
function checkConnection() {
    fetch(`${API_BASE_URL}/customers`)
        .then(response => {
            if (response.ok) {
                connectionStatus.className = 'alert alert-success';
                connectionStatus.textContent = 'Connected to backend services successfully!';
                
                // Load all data
                loadCustomers();
                loadFoodItems();
                loadOrders();
                loadDeliveries();
                loadInventory();
            } else {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }
        })
        .catch(error => {
            connectionStatus.className = 'alert alert-danger';
            connectionStatus.innerHTML = `
                <strong>Connection Error:</strong> Could not connect to backend services. 
                <br>Error: ${error.message}
                <br><br>
                <strong>Possible solutions:</strong>
                <ul>
                    <li>Make sure all microservices are running</li>
                    <li>Check if the API Gateway is running at ${API_BASE_URL}</li>
                    <li>Verify CORS configuration in the API Gateway</li>
                </ul>
            `;
        });
}

// Enhanced fetch function with error handling
async function fetchWithErrorHandling(url, options = {}) {
    showLoading();
    try {
        const response = await fetch(url, options);
        
        if (!response.ok) {
            // Try to get error message from response
            try {
                const errorData = await response.json();
                throw new Error(errorData.error || `HTTP error! Status: ${response.status}`);
            } catch (e) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }
        }
        
        return await response.json();
    } catch (error) {
        console.error('Fetch error:', error);
        throw error;
    } finally {
        hideLoading();
    }
}

// Customer Service
document.getElementById('customerForm').addEventListener('submit', async function(e) {
    e.preventDefault();
    
    const customer = {
        username: document.getElementById('username').value,
        password: document.getElementById('password').value,
        name: document.getElementById('name').value,
        email: document.getElementById('email').value,
        phone: document.getElementById('phone').value,
        address: document.getElementById('address').value
    };
    
    try {
        await fetchWithErrorHandling(`${API_BASE_URL}/customers/register`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(customer)
        });
        
        showSuccess('customerFormMessage', 'Customer added successfully!');
        loadCustomers();
        this.reset();
    } catch (error) {
        showError('customerFormMessage', `Failed to add customer: ${error.message}`);
    }
});

document.getElementById('refreshCustomers').addEventListener('click', loadCustomers);

async function loadCustomers() {
    try {
        const tableBody = document.getElementById('customerTableBody');
        tableBody.innerHTML = '<tr><td colspan="6" class="text-center">Loading...</td></tr>';
        
        const customers = await fetchWithErrorHandling(`${API_BASE_URL}/customers`);
        
        tableBody.innerHTML = '';
        
        if (customers.length === 0) {
            tableBody.innerHTML = '<tr><td colspan="6" class="text-center">No customers found</td></tr>';
            return;
        }
        
        customers.forEach(customer => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${customer.id}</td>
                <td>${customer.username}</td>
                <td>${customer.name}</td>
                <td>${customer.email}</td>
                <td>${customer.phone || '-'}</td>
                <td>
                    <button class="btn btn-sm btn-danger" onclick="deleteCustomer(${customer.id})">Delete</button>
                </td>
            `;
            tableBody.appendChild(row);
        });
    } catch (error) {
        showError('customerTableMessage', `Failed to load customers: ${error.message}`);
    }
}

async function deleteCustomer(id) {
    if (confirm('Are you sure you want to delete this customer?')) {
        try {
            await fetchWithErrorHandling(`${API_BASE_URL}/customers/${id}`, {
                method: 'DELETE'
            });
            
            showSuccess('customerTableMessage', 'Customer deleted successfully!');
            loadCustomers();
        } catch (error) {
            showError('customerTableMessage', `Failed to delete customer: ${error.message}`);
        }
    }
}

// Food Item Service
document.getElementById('foodItemForm').addEventListener('submit', async function(e) {
    e.preventDefault();
    
    const foodItem = {
        name: document.getElementById('foodName').value,
        description: document.getElementById('description').value,
        price: parseFloat(document.getElementById('price').value),
        categoryName: document.getElementById('category').value,
        available: document.getElementById('available').checked
    };
    
    try {
        await fetchWithErrorHandling(`${API_BASE_URL}/food-items`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(foodItem)
        });
        
        showSuccess('foodItemFormMessage', 'Food item added successfully!');
        loadFoodItems();
        this.reset();
    } catch (error) {
        showError('foodItemFormMessage', `Failed to add food item: ${error.message}`);
    }
});

document.getElementById('refreshFoodItems').addEventListener('click', loadFoodItems);

async function loadFoodItems() {
    try {
        const tableBody = document.getElementById('foodItemTableBody');
        tableBody.innerHTML = '<tr><td colspan="6" class="text-center">Loading...</td></tr>';
        
        const foodItems = await fetchWithErrorHandling(`${API_BASE_URL}/food-items`);
        
        tableBody.innerHTML = '';
        
        if (foodItems.length === 0) {
            tableBody.innerHTML = '<tr><td colspan="6" class="text-center">No food items found</td></tr>';
            return;
        }
        
        foodItems.forEach(item => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${item.id}</td>
                <td>${item.name}</td>
                <td>$${item.price.toFixed(2)}</td>
                <td>${item.categoryName || '-'}</td>
                <td>${item.available ? 'Yes' : 'No'}</td>
                <td>
                    <button class="btn btn-sm btn-warning" onclick="toggleAvailability(${item.id}, ${!item.available})">
                        ${item.available ? 'Mark Unavailable' : 'Mark Available'}
                    </button>
                    <button class="btn btn-sm btn-danger" onclick="deleteFoodItem(${item.id})">Delete</button>
                </td>
            `;
            tableBody.appendChild(row);
        });
    } catch (error) {
        showError('foodItemTableMessage', `Failed to load food items: ${error.message}`);
    }
}

async function toggleAvailability(id, available) {
    try {
        await fetchWithErrorHandling(`${API_BASE_URL}/food-items/${id}/availability?available=${available}`, {
            method: 'PATCH'
        });
        
        showSuccess('foodItemTableMessage', 'Food item availability updated!');
        loadFoodItems();
    } catch (error) {
        showError('foodItemTableMessage', `Failed to update food item availability: ${error.message}`);
    }
}

async function deleteFoodItem(id) {
    if (confirm('Are you sure you want to delete this food item?')) {
        try {
            await fetchWithErrorHandling(`${API_BASE_URL}/food-items/${id}`, {
                method: 'DELETE'
            });
            
            showSuccess('foodItemTableMessage', 'Food item deleted successfully!');
            loadFoodItems();
        } catch (error) {
            showError('foodItemTableMessage', `Failed to delete food item: ${error.message}`);
        }
    }
}

// Order Service
document.getElementById('addOrderItem').addEventListener('click', function() {
    const orderItems = document.getElementById('orderItems');
    const newItem = document.createElement('div');
    newItem.className = 'mb-3 order-item';
    newItem.innerHTML = `
        <div class="row">
            <div class="col-md-6">
                <label class="form-label">Food Item ID</label>
                <input type="number" class="form-control food-item-id" required>
            </div>
            <div class="col-md-6">
                <label class="form-label">Quantity</label>
                <input type="number" class="form-control quantity" min="1" value="1" required>
            </div>
        </div>
    `;
    orderItems.appendChild(newItem);
});

document.getElementById('orderForm').addEventListener('submit', async function(e) {
    e.preventDefault();
    
    const orderItems = [];
    const itemElements = document.querySelectorAll('.order-item');
    
    itemElements.forEach(item => {
        orderItems.push({
            foodItemId: parseInt(item.querySelector('.food-item-id').value),
            quantity: parseInt(item.querySelector('.quantity').value)
        });
    });
    
    const order = {
        customerId: parseInt(document.getElementById('customerId').value),
        paymentMethod: document.getElementById('paymentMethod').value,
        orderItems: orderItems
    };
    
    try {
        await fetchWithErrorHandling(`${API_BASE_URL}/orders`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(order)
        });
        
        showSuccess('orderFormMessage', 'Order created successfully!');
        loadOrders();
        this.reset();
        // Reset order items to just one
        document.getElementById('orderItems').innerHTML = `
            <div class="mb-3 order-item">
                <div class="row">
                    <div class="col-md-6">
                        <label class="form-label">Food Item ID</label>
                        <input type="number" class="form-control food-item-id" required>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label">Quantity</label>
                        <input type="number" class="form-control quantity" min="1" value="1" required>
                    </div>
                </div>
            </div>
        `;
    } catch (error) {
        showError('orderFormMessage', `Failed to create order: ${error.message}`);
    }
});

document.getElementById('refreshOrders').addEventListener('click', loadOrders);

async function loadOrders() {
    try {
        const tableBody = document.getElementById('orderTableBody');
        tableBody.innerHTML = '<tr><td colspan="7" class="text-center">Loading...</td></tr>';
        
        const orders = await fetchWithErrorHandling(`${API_BASE_URL}/orders`);
        
        tableBody.innerHTML = '';
        
        if (orders.length === 0) {
            tableBody.innerHTML = '<tr><td colspan="7" class="text-center">No orders found</td></tr>';
            return;
        }
        
        orders.forEach(order => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${order.id}</td>
                <td>${order.customerName || order.customerId}</td>
                <td>${new Date(order.orderDate).toLocaleString()}</td>
                <td>${order.status}</td>
                <td>$${order.totalAmount ? order.totalAmount.toFixed(2) : '0.00'}</td>
                <td>${order.paymentMethod} (${order.paymentStatus})</td>
                <td>
                    <button class="btn btn-sm btn-primary" onclick="viewOrderDetails(${order.id})">View</button>
                    <button class="btn btn-sm btn-warning" onclick="updateOrderStatus(${order.id})">Update Status</button>
                    <button class="btn btn-sm btn-danger" onclick="cancelOrder(${order.id})">Cancel</button>
                </td>
            `;
            tableBody.appendChild(row);
        });
    } catch (error) {
        showError('orderTableMessage', `Failed to load orders: ${error.message}`);
    }
}

async function viewOrderDetails(id) {
    try {
        const order = await fetchWithErrorHandling(`${API_BASE_URL}/orders/${id}`);
        
        let itemsHtml = '';
        if (order.orderItems && order.orderItems.length > 0) {
            order.orderItems.forEach(item => {
                itemsHtml += `
                    <tr>
                        <td>${item.foodItemName || item.foodItemId}</td>
                        <td>${item.quantity}</td>
                        <td>$${item.price ? item.price.toFixed(2) : '0.00'}</td>
                        <td>$${item.subtotal ? item.subtotal.toFixed(2) : '0.00'}</td>
                    </tr>
                `;
            });
        } else {
            itemsHtml = '<tr><td colspan="4" class="text-center">No items in this order</td></tr>';
        }
        
        const detailsHtml = `
            <div>
                <h5>Order #${order.id}</h5>
                <p><strong>Customer:</strong> ${order.customerName || order.customerId}</p>
                <p><strong>Date:</strong> ${new Date(order.orderDate).toLocaleString()}</p>
                <p><strong>Status:</strong> ${order.status}</p>
                <p><strong>Payment:</strong> ${order.paymentMethod} (${order.paymentStatus})</p>
                <h6>Items:</h6>
                <table class="table table-sm">
                    <thead>
                        <tr>
                            <th>Item</th>
                            <th>Quantity</th>
                            <th>Price</th>
                            <th>Subtotal</th>
                        </tr>
                    </thead>
                    <tbody>
                        ${itemsHtml}
                    </tbody>
                    <tfoot>
                        <tr>
                            <th colspan="3" class="text-end">Total:</th>
                            <th>$${order.totalAmount ? order.totalAmount.toFixed(2) : '0.00'}</th>
                        </tr>
                    </tfoot>
                </table>
            </div>
        `;
        
        document.getElementById('orderDetailsContent').innerHTML = detailsHtml;
        
        // Show the modal
        const orderDetailsModal = new bootstrap.Modal(document.getElementById('orderDetailsModal'));
        orderDetailsModal.show();
    } catch (error) {
        showError('orderTableMessage', `Failed to load order details: ${error.message}`);
    }
}

async function updateOrderStatus(id) {
    const newStatus = prompt('Enter new status (PENDING, PREPARING, READY, DELIVERED, CANCELLED):');
    if (newStatus) {
        try {
            await fetchWithErrorHandling(`${API_BASE_URL}/orders/${id}/status`, {
                method: 'PATCH',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ status: newStatus })
            });
            
            showSuccess('orderTableMessage', 'Order status updated!');
            loadOrders();
        } catch (error) {
            showError('orderTableMessage', `Failed to update order status: ${error.message}`);
        }
    }
}

async function cancelOrder(id) {
    if (confirm('Are you sure you want to cancel this order?')) {
        try {
            await fetchWithErrorHandling(`${API_BASE_URL}/orders/${id}/cancel`, {
                method: 'POST'
            });
            
            showSuccess('orderTableMessage', 'Order cancelled!');
            loadOrders();
        } catch (error) {
            showError('orderTableMessage', `Failed to cancel order: ${error.message}`);
        }
    }
}

// Delivery Service
document.getElementById('deliveryForm').addEventListener('submit', async function(e) {
    e.preventDefault();
    
    const delivery = {
        orderId: parseInt(document.getElementById('deliveryOrderId').value),
        deliveryAddress: document.getElementById('deliveryAddress').value
    };
    
    try {
        await fetchWithErrorHandling(`${API_BASE_URL}/deliveries`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(delivery)
        });
        
        showSuccess('deliveryFormMessage', 'Delivery created successfully!');
        loadDeliveries();
        this.reset();
    } catch (error) {
        showError('deliveryFormMessage', `Failed to create delivery: ${error.message}`);
    }
});

document.getElementById('assignDeliveryForm').addEventListener('submit', async function(e) {
    e.preventDefault();
    
    const deliveryId = parseInt(document.getElementById('deliveryId').value);
    const deliveryPerson = document.getElementById('deliveryPerson').value;
    
    try {
        await fetchWithErrorHandling(`${API_BASE_URL}/deliveries/${deliveryId}/assign`, {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ deliveryPerson: deliveryPerson })
        });
        
        showSuccess('assignDeliveryFormMessage', 'Delivery person assigned successfully!');
        loadDeliveries();
        this.reset();
    } catch (error) {
        showError('assignDeliveryFormMessage', `Failed to assign delivery person: ${error.message}`);
    }
});

document.getElementById('refreshDeliveries').addEventListener('click', loadDeliveries);

async function loadDeliveries() {
    try {
        const tableBody = document.getElementById('deliveryTableBody');
        tableBody.innerHTML = '<tr><td colspan="6" class="text-center">Loading...</td></tr>';
        
        const deliveries = await fetchWithErrorHandling(`${API_BASE_URL}/deliveries`);
        
        tableBody.innerHTML = '';
        
        if (deliveries.length === 0) {
            tableBody.innerHTML = '<tr><td colspan="6" class="text-center">No deliveries found</td></tr>';
            return;
        }
        
        deliveries.forEach(delivery => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${delivery.id}</td>
                <td>${delivery.orderId}</td>
                <td>${delivery.deliveryPerson || '-'}</td>
                <td>${delivery.deliveryStatus}</td>
                <td>${delivery.deliveryAddress}</td>
                <td>
                    <button class="btn btn-sm btn-warning" onclick="updateDeliveryStatus(${delivery.id})">Update Status</button>
                    <button class="btn btn-sm btn-success" onclick="markAsDelivered(${delivery.id})">Mark Delivered</button>
                </td>
            `;
            tableBody.appendChild(row);
        });
    } catch (error) {
        showError('deliveryTableMessage', `Failed to load deliveries: ${error.message}`);
    }
}

async function updateDeliveryStatus(id) {
    const newStatus = prompt('Enter new status (PENDING, ASSIGNED, IN_TRANSIT, DELIVERED, FAILED):');
    if (newStatus) {
        try {
            await fetchWithErrorHandling(`${API_BASE_URL}/deliveries/${id}/status`, {
                method: 'PATCH',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ deliveryStatus: newStatus })
            });
            
            showSuccess('deliveryTableMessage', 'Delivery status updated!');
            loadDeliveries();
        } catch (error) {
            showError('deliveryTableMessage', `Failed to update delivery status: ${error.message}`);
        }
    }
}

async function markAsDelivered(id) {
    if (confirm('Are you sure you want to mark this delivery as delivered?')) {
        try {
            await fetchWithErrorHandling(`${API_BASE_URL}/deliveries/${id}/deliver`, {
                method: 'POST'
            });
            
            showSuccess('deliveryTableMessage', 'Delivery marked as delivered!');
            loadDeliveries();
        } catch (error) {
            showError('deliveryTableMessage', `Failed to mark delivery as delivered: ${error.message}`);
        }
    }
}

// Inventory Service
document.getElementById('inventoryForm').addEventListener('submit', async function(e) {
    e.preventDefault();
    
    const inventoryItem = {
        name: document.getElementById('inventoryName').value,
        quantity: parseInt(document.getElementById('quantity').value),
        unit: document.getElementById('unit').value,
        reorderLevel: parseInt(document.getElementById('reorderLevel').value)
    };
    
    try {
        await fetchWithErrorHandling(`${API_BASE_URL}/inventory`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(inventoryItem)
        });
        
        showSuccess('inventoryFormMessage', 'Inventory item added successfully!');
        loadInventory();
        this.reset();
    } catch (error) {
        showError('inventoryFormMessage', `Failed to add inventory item: ${error.message}`);
    }
});

document.getElementById('refreshInventory').addEventListener('click', loadInventory);

async function loadInventory() {
    try {
        const tableBody = document.getElementById('inventoryTableBody');
        tableBody.innerHTML = '<tr><td colspan="6" class="text-center">Loading...</td></tr>';
        
        const items = await fetchWithErrorHandling(`${API_BASE_URL}/inventory`);
        
        tableBody.innerHTML = '';
        
        if (items.length === 0) {
            tableBody.innerHTML = '<tr><td colspan="6" class="text-center">No inventory items found</td></tr>';
            return;
        }
        
        items.forEach(item => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${item.id}</td>
                <td>${item.name}</td>
                <td>${item.quantity}</td>
                <td>${item.unit}</td>
                <td>${item.reorderLevel}</td>
                <td>
                    <button class="btn btn-sm btn-primary" onclick="updateInventoryQuantity(${item.id})">Update Quantity</button>
                    <button class="btn btn-sm btn-info" onclick="generateQRCode(${item.id})">Generate QR</button>
                    <button class="btn btn-sm btn-danger" onclick="deleteInventoryItem(${item.id})">Delete</button>
                </td>
            `;
            tableBody.appendChild(row);
        });
    } catch (error) {
        showError('inventoryTableMessage', `Failed to load inventory items: ${error.message}`);
    }
}

async function updateInventoryQuantity(id) {
    const newQuantity = prompt('Enter new quantity:');
    if (newQuantity !== null) {
        try {
            await fetchWithErrorHandling(`${API_BASE_URL}/inventory/${id}/quantity`, {
                method: 'PATCH',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ quantity: parseInt(newQuantity) })
            });
            
            showSuccess('inventoryTableMessage', 'Inventory quantity updated!');
            loadInventory();
        } catch (error) {
            showError('inventoryTableMessage', `Failed to update inventory quantity: ${error.message}`);
        }
    }
}

async function generateQRCode(id) {
    try {
        const path = await fetchWithErrorHandling(`${API_BASE_URL}/inventory/${id}/qrcode`, {
            method: 'POST'
        });
        
        showSuccess('inventoryTableMessage', `QR Code generated! Path: ${path}`);
    } catch (error) {
        showError('inventoryTableMessage', `Failed to generate QR code: ${error.message}`);
    }
}

async function deleteInventoryItem(id) {
    if (confirm('Are you sure you want to delete this inventory item?')) {
        try {
            await fetchWithErrorHandling(`${API_BASE_URL}/inventory/${id}`, {
                method: 'DELETE'
            });
            
            showSuccess('inventoryTableMessage', 'Inventory item deleted successfully!');
            loadInventory();
        } catch (error) {
            showError('inventoryTableMessage', `Failed to delete inventory item: ${error.message}`);
        }
    }
}

// Check connection when the page loads
document.addEventListener('DOMContentLoaded', function() {
    checkConnection();
});
