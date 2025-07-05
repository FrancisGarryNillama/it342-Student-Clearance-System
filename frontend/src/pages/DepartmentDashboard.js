import React, { useEffect, useState } from 'react';
import './DepartmentDashboard.css';

function DepartmentDashboard() {
  const [user, setUser] = useState(null);
  const [pending, setPending] = useState([]);
  const [processed, setProcessed] = useState([]);

  useEffect(() => {
    setUser({
      name: 'Megan Fox',
      role: 'Admin',
      department: 'Computer Science',
    });

    setPending([
      { name: 'Student A', id: 'ID123', date: '2025-06-19', status: 'Pending' },
      { name: 'Student A', id: 'ID123', date: '2025-06-19', status: 'Pending' }
    ]);

    setProcessed([
      {
        department: '00001',
        status: '089 Kutch Green Apt. 448',
        comment: '04 Sep 2019',
        date: '04 Sep 2019'
      },
      {
        department: '00001',
        status: '089 Kutch Green Apt. 448',
        comment: '04 Sep 2019',
        date: '04 Sep 2019'
      }
    ]);
  }, []);

  if (!user) return <p>Loading...</p>;

  return (
    <div className="department-dashboard">
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

      <h1>Department Head Dashboard</h1>

      <div className="card-row">
        <div className="info-card">
          <p><strong>Your Dept:</strong> {user.department}</p>
          <p className="subtle">Overview of departmental clearances.</p>
        </div>
        <div className="info-card">
          <p><strong>Dept Notifications</strong></p>
          <p className="notif">New request for review.</p>
          <p className="notif">Reminder for pending tasks.</p>
        </div>
      </div>

      <h2>Pending Requests for Review</h2>
      <table className="dashboard-table">
        <thead>
          <tr>
            <th>Student Name</th>
            <th>Student ID</th>
            <th>Date</th>
            <th>Status</th>
            <th>Action</th>
          </tr>
        </thead>
        <tbody>
          {pending.map((item, index) => (
            <tr key={index}>
              <td>{item.name}</td>
              <td>{item.id}</td>
              <td>{item.date}</td>
              <td><span className="badge pending">{item.status}</span></td>
              <td className="actions">
                <button className="btn approve">Approve</button>
                <button className="btn reject">Reject</button>
                <button className="btn comment">Comment</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      <h2>Processed Requests</h2>
      <table className="dashboard-table">
        <thead>
          <tr>
            <th>Department</th>
            <th>Status</th>
            <th>Comments</th>
            <th>Date</th>
            <th>Action</th>
          </tr>
        </thead>
        <tbody>
          {processed.map((item, index) => (
            <tr key={index}>
              <td>{item.department}</td>
              <td>{item.status}</td>
              <td>{item.comment}</td>
              <td>{item.date}</td>
              <td><span className="badge completed">Completed</span></td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default DepartmentDashboard;

