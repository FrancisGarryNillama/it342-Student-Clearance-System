import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './LoginPage.css';
import { login } from '../services/authService';

function LoginPage() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [showPassword, setShowPassword] = useState(false);
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();
    setError('');

    try {
      const res = await login(email, password);

      if (res.status === 1 && res.results?.role) {
        const role = res.results.role.toLowerCase();
        localStorage.setItem('userRole', role);
        navigate(`/${role}`);
      } else {
        setError(res.message || 'Invalid login.');
      }
    } catch (err) {
      setError('Login failed: ' + (err.response?.data?.message || err.message));
    }
  };

  const handleGoogleLogin = () => {
    window.location.href = 'http://localhost:8080/login';
  };

  return (
    <div className="login-container">
      <div className="login-form-section">
        <div className="form-wrapper">
          <h2>Login to your Account</h2>
          <form onSubmit={handleLogin}>
            <div className="form-group">
              <label>Email</label>
              <input
                type="email"
                placeholder="Enter your email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
              />
            </div>
            <div className="form-group password-group">
              <label>Password</label>
              <input
                type={showPassword ? 'text' : 'password'}
                placeholder="Enter your password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
              />
              <span
                className="toggle-password"
                onClick={() => setShowPassword(!showPassword)}
              >
                {showPassword ? '🙈' : '👁️'}
              </span>
            </div>

            {error && <p className="error-msg">{error}</p>}

            <div className="form-options">
              <label><input type="checkbox" /> Remember me</label>
              <a href="#">Forgot Password?</a>
            </div>

            <button type="submit" className="btn-login">Log In</button>

            <p className="signup-text">Don’t have an account? <a href="#">Create one</a></p>
            <div className="divider">or continue with</div>
            <button
              type="button"
              className="btn-google"
              onClick={handleGoogleLogin}
            >
              Google OAuth
            </button>
          </form>
        </div>
      </div>

      <div className="login-graphic-section">
        <h1>Student Clearance System</h1>
        <div className="graphic-mockup">
          <div className="device laptop"></div>
          <div className="device phone"></div>
          <div className="device lock"></div>
          <div className="line line-1"></div>
          <div className="line line-2"></div>
        </div>
      </div>
    </div>
  );
}

export default LoginPage;




