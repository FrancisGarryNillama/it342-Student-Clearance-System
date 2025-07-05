// src/pages/OAuthRedirect.js
import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

const OAuthRedirect = () => {
  const navigate = useNavigate();

  useEffect(() => {
    // In frontend-only mode, this page won't receive actual OAuth tokens.
    // It's here to prevent errors if a redirect somehow happens.
    // In a real scenario, this would parse URL params for token and role.
    alert('Google OAuth flow is not active in frontend-only mode. Redirecting to login.');
    localStorage.clear(); // Clear any partial states
    navigate('/'); // Always redirect to login in this mode
  }, [navigate]);

  return (
    <div>
      <h2>Processing OAuth (Frontend Mock)...</h2>
      <p>This page handles OAuth redirects from a backend. In frontend-only mode, it redirects to login.</p>
    </div>
  );
};

export default OAuthRedirect;