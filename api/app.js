const express = require('express');
const app = express();
const PORT = 8080;
const locationRoutes = require('./routes/location.routes.js');

app.use('/api/locations/', locationRoutes);

app.listen(PORT);

