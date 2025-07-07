import React, { useEffect, useState } from 'react';
import './StudentDashboard.css';
import axios from '../services/api';

function StudentDashboard() {
  const [student, setStudent] = useState(null);
  const [clearance, setClearance] = useState([]);
  const [notifications, setNotifications] = useState([]);
  const [requestType, setRequestType] = useState('');
  const [file, setFile] = useState(null);
  const [selectedTask, setSelectedTask] = useState(null);

  useEffect(() => {
    axios.get('/user')
      .then(res => {
        if (res.data.role !== 'STUDENT') {
          window.location.href = '/';
        } else {
          setStudent({
            name: res.data.fullName,
            role: 'Student',
            id: res.data.email,
            graduationYear: '2025',
            updatedAt: new Date().toLocaleString()
          });
        }
      })
      .catch(() => window.location.href = '/');

    axios.get('/student/clearance-tasks')
      .then(res => setClearance(res.data || []))
      .catch(err => {
        console.error('Failed to load clearance tasks:', err);
        setClearance([]);
      });

    axios.get('/notifications')
      .then(res => setNotifications(res.data || []))
      .catch(() => setNotifications([]));
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
      await axios.post('/student/clearance-request', formData, {
        headers: { 'Content-Type': 'multipart/form-data' },
        withCredentials: true
      });

      alert("Clearance request submitted!");
      const updated = await axios.get('/student/clearance-tasks');
      setClearance(updated.data || []);
      setRequestType('');
      setFile(null);
    } catch (error) {
      alert("Error: " + (error.response?.data || error.message));
    }
  };

  const closeModal = () => setSelectedTask(null);

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
          {notifications.length === 0
            ? <p className="subtle">No new notifications</p>
            : notifications.map((note, i) => (
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
            <th>Type</th>
            <th>Action</th>
          </tr>
        </thead>
        <tbody>
          {clearance.length === 0 ? (
            <tr>
              <td colSpan="4">No clearance records found.</td>
            </tr>
          ) : clearance.map((row, i) => (
            <tr key={i}>
              <td>{row.department?.name || 'N/A'}</td>
              <td><span className={`badge ${row.status.toLowerCase()}`}>{row.status}</span></td>
              <td>{row.type || '—'}</td>
              <td>
                <a href="#" onClick={(e) => {
                  e.preventDefault();
                  setSelectedTask(row);
                }}>View</a>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      <h2>Submit Clearance Request</h2>
      <div className="submit-form">
        <label>Select Type:</label>
        <select className="input-card" value={requestType} onChange={(e) => setRequestType(e.target.value)}>
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

      {/* Modal */}
      {selectedTask && (
        <div className="modal-overlay">
          <div className="modal">
            <h3>Clearance Task Details</h3>
            <p><strong>Department:</strong> {selectedTask.department?.name}</p>
            <p><strong>Status:</strong> {selectedTask.status}</p>
            <p><strong>Type:</strong> {selectedTask.type || '—'}</p>
            <p><strong>Comment:</strong> {selectedTask.comment || 'None'}</p>
            <p><strong>File:</strong> {selectedTask.fileUrl
              ? <a href={`http://localhost:8080${selectedTask.fileUrl}`} target="_blank" rel="noreferrer">View File</a>
              : 'No File Uploaded'}
            </p>
            <button className="submit-btn" onClick={closeModal}>Close</button>
          </div>
        </div>
      )}
    </div>
  );
}

export default StudentDashboard;
