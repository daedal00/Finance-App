// src/components/UserProfile.js

import React, { useState, useEffect } from 'react';

function UserProfile({ userId }) {
  const [userData, setUserData] = useState(null);

  useEffect(() => {
    const fetchUserData = async () => {
      try {
        const response = await fetch(`http://localhost:8081/api/users/${userId}`);
        const data = await response.json();
        setUserData(data);
      } catch (error) {
        console.error("Error fetching user data:", error);
      }
    };

    fetchUserData();
  }, [userId]);

  return (
    <div>
      {userData ? (
        <>
          <h2>{userData.firstName} {userData.lastName}</h2>
          <p>Email: {userData.email}</p>
          <p>Username: {userData.username}</p>
          {/* Display other user details as needed */}
        </>
      ) : (
        <p>Loading user data...</p>
      )}
    </div>
  );
}

export default UserProfile;
