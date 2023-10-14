import React, { useState, useEffect } from 'react';
import { Chart } from "react-google-charts";
import axios from 'axios';
import App from '../PlaidLinkButton';
import './Dashboard.css';

function Dashboard() {
  const [isPlaidLinked, setIsPlaidLinked] = useState(false);
  const [transactions, setTransactions] = useState([]);
  const [balance, setBalance] = useState(0);
  const [categoryTotals, setCategoryTotals] = useState({});
  const userId = localStorage.getItem('userId');

  useEffect(() => {
    const checkPlaidLinkStatus = async () => {
      try {
        const response = await axios.get('http://localhost:8081/api/users/check-plaid-link', { params: { userId: userId }, withCredentials: true });
        setIsPlaidLinked(response.data.isPlaidLinked);
      } catch (error) {
        console.error("Error checking Plaid link status:", error);
      }
    };

    checkPlaidLinkStatus();
  }, [userId]);

  useEffect(() => {
    const fetchCategoryTotals = async () => {
      try {
        const response = await axios.get('http://localhost:8081/api/transactions/totals', { params: { userId: userId }, withCredentials: true });
        setCategoryTotals(response.data);
      } catch (error) {
        console.error("Error fetching transaction totals by category:", error);
      }
    };
  
    fetchCategoryTotals();
  }, [userId]);

  const getCategoryChartData = () => {
    const chartData = [['Category', 'Spending']];
  
    for (let category in categoryTotals) {
      let totalSpendingForCategory = 0;
      for (let month in categoryTotals[category]) {
        totalSpendingForCategory += Math.abs(categoryTotals[category][month].totalAmount);
      }
      chartData.push([category, totalSpendingForCategory]);
    }
  
    // Filter out categories with no data (0 spending)
    return chartData.filter(data => data[1] !== 0);
  };
  
  
  

  useEffect(() => {
    if (isPlaidLinked) {
      const fetchPlaidData = async () => {
        try {
          const transactionsResponse = await axios.get('http://localhost:8081/plaid/info/transactions', { params: { userId: userId }, withCredentials: true });
          
          const sortedTransactions = transactionsResponse.data.sort((a, b) => new Date(b.date) - new Date(a.date));
          
          setTransactions(sortedTransactions);

          const balanceResponse = await axios.get('http://localhost:8081/plaid/info/total-balance', { params: { userId: userId }, withCredentials: true });
          setBalance(balanceResponse.data);
        } catch (error) {
          console.error("Error fetching Plaid data:", error);
        }
      };

      fetchPlaidData();
    }
}, [isPlaidLinked, userId]);


  const handlePlaidSuccess = async (publicToken, linkToken) => {
    try {
      const response = await axios.post('http://localhost:8081/plaid/info/exchange-public-token', { publicToken, linkToken }, { params: { userId: userId }, withCredentials: true });
      if (response.data) {
        setIsPlaidLinked(true);
      }
    } catch (error) {
      console.error("Error exchanging Plaid public token:", error);
    }
  };

  async function logout() {
    try {
        await axios.get('http://localhost:8081/api/users/logout');

        setIsPlaidLinked(false);
        setTransactions([]);
        setBalance(0);

        localStorage.removeItem('userId');

        window.location.href = '/login';
    } catch (error) {
        console.error("Error logging out:", error);
    }
}


return (
  <div className="dashboard-container">
      <div className="header">
          <h2>Your Finance Dashboard</h2>
          <button onClick={logout}>Logout</button>
      </div>
      {isPlaidLinked ? (
          <>
              <h2>Your Balance: ${balance.toFixed(2)}</h2>
              <div className="chart-container">
                  <h3>Spending by Category:</h3>
                  <Chart
                      width={'100%'}
                      height={'300px'}
                      chartType="PieChart"
                      loader={<div>Loading Chart</div>}
                      data={getCategoryChartData()}
                      options={{
                          title: 'Spending by Category',
                          pieHole: 0.4,  // This makes it a donut chart
                          colors: ['#007BFF', '#28a745', '#ffc107', '#dc3545', '#17a2b8']
                      }}
                  />
              </div>
              <div className="transactions-list">
                  <h3>Recent Transactions:</h3>
                  {transactions.map(transaction => (
                      <div className="transaction-item" key={transaction.id}>
                          <span className="transaction-date">{new Date(transaction.date).toLocaleDateString()}</span>
                          <span>${transaction.amount.toFixed(2)}</span>
                          <span className="transaction-name">{transaction.merchantName}</span>
                          <span className="transaction-category">{transaction.category}</span>
                      </div>
                  ))}
              </div>
          </>
      ) : (
          <App userId={userId} onPlaidSuccess={handlePlaidSuccess} />
      )}
  </div>
);

}

export default Dashboard;
