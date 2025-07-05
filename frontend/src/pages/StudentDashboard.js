import React, { useEffect, useState } from 'react';
import './StudentDashboard.css';

function StudentDashboard() {
  const [student, setStudent] = useState(null);
  const [clearance, setClearance] = useState([]);
  const [notifications, setNotifications] = useState([]);

  useEffect(() => {
    setStudent({
      name: "Test User",
      role: "STUDENT",
      id: "123456",
      graduationYear: "2025",
      updatedAt: "Now"
    });

    setClearance([
      { department: "Accounting", status: "Approved", comment: "Cleared" },
      { department: "Library", status: "Pending", comment: "Return overdue books" }
    ]);

    setNotifications([
      "Your clearance for Accounting was approved.",
      "Library requires action."
    ]);
  }, []);


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
        <div className="input-card">Option 1</div>

        <label>Upload Docs:</label>
        <div className="input-card">Choose File</div>

        <div className="button-row">
          <button className="print-btn">🖨️</button>
          <button className="submit-btn">Submit 📤</button>
        </div>
      </div>
    </div>
  );
}

export default StudentDashboard;


