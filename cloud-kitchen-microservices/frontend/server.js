const express = require('express');
const { createProxyMiddleware } = require('http-proxy-middleware');
const path = require('path');
const app = express();
const port = 3000;

// Serve static files
app.use(express.static(__dirname));

// Proxy API requests to the API Gateway
app.use('/api', createProxyMiddleware({
    target: 'http://localhost:8080',
    changeOrigin: true,
    pathRewrite: {
        '^/api': '/api' // No rewrite needed
    },
    onProxyReq: (proxyReq, req, res) => {
        // Log proxy requests
        console.log(`Proxying ${req.method} request to: ${proxyReq.path}`);
    },
    onError: (err, req, res) => {
        console.error('Proxy error:', err);
        res.status(500).json({ error: 'Proxy error', message: err.message });
    }
}));

// Handle all routes by serving index.html
app.get('*', (req, res) => {
    res.sendFile(path.join(__dirname, 'index-fixed.html'));
});

// Start the server
app.listen(port, () => {
    console.log(`
=======================================================
Cloud Kitchen Frontend Server running at http://localhost:${port}
=======================================================

- Open your browser and navigate to: http://localhost:${port}
- All API requests will be proxied to: http://localhost:8080
- Make sure all microservices are running

Press Ctrl+C to stop the server
`);
});
