import React from 'react';

import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import LoginPage from './components/Login';
import SignUpPage from './components/SignUp';
import Dashboard from './pages/Dashboard';
import BalancesPage from './pages/BalancesPage';
import TransactionsPage from './pages/TransactionsPage';
import MonthlySpendingsPage from './pages/MonthlySpendingsPage';
import ProfileManagement from './components/ProfileManagement';
import MainLayout from './components/MainLayout';
import Navbar from './components/Navbar';

function App() {
  return (
    <Router>
      <MainLayout>
        <Navbar />
        <Routes>
        <Route path="/" element={<Navigate to="/login" />} />
          <Route path="/login" element={<LoginPage />} />
          <Route path="/signup" element={<SignUpPage />} />
          <Route path="/dashboard" element={<Dashboard />} />
          <Route path="/balances" element={<BalancesPage />} />
          <Route path="/transactions" element={<TransactionsPage />} />
          <Route path="/monthly-spendings" element={<MonthlySpendingsPage />} />
          <Route path="/profile" element={<ProfileManagement />} />
          {/* You can add more routes as needed */}
        </Routes>
      </MainLayout>
    </Router>
  );
}

export default App;
