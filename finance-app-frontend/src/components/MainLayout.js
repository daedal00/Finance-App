// src/components/MainLayout.js

import React from 'react';
import { AppBar, Toolbar, Typography, Button, Container } from '@mui/material';
import { Link } from 'react-router-dom';
export default MainLayout;

function MainLayout({ children, isLoggedIn }) {
    return (
      <div>
        <AppBar position="static">
          <Toolbar>
            <Typography variant="h6" style={{ flexGrow: 1 }}>
              Finance App
            </Typography>
            {isLoggedIn ? (
              <>
                <Button color="inherit" component={Link} to="/">Home</Button>
                <Button color="inherit" component={Link} to="/transactions">Transactions</Button>
                <Button color="inherit" component={Link} to="/balances">Balances</Button>
                <Button color="inherit" component={Link} to="/monthly-spendings">Monthly Spendings</Button>
              </>
            ) : (
              <>
                <Button color="inherit" component={Link} to="/login">Login</Button>
                <Button color="inherit" component={Link} to="/signup">SignUp</Button>
              </>
            )}
          </Toolbar>
        </AppBar>
        <Container style={{ marginTop: '20px' }}>
          {children}
        </Container>
      </div>
    );
  }