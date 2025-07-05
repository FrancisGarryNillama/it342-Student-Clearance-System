import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080/api', // adjust if backend runs on a different port
  headers: {
    'Content-Type': 'application/json',
  },
});

export default api;
