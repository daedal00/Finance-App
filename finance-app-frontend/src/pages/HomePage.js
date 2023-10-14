// src/pages/HomePage.js

import React from 'react';
import { Typography } from '@mui/material';

function HomePage() {
  return (
    <div>
      <Typography variant="h4" gutterBottom>
        Welcome to Finance App
      </Typography>
      <Typography>
        Connect with your bank and view transactions, balances, and monthly spendings.
      </Typography>
    </div>
  );
}

export default HomePage;
