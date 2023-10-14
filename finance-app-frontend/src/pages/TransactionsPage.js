// src/pages/TransactionsPage.js

import React from 'react';
import { Typography } from '@mui/material';

function TransactionsPage() {
  return (
    <div>
      <Typography variant="h4" gutterBottom>
        Transactions
      </Typography>
      {/* Here you can integrate with Plaid or your backend to fetch and display transactions */}
    </div>
  );
}

export default TransactionsPage;
