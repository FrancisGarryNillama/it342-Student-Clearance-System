import React, { useEffect, useState } from "react";
import Navbar from "../shared/Navbar";
import TaskList from "../tasks/TaskList";
import axios from "../../services/api";

const StudentDashboard = () => {
  const [user, setUser] = useState({});
  const [tasks, setTasks] = useState([]);

  useEffect(() => {
    axios.get("/api/user").then((res) => setUser(res.data));
    axios.get("/api/student/clearance-tasks").then((res) => setTasks(res.data));
  }, []);

  return (
    <div>
      <Navbar fullName={user.fullName || "Student"} />
      <div className="p-6">
        <h2 className="text-xl font-bold mb-4">📝 Your Clearance Tasks</h2>
        <TaskList tasks={tasks} />
      </div>
    </div>
  );
};

export default StudentDashboard;
