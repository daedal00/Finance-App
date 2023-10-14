import React from 'react';
import axios from 'axios';
import './Navbar.css';

function Navbar() {
    function Navbar() {
        const userId = localStorage.getItem('userId');
    
        async function logout() {
            try {
                await axios.get('http://localhost:8081/api/users/logout');
                localStorage.removeItem('userId');
                window.location.href = '/login';
            } catch (error) {
                console.error("Error logging out:", error);
            }
        }
    

        return (
            <nav className="navbar">
                <a href="/">Home</a>
                {userId ? (
                    <>
                        <a href="/profile">Profile</a>
                        <a href="/dashboard">Dashboard</a>
                        <a href="/logout" onClick={logout}>Logout</a>
                    </>
                ) : (
                    <>
                        <a href="/login">Login</a>
                        <a href="/signup">Signup</a>
                    </>
                )}
            </nav>
        );
    }
}

export default Navbar;
