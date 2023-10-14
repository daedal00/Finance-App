import React from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

function ProfileManagement() {
  const navigate = useNavigate();

  const handleUpdateProfile = async (updatedData) => {
    try {
      const response = await axios.put('http://localhost:8081/api/users/update', updatedData);
      if (response.data && response.data.success) {
        // Handle successful profile update
      }
    } catch (error) {
      console.error("Error updating profile:", error);
    }
  };

  const handleDeleteAccount = async () => {
    try {
      const response = await axios.delete('http://localhost:8081/api/users/delete');
      if (response.data && response.data.success) {
        navigate('/login');
      }
    } catch (error) {
      console.error("Error deleting account:", error);
    }
  };

  return (
    <div>
      {/* Components to update user information */}
      <button onClick={handleUpdateProfile}>Update Profile</button>
      <button onClick={handleDeleteAccount}>Delete Account</button>
    </div>
  );
}

export default ProfileManagement;
