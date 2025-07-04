import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import DashboardRouter from "./pages/DashboardRouter";
import LoginPage from "./pages/LoginPage";

import StudentDashboard from "./components/student/StudentDashboard";
import DeptDashboard from "./components/dept/DeptDashboard";
import RegistrarDashboard from "./components/registrar/RegistrarDashboard";

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<DashboardRouter />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/student" element={<StudentDashboard />} />
        <Route path="/dept" element={<DeptDashboard />} />
        <Route path="/registrar" element={<RegistrarDashboard />} />
      </Routes>
    </Router>
  );
}

export default App;
