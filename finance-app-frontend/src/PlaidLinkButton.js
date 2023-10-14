import React, { useEffect, useState, useCallback, useRef } from 'react';
import { usePlaidLink } from 'react-plaid-link';

function PlaidLinkButton({ userId, onPlaidSuccess }) {
  const [linkToken, setLinkToken] = useState(null);

  const hasFetchedToken = useRef(false);

  useEffect(() => {
    console.log("Generating link token for userId:", userId); // Logging for diagnosis

    const generateToken = async () => {
      try {
        const response = await fetch('http://localhost:8081/plaid/get_link_token', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({ id: userId }),
          credentials: 'include'
        });
        if (!response.ok) {
          throw new Error(`Failed to get link token: ${response.statusText}`);
        }
        const responseData = await response.json();
        setLinkToken(responseData.linkToken);

      } catch (error) {
        console.error('Error creating link token:', error);
      }
    };

    if (!hasFetchedToken.current && userId) {
    generateToken();
    hasFetchedToken.current = true; // Set the ref to true after fetching the token
  }
}, [userId]);

  return linkToken != null ? <Link linkToken={linkToken} onPlaidSuccess={(publicToken) => onPlaidSuccess(publicToken, linkToken)} /> : <></>;
}

const Link = ({ linkToken, onPlaidSuccess }) => {
  const onSuccessCallback = useCallback(async (public_token) => {
    try {
      const response = await fetch('http://localhost:8081/plaid/set_access_token', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ public_token, link_token: linkToken }), // Sending both public_token and link_token
        credentials: 'include'
      });

      if (!response.ok) {
        throw new Error(`Failed to set access token: ${response.statusText}`);
      }

      const data = await response.json();
      console.log(data); // You can handle the response as needed

      onPlaidSuccess(data); // Call the onPlaidSuccess prop
    } catch (error) {
      console.error('Error setting access token:', error);
    }
  }, [onPlaidSuccess, linkToken]);  // Added linkToken as a dependency

  const { open, ready } = usePlaidLink({
    token: linkToken,
    onSuccess: onSuccessCallback,
  });

  return (
    <button onClick={() => open()} disabled={!ready}>
      Link account
    </button>
  );
};

export default PlaidLinkButton;
