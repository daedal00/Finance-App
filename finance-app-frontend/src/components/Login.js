import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate, Link } from 'react-router-dom';

axios.defaults.withCredentials = true;

function Login() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      const credentials = btoa(username + ":" + password);
      const headers = { Authorization: "Basic " + credentials };

      const response = await axios.post('http://localhost:8081/api/users/login', {}, { headers: headers });
      if (response.status === 200 && response.data.id) {
        localStorage.setItem('userId', response.data.id);
        navigate('/dashboard');
    }
  else {
        // Handle login error (e.g., display an error message to the user)
      }
    } catch (error) {
      console.error("Error during login:", error);
    }
  };

  return (
    <div>
      <form onSubmit={handleLogin}>
        <input
          type="text"
          placeholder="Username"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
        />
        <input
          type="password"
          placeholder="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />
        <button type="submit">Login</button>
      </form>
      <Link to="/signup">Don't have an account? Sign up</Link>
    </div>
  );
}

export default Login;
