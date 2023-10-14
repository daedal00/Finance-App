import React, { useState, useEffect } from 'react';
import { Typography } from '@mui/material';

function BalancesPage() {
  const [balances, setBalances] = useState([]);

  useEffect(() => {
    const fetchBalances = async () => {
      try {
        const response = await fetch('http://localhost:8081/api/balances', {
          headers: {
            'Authorization': `Bearer YOUR_JWT_TOKEN`
          }
        });
        const data = await response.json();
        setBalances(data);
      } catch (error) {
        console.error('Error fetching balances:', error);
      }
    };

    fetchBalances();
  }, []);

  return (
    <div>
      <Typography variant="h4" gutterBottom>
        Account Balances
      </Typography>
      {balances.map((balance, index) => (
        <div key={index}>
          <Typography variant="h6">{balance.accountName}: ${balance.amount}</Typography>
        </div>
      ))}
    </div>
  );
}

export default BalancesPage;
