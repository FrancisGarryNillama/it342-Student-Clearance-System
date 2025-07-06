// src/services/api.js (or src/api.js)
import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080/api',
  withCredentials: true // ✅ needed for Spring session cookies
});


export default api;

