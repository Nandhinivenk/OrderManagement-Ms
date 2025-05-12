// Simple proxy server to bypass CORS issues during development
const express = require('express');
const { createProxyMiddleware } = require('http-proxy-middleware');
const app = express();
const port = 3000;

// Serve static files
app.use(express.static('.'));

// Proxy API requests to the API Gateway
app.use('/api', createProxyMiddleware({ 
    target: 'http://localhost:8080',
    changeOrigin: true
}));

app.listen(port, () => {
    console.log(`Frontend server with proxy running at http://localhost:${port}`);
});
