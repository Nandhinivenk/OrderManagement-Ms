// API Base URL
const API_BASE_URL = 'http://localhost:8080/api';

// Customer Service
document.getElementById('customerForm').addEventListener('submit', function(e) {
    e.preventDefault();
    
    const customer = {
        username: document.getElementById('username').value,
        password: document.getElementById('password').value,
        name: document.getElementById('name').value,
        email: document.getElementById('email').value,
        phone: document.getElementById('phone').value,
        address: document.getElementById('address').value
    };
    
    fetch(`${API_BASE_URL}/customers/register`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(customer)
    })
    .then(response => response.json())
    .then(data => {
        alert('Customer added successfully!');
        loadCustomers();
        this.reset();
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Failed to add customer');
    });
});

document.getElementById('refreshCustomers').addEventListener('click', loadCustomers);

function loadCustomers() {
    fetch(`${API_BASE_URL}/customers`)
    .then(response => response.json())
    .then(customers => {
        const tableBody = document.getElementById('customerTableBody');
        tableBody.innerHTML = '';
        
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
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Failed to load customers');
    });
}

function deleteCustomer(id) {
    if (confirm('Are you sure you want to delete this customer?')) {
        fetch(`${API_BASE_URL}/customers/${id}`, {
            method: 'DELETE'
        })
        .then(response => {
            if (response.ok) {
                alert('Customer deleted successfully!');
                loadCustomers();
            } else {
                throw new Error('Failed to delete customer');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Failed to delete customer');
        });
    }
}

// Food Item Service
document.getElementById('foodItemForm').addEventListener('submit', function(e) {
    e.preventDefault();
    
    const foodItem = {
        name: document.getElementById('foodName').value,
        description: document.getElementById('description').value,
        price: parseFloat(document.getElementById('price').value),
        categoryName: document.getElementById('category').value,
        available: document.getElementById('available').checked
    };
    
    fetch(`${API_BASE_URL}/food-items`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(foodItem)
    })
    .then(response => response.json())
    .then(data => {
        alert('Food item added successfully!');
        loadFoodItems();
        this.reset();
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Failed to add food item');
    });
});

document.getElementById('refreshFoodItems').addEventListener('click', loadFoodItems);

function loadFoodItems() {
    fetch(`${API_BASE_URL}/food-items`)
    .then(response => response.json())
    .then(foodItems => {
        const tableBody = document.getElementById('foodItemTableBody');
        tableBody.innerHTML = '';
        
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
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Failed to load food items');
    });
}

function toggleAvailability(id, available) {
    fetch(`${API_BASE_URL}/food-items/${id}/availability?available=${available}`, {
        method: 'PATCH'
    })
    .then(response => response.json())
    .then(data => {
        alert('Food item availability updated!');
        loadFoodItems();
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Failed to update food item availability');
    });
}

function deleteFoodItem(id) {
    if (confirm('Are you sure you want to delete this food item?')) {
        fetch(`${API_BASE_URL}/food-items/${id}`, {
            method: 'DELETE'
        })
        .then(response => {
            if (response.ok) {
                alert('Food item deleted successfully!');
                loadFoodItems();
            } else {
                throw new Error('Failed to delete food item');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Failed to delete food item');
        });
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

document.getElementById('orderForm').addEventListener('submit', function(e) {
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
    
    fetch(`${API_BASE_URL}/orders`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(order)
    })
    .then(response => response.json())
    .then(data => {
        alert('Order created successfully!');
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
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Failed to create order');
    });
});

document.getElementById('refreshOrders').addEventListener('click', loadOrders);

function loadOrders() {
    fetch(`${API_BASE_URL}/orders`)
    .then(response => response.json())
    .then(orders => {
        const tableBody = document.getElementById('orderTableBody');
        tableBody.innerHTML = '';
        
        orders.forEach(order => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${order.id}</td>
                <td>${order.customerName || order.customerId}</td>
                <td>${new Date(order.orderDate).toLocaleString()}</td>
                <td>${order.status}</td>
                <td>$${order.totalAmount.toFixed(2)}</td>
                <td>${order.paymentMethod} (${order.paymentStatus})</td>
                <td>
                    <button class="btn btn-sm btn-primary" onclick="viewOrderDetails(${order.id})">View</button>
                    <button class="btn btn-sm btn-warning" onclick="updateOrderStatus(${order.id})">Update Status</button>
                    <button class="btn btn-sm btn-danger" onclick="cancelOrder(${order.id})">Cancel</button>
                </td>
            `;
            tableBody.appendChild(row);
        });
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Failed to load orders');
    });
}

function viewOrderDetails(id) {
    fetch(`${API_BASE_URL}/orders/${id}`)
    .then(response => response.json())
    .then(order => {
        let itemsHtml = '';
        order.orderItems.forEach(item => {
            itemsHtml += `
                <tr>
                    <td>${item.foodItemName}</td>
                    <td>${item.quantity}</td>
                    <td>$${item.price.toFixed(2)}</td>
                    <td>$${item.subtotal.toFixed(2)}</td>
                </tr>
            `;
        });
        
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
                            <th>$${order.totalAmount.toFixed(2)}</th>
                        </tr>
                    </tfoot>
                </table>
            </div>
        `;
        
        alert(detailsHtml);
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Failed to load order details');
    });
}

function updateOrderStatus(id) {
    const newStatus = prompt('Enter new status (PENDING, PREPARING, READY, DELIVERED, CANCELLED):');
    if (newStatus) {
        fetch(`${API_BASE_URL}/orders/${id}/status`, {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ status: newStatus })
        })
        .then(response => response.json())
        .then(data => {
            alert('Order status updated!');
            loadOrders();
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Failed to update order status');
        });
    }
}

function cancelOrder(id) {
    if (confirm('Are you sure you want to cancel this order?')) {
        fetch(`${API_BASE_URL}/orders/${id}/cancel`, {
            method: 'POST'
        })
        .then(response => response.json())
        .then(data => {
            alert('Order cancelled!');
            loadOrders();
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Failed to cancel order');
        });
    }
}

// Delivery Service
document.getElementById('deliveryForm').addEventListener('submit', function(e) {
    e.preventDefault();
    
    const delivery = {
        orderId: parseInt(document.getElementById('deliveryOrderId').value),
        deliveryAddress: document.getElementById('deliveryAddress').value
    };
    
    fetch(`${API_BASE_URL}/deliveries`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(delivery)
    })
    .then(response => response.json())
    .then(data => {
        alert('Delivery created successfully!');
        loadDeliveries();
        this.reset();
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Failed to create delivery');
    });
});

