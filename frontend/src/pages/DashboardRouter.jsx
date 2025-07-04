// src/pages/DashboardRouter.jsx
import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "../services/api";

const DashboardRouter = () => {
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    axios
      .get("/api/user")
      .then((res) => {
        const role = res.data.role;
        if (role === "STUDENT") navigate("/student");
        else if (role === "DEPT_HEAD") navigate("/dept");
        else if (role === "REGISTRAR") navigate("/registrar");
        else navigate("/login"); // fallback
      })
      .catch((err) => {
        console.error("Failed to fetch user", err);
        navigate("/login");
      })
      .finally(() => setLoading(false));
  }, [navigate]);

  return loading ? <div className="text-center p-8">Loading...</div> : null;
};

export default DashboardRouter;
