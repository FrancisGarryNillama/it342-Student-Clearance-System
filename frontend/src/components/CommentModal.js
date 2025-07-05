// src/components/CommentModal.js
import React, { useState } from 'react';

const CommentModal = ({ taskId, onClose, onSubmitComment }) => {
  const [commentText, setCommentText] = useState('');

  const handleSubmit = () => {
    if (commentText.trim()) {
      onSubmitComment(taskId, commentText);
      onClose(); // Close the modal after submission
    } else {
      alert('Comment cannot be empty.');
    }
  };

  return (
    <div className="modal-overlay">
      <div className="modal-content">
        <h3>Add Comment for Task ID: {taskId}</h3>
        <textarea
          value={commentText}
          onChange={(e) => setCommentText(e.target.value)}
          placeholder="Type your comment here..."
          rows="5"
        ></textarea>
        <div className="modal-actions">
          <button onClick={handleSubmit} className="action-button">Submit Comment</button>
          <button onClick={onClose} className="logout-button" style={{ marginLeft: '10px' }}>Cancel</button>
        </div>
      </div>
    </div>
  );
};

export default CommentModal;