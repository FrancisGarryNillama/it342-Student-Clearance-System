import React, { useEffect, useState } from 'react';
import './DepartmentDashboard.css';
import axios from '../services/api';

function DepartmentDashboard() {
  const [user, setUser] = useState(null);
  const [pending, setPending] = useState([]);
  const [processed, setProcessed] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [selectedTask, setSelectedTask] = useState(null);
  const [comment, setComment] = useState('');

  useEffect(() => {
    axios.get('/user', { withCredentials: true })
      .then(res => {
        if (res.data.role !== 'DEPT_HEAD') {
          window.location.href = '/';
        } else {
          setUser({
            name: res.data.fullName,
            role: 'Dept Head',
            department: res.data.department?.name || 'Unknown'
          });
        }
      })
      .catch(() => window.location.href = '/');

    fetchTasks();
  }, []);

  const fetchTasks = () => {
    axios.get('/dept/tasks/pending', { withCredentials: true })
      .then(res => setPending(res.data || []))
      .catch(() => setPending([]));

    axios.get('/dept/tasks/processed', { withCredentials: true })
      .then(res => setProcessed(res.data || []))
      .catch(() => setProcessed([]));
  };

  const handleAction = async (taskId, actionStatus, optionalComment = '') => {
    try {
      await axios.put(`/dept/clearance-tasks/${taskId}?status=${actionStatus}&comment=${optionalComment}`, {}, { withCredentials: true });
      fetchTasks(); // Refresh
      setShowModal(false);
      setComment('');
      setSelectedTask(null);
    } catch (err) {
      alert('Failed to update task: ' + (err.response?.data || err.message));
    }
  };

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
            <th>Updated</th>
            <th>Status</th>
            <th>Action</th>
          </tr>
        </thead>
        <tbody>
          {pending.length === 0 ? (
            <tr><td colSpan="5">No pending tasks.</td></tr>
          ) : (
            pending.map((task, index) => (
              <tr key={index}>
                <td>{task.user?.fullName}</td>
                <td>{task.user?.email}</td>
                <td>{new Date(task.updatedAt).toLocaleDateString()}</td>
                <td><span className="badge pending">{task.status}</span></td>
                <td className="actions">
                  <button className="btn approve" onClick={() => handleAction(task.taskId, 'APPROVED')}>Approve</button>
                  <button className="btn reject" onClick={() => handleAction(task.taskId, 'REJECTED')}>Reject</button>
                  <button className="btn comment" onClick={() => {
                    setSelectedTask(task);
                    setShowModal(true);
                  }}>Comment</button>
                </td>
              </tr>
            ))
          )}
        </tbody>
      </table>

      <h2>Processed Requests</h2>
      <table className="dashboard-table">
        <thead>
          <tr>
            <th>Student</th>
            <th>Status</th>
            <th>Comment</th>
            <th>Updated</th>
            <th>Action</th>
          </tr>
        </thead>
        <tbody>
          {processed.length === 0 ? (
            <tr><td colSpan="5">No processed tasks.</td></tr>
          ) : (
            processed.map((task, index) => (
              <tr key={index}>
                <td>{task.user?.fullName}</td>
                <td><span className={`badge ${task.status.toLowerCase()}`}>{task.status}</span></td>
                <td>{task.comment || '—'}</td>
                <td>{new Date(task.updatedAt).toLocaleDateString()}</td>
                <td><span className="badge completed">Completed</span></td>
              </tr>
            ))
          )}
        </tbody>
      </table>

      {/* Comment Modal */}
      {showModal && selectedTask && (
        <div className="modal-overlay">
          <div className="modal">
            <h3>Comment on Task</h3>
            <p>Student: {selectedTask.user?.fullName}</p>
            <textarea
              className="input-card"
              placeholder="Write your comment here..."
              value={comment}
              onChange={(e) => setComment(e.target.value)}
            />
            <div className="button-row">
              <button className="submit-btn" onClick={() => handleAction(selectedTask.taskId, 'COMMENTED', comment)}>Submit</button>
              <button className="cancel-btn" onClick={() => setShowModal(false)}>Cancel</button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

export default DepartmentDashboard;
