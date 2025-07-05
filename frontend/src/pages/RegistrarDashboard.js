import React, { useEffect, useState } from 'react';
import './RegistrarDashboard.css';

function RegistrarDashboard() {
  const [user, setUser] = useState(null);
  const [requests, setRequests] = useState([]);

  useEffect(() => {
    setUser({
      name: 'Megan Fox',
      role: 'Admin'
    });

    setRequests([
      {
        name: 'Student A',
        id: 'ID123',
        status: 'Completed',
        progress: '100%'
      },
      {
        name: 'Student B',
        id: 'ID234',
        status: 'In Progress',
        progress: '50%'
      }
    ]);
  }, []);

  if (!user) return <p>Loading...</p>;

  return (
    <div className="registrar-dashboard">
      <header className="dashboard-header">
        <input type="text" className="search-bar" placeholder="Search" />
        <div className="profile-section">
          <span className="notif-icon">🔔</span>
          <div className="profile">
            <img src="/images/profile.png" alt="Profile" />
            <div>
              <strong>{user.name}</strong>
              <p>{user.role}</p>
            </div>
          </div>
        </div>
      </header>

      <h1>Registrar Dashboard</h1>

      <div className="card-row">
        <div className="info-card">
          <p><strong>Registrar Overview</strong></p>
          <p className="subtle">Monitor all clearance processes.</p>
        </div>
        <div className="info-card">
          <p><strong>System Alerts</strong></p>
          <p className="subtle">No new alerts</p>
        </div>
      </div>

      <h2>Pending Requests for Review</h2>
      <table className="dashboard-table">
        <thead>
          <tr>
            <th>Student Name</th>
            <th>Student ID</th>
            <th>Overall Status</th>
            <th>Progress</th>
            <th>Action</th>
          </tr>
        </thead>
        <tbody>
          {requests.map((req, i) => (
            <tr key={i}>
              <td>{req.name}</td>
              <td>{req.id}</td>
              <td><span className={`badge ${req.status.toLowerCase().replace(' ', '-')}`}>{req.status}</span></td>
              <td>{req.progress}</td>
              <td><button className="btn comment">Comment</button></td>
            </tr>
          ))}
        </tbody>
      </table>

      <div className="triple-box">
        <div className="box">
          <h3>Audit Logs</h3>
        </div>
        <div className="box">
          <h3>Generate Reports</h3>
          <button className="submit-btn">
            Submit <span>📤</span>
          </button>
        </div>
      </div>

      <div className="box wide">
        <h3>API Documentation</h3>
        <button className="submit-btn">
          Submit <span>📤</span>
        </button>
      </div>
    </div>
  );
}

export default RegistrarDashboard;


