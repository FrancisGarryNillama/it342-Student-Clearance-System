import React, { useEffect, useState } from 'react';
import './RegistrarDashboard.css';
import axios from '../services/api';

function RegistrarDashboard() {
  const [user, setUser] = useState(null);
  const [requests, setRequests] = useState([]);

  useEffect(() => {
    axios.get('/user', { withCredentials: true })
      .then(res => {
        setUser({ name: res.data.fullName, role: res.data.role });
      })
      .catch(() => setUser({ name: 'Unknown', role: 'REGISTRAR' }));

    fetchRequests();
  }, []);

  const fetchRequests = () => {
    axios.get('/registrar/requests', { withCredentials: true })
      .then(res => setRequests(res.data || []))
      .catch(() => setRequests([]));
  };

  const handleApprove = async (id) => {
    try {
      await axios.patch(`/registrar/request/${id}/approve`, {}, { withCredentials: true });
      fetchRequests();
    } catch (err) {
      alert("Failed to approve: " + (err.response?.data || err.message));
    }
  };

    const handleReject = async (id) => {
      const reason = prompt("Enter rejection comment:");
      if (!reason) return;

      try {
        await axios.patch(`/registrar/request/${id}/reject`, reason, {
          headers: { 'Content-Type': 'text/plain' },
          withCredentials: true
        });
        fetchRequests();
      } catch (err) {
        alert("Failed to reject: " + (err.response?.data || err.message));
      }
    };

    const handleReport = async () => {
      try {
        const res = await axios.get('/registrar/report', {
          responseType: 'blob',
          withCredentials: true
        });

        const blob = new Blob([res.data], { type: 'application/pdf' });
        const link = document.createElement('a');
        link.href = window.URL.createObjectURL(blob);
        link.download = 'clearance-report.pdf';
        link.click();
      } catch (err) {
        alert("Failed to generate report.");
        console.error(err);
      }
    };


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
          <p className="subtle">Monitor all graduation clearances.</p>
        </div>
        <div className="info-card">
          <p><strong>System Alerts</strong></p>
          <p className="subtle">No new alerts</p>
        </div>
      </div>

      <h2>Pending Graduation Clearance Requests</h2>
      <table className="dashboard-table">
        <thead>
          <tr>
            <th>Student Name</th>
            <th>Email</th>
            <th>Status</th>
            <th>Comment</th>
            <th>File</th>
            <th>Action</th>
          </tr>
        </thead>
        <tbody>
          {requests.length === 0 ? (
            <tr><td colSpan="6">No requests found.</td></tr>
          ) : (
            requests.map((req, i) => (
              <tr key={i}>
                <td>{req.user?.fullName}</td>
                <td>{req.user?.email}</td>
                <td>
                  <span className={`badge ${req.status.toLowerCase()}`}>
                    {req.status}
                  </span>
                </td>
                <td>{req.comment || '—'}</td>
                <td>
                  {req.fileUrl
                    ? <a href={`http://localhost:8080${req.fileUrl}`} target="_blank" rel="noreferrer">👁 View</a>
                    : 'No File'}
                </td>
                <td>
                  {req.status === 'PENDING' ? (
                    <>
                      <button className="btn comment" onClick={() => handleApprove(req.taskId)}>✅ Approve</button>
                      <button className="btn comment" onClick={() => handleReject(req.taskId)}>❌ Reject</button>
                    </>
                  ) : (
                    '—'
                  )}
                </td>
              </tr>
            ))
          )}
        </tbody>
      </table>

      <div className="triple-box">
        <div className="box">
          <h3>Audit Logs</h3>
          <p>Coming soon: view logs of all actions taken.</p>
        </div>
        <div className="box">
          <h3>Generate Reports</h3>
          <button className="submit-btn" onClick={handleReport}>
            Download Report <span>📄</span>
          </button>
        </div>
      </div>

      <div className="box wide">
        <h3>API Documentation</h3>
        <button className="submit-btn" onClick={() => window.open('http://localhost:8080/swagger-ui.html', '_blank')}>
          Open Docs <span>📘</span>
        </button>
      </div>
    </div>
  );
}

export default RegistrarDashboard;






