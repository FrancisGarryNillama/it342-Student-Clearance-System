import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import LoginPage from './pages/LoginPage';
import StudentDashboard from './pages/StudentDashboard';
import RegistrarDashboard from './pages/RegistrarDashboard';
import DepartmentDashboard from './pages/DepartmentDashboard';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<LoginPage />} />

        <Route path="/student" element={<StudentDashboard />} />
        <Route path="/registrar" element={<RegistrarDashboard />} />
        <Route path="/department" element={<DepartmentDashboard />} />

      </Routes>
    </Router>
  );
}

export default App;