document.getElementById('assignDeliveryForm').addEventListener('submit', function(e) {
    e.preventDefault();
    
    const deliveryId = parseInt(document.getElementById('deliveryId').value);
    const deliveryPerson = document.getElementById('deliveryPerson').value;
    
    fetch(`${API_BASE_URL}/deliveries/${deliveryId}/assign`, {
        method: 'PATCH',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ deliveryPerson: deliveryPerson })
    })
    .then(response => response.json())
    .then(data => {
        alert('Delivery person assigned successfully!');
        loadDeliveries();
        this.reset();
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Failed to assign delivery person');
    });
});

document.getElementById('refreshDeliveries').addEventListener('click', loadDeliveries);

function loadDeliveries() {
    fetch(`${API_BASE_URL}/deliveries`)
    .then(response => response.json())
    .then(deliveries => {
        const tableBody = document.getElementById('deliveryTableBody');
        tableBody.innerHTML = '';
        
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
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Failed to load deliveries');
    });
}

function updateDeliveryStatus(id) {
    const newStatus = prompt('Enter new status (PENDING, ASSIGNED, IN_TRANSIT, DELIVERED, FAILED):');
    if (newStatus) {
        fetch(`${API_BASE_URL}/deliveries/${id}/status`, {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ deliveryStatus: newStatus })
        })
        .then(response => response.json())
        .then(data => {
            alert('Delivery status updated!');
            loadDeliveries();
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Failed to update delivery status');
        });
    }
}

function markAsDelivered(id) {
    if (confirm('Are you sure you want to mark this delivery as delivered?')) {
        fetch(`${API_BASE_URL}/deliveries/${id}/deliver`, {
            method: 'POST'
        })
        .then(response => response.json())
        .then(data => {
            alert('Delivery marked as delivered!');
            loadDeliveries();
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Failed to mark delivery as delivered');
        });
    }
}

// Inventory Service
document.getElementById('inventoryForm').addEventListener('submit', function(e) {
    e.preventDefault();
    
    const inventoryItem = {
        name: document.getElementById('inventoryName').value,
        quantity: parseInt(document.getElementById('quantity').value),
        unit: document.getElementById('unit').value,
        reorderLevel: parseInt(document.getElementById('reorderLevel').value)
    };
    
    fetch(`${API_BASE_URL}/inventory`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(inventoryItem)
    })
    .then(response => response.json())
    .then(data => {
        alert('Inventory item added successfully!');
        loadInventory();
        this.reset();
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Failed to add inventory item');
    });
});

document.getElementById('refreshInventory').addEventListener('click', loadInventory);

function loadInventory() {
    fetch(`${API_BASE_URL}/inventory`)
    .then(response => response.json())
    .then(items => {
        const tableBody = document.getElementById('inventoryTableBody');
        tableBody.innerHTML = '';
        
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
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Failed to load inventory items');
    });
}

function updateInventoryQuantity(id) {
    const newQuantity = prompt('Enter new quantity:');
    if (newQuantity !== null) {
        fetch(`${API_BASE_URL}/inventory/${id}/quantity`, {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ quantity: parseInt(newQuantity) })
        })
        .then(response => response.json())
        .then(data => {
            alert('Inventory quantity updated!');
            loadInventory();
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Failed to update inventory quantity');
        });
    }
}

function generateQRCode(id) {
    fetch(`${API_BASE_URL}/inventory/${id}/qrcode`, {
        method: 'POST'
    })
    .then(response => response.text())
    .then(path => {
        alert(`QR Code generated! Path: ${path}`);
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Failed to generate QR code');
    });
}

function deleteInventoryItem(id) {
    if (confirm('Are you sure you want to delete this inventory item?')) {
        fetch(`${API_BASE_URL}/inventory/${id}`, {
            method: 'DELETE'
        })
        .then(response => {
            if (response.ok) {
                alert('Inventory item deleted successfully!');
                loadInventory();
            } else {
                throw new Error('Failed to delete inventory item');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Failed to delete inventory item');
        });
    }
}

// Load data when the page loads
document.addEventListener('DOMContentLoaded', function() {
    loadCustomers();
    loadFoodItems();
    loadOrders();
    loadDeliveries();
    loadInventory();
});
