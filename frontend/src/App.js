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
            {/* 👇 Temporarily render StudentDashboard on root for testing */}
            <Route path="/" element={<StudentDashboard />} />

            {/* 🔒 Keep these as is for later real routing */}
            <Route path="/student" element={<StudentDashboard />} />
            <Route path="/registrar" element={<RegistrarDashboard />} />
            <Route path="/department" element={<DepartmentDashboard />} />
            {/* <Route path="/" element={<LoginPage />} /> */}
          </Routes>
        </Router>
  );
}

export default App;
