// src/pages/MonthlySpendingsPage.js

import React from 'react';
import { Typography } from '@mui/material';

function MonthlySpendingsPage() {
  return (
    <div>
      <Typography variant="h4" gutterBottom>
        Monthly Spendings
      </Typography>
      {/* Here you can integrate with Plaid or your backend to fetch and display monthly spendings */}
    </div>
  );
}

export default MonthlySpendingsPage;
