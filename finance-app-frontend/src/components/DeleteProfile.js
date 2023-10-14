// DeleteProfile.js
import React from 'react';
import axios from 'axios';

function DeleteProfile({ userId }) {
  const handleDelete = async () => {
    try {
      await axios.delete(`http://localhost:8081/api/users/${userId}`);
      // Redirect to the home page or show a success message
    } catch (error) {
      console.error("Error deleting profile:", error);
    }
  };

  return (
    <button onClick={handleDelete}>Delete Profile</button>
  );
}

export default DeleteProfile;
