import React, { useEffect, useState } from 'react';
import './StudentDashboard.css';

function StudentDashboard() {
  const [student, setStudent] = useState(null);
  const [clearance, setClearance] = useState([]);
  const [notifications, setNotifications] = useState([]);

  const [requestType, setRequestType] = useState('');
  const [file, setFile] = useState(null);

  useEffect(() => {
    // Dummy data for now
    setStudent({
      name: "Test User",
      role: "Student",
      id: "202501234",
      graduationYear: "2025",
      updatedAt: "2025-07-06 09:12 AM"
    });

    setClearance([
      { department: "Dept A", status: "Approved", comment: "Comment text here." },
      { department: "Dept B", status: "Pending", comment: "Comment text here." }
    ]);

    setNotifications([
      "New notification 1",
      "New notification 2"
    ]);
  }, []);

  const handleSubmit = async () => {
    if (!requestType || !file) {
      alert("Please select a type and upload a file.");
      return;
    }

    const formData = new FormData();
    formData.append("type", requestType);
    formData.append("file", file);

    try {
      const response = await fetch("http://localhost:8080/api/student/clearance-request", {
        method: "POST",
        body: formData,
        credentials: "include"
      });

      if (response.ok) {
        alert("Clearance request submitted!");
      } else {
        const err = await response.text();
        alert("Submission failed: " + err);
      }
    } catch (error) {
      console.error("Error submitting request:", error);
      alert("Something went wrong.");
    }
  };

  if (!student) return <p>Loading...</p>;

  return (
    <div className="student-dashboard">
      <header className="dashboard-header">
        <input type="text" className="search-bar" placeholder="Search" />
        <div className="profile-section">
          <span className="notif-icon">🔔</span>
          <div className="profile">
            <img src="/images/profile.png" alt="Profile" />
            <div>
              <strong>{student.name}</strong>
              <p>{student.role}</p>
            </div>
          </div>
        </div>
      </header>

      <h1>Student Dashboard</h1>

      <div className="card-row">
        <div className="info-card">
          <p><strong>Student Info:</strong> {student.name}, {student.id}</p>
          <p className="subtle">Graduation Year: {student.graduationYear}</p>
        </div>
        <div className="info-card">
          <p><strong>Overall Status</strong></p>
          <p className="status in-progress">In Progress</p>
          <p className="subtle">Updated: {student.updatedAt}</p>
        </div>
        <div className="info-card">
          <p><strong>Notifications</strong></p>
          {notifications.map((note, i) => (
            <p className="notif" key={i}>{note}</p>
          ))}
        </div>
      </div>

      <h2>Clearance Progress Table</h2>
      <table className="progress-table">
        <thead>
          <tr>
            <th>Department</th>
            <th>Status</th>
            <th>Comments</th>
            <th>Action</th>
          </tr>
        </thead>
        <tbody>
          {clearance.map((row, i) => (
            <tr key={i}>
              <td>{row.department}</td>
              <td><span className={`badge ${row.status.toLowerCase()}`}>{row.status}</span></td>
              <td>{row.comment}</td>
              <td><a href="#">View</a></td>
            </tr>
          ))}
        </tbody>
      </table>

      <h2>Submit Clearance Request</h2>
      <div className="submit-form">
        <label>Select Type:</label>
        <select
          className="input-card"
          value={requestType}
          onChange={(e) => setRequestType(e.target.value)}
        >
          <option value="">-- Choose Type --</option>
          <option value="graduation">Graduation Clearance</option>
          <option value="exit">Exit Clearance</option>
        </select>

        <label>Upload Docs:</label>
        <input
          type="file"
          className="input-card"
          onChange={(e) => setFile(e.target.files[0])}
        />

        <div className="button-row">
          <button className="print-btn">🖨️</button>
          <button className="submit-btn" onClick={handleSubmit}>Submit 📤</button>
        </div>
      </div>
    </div>
  );
}

export default StudentDashboard;



