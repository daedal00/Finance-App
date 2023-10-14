package com.daedal00.app.service;

import com.daedal00.app.api.dto.UserDTO;
import com.daedal00.app.model.PlaidData;
import com.daedal00.app.model.Transaction;
import com.daedal00.app.model.User;
import com.daedal00.app.repository.PlaidDataRepository;
import com.daedal00.app.repository.TransactionRepository;
import com.daedal00.app.repository.UserRepository;
import com.plaid.client.ApiClient;
import com.plaid.client.model.AccountBase;
import com.plaid.client.model.AccountsGetRequest;
import com.plaid.client.model.AccountsGetResponse;
import com.plaid.client.model.CountryCode;
import com.plaid.client.model.ItemPublicTokenExchangeRequest;
import com.plaid.client.model.ItemPublicTokenExchangeResponse;
import com.plaid.client.model.LinkTokenCreateRequest;
import com.plaid.client.model.LinkTokenCreateRequestUser;
import com.plaid.client.model.LinkTokenCreateResponse;
import com.plaid.client.model.Products;
import com.plaid.client.model.SandboxPublicTokenCreateRequest;
import com.plaid.client.model.SandboxPublicTokenCreateResponse;
import com.plaid.client.model.TransactionsGetRequest;
import com.plaid.client.model.TransactionsGetResponse;
import com.plaid.client.request.PlaidApi;
import com.plaid.client.model.Products;
import com.plaid.client.model.CountryCode;
import com.plaid.client.model.LinkTokenCreateRequest;
import com.plaid.client.model.LinkTokenCreateRequestUser;
import com.plaid.client.model.LinkTokenCreateResponse;

import retrofit2.Response;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.service.annotation.HttpExchange;

@Service
public class PlaidService {

    @Autowired
    private PlaidDataRepository plaidDataRepository;

    @Autowired
    private PlaidApi plaidClient;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserService userService;


    @Value("${plaid.client.id}")
    public String clientid;

    @Value("${plaid.secret}")
    public String secret;
    
    public String getAccessToken(String userId) {
        PlaidData plaidData = plaidDataRepository.findByUserId(userId);
        return plaidData != null ? plaidData.getAccessToken() : null;
    }

    public void setAccessToken(String userId, String accessToken) {
        PlaidData plaidData = plaidDataRepository.findByUserId(userId);
        if (plaidData == null) {
            plaidData = new PlaidData();
            plaidData.setUserId(userId);
        }
        plaidData.setAccessToken(accessToken);
        plaidDataRepository.save(plaidData);
    }

    public String getItemId(String userId) {
        PlaidData plaidData = plaidDataRepository.findByUserId(userId);
        return plaidData != null ? plaidData.getItemId() : null;
    }

    public void setItemId(String userId, String itemId) {
        PlaidData plaidData = plaidDataRepository.findByUserId(userId);
        if (plaidData == null) {
            plaidData = new PlaidData();
            plaidData.setUserId(userId);
        }
        plaidData.setItemId(itemId);
        plaidDataRepository.save(plaidData);
    }

    public String exchangePublicToken(String publicToken) throws IOException {
        ItemPublicTokenExchangeRequest request = new ItemPublicTokenExchangeRequest().publicToken(publicToken);
        Response<ItemPublicTokenExchangeResponse> response = plaidClient.itemPublicTokenExchange(request).execute();
    
        if (response.isSuccessful()) {
            return response.body().getAccessToken();
        } else {
            throw new IOException("Failed to exchange public token: " + response.errorBody().string());
        }
    }
    

    public List<AccountBase> fetchAccountsFromPlaid(String userId) throws IOException {
        String accessToken = getAccessToken(userId);
        AccountsGetRequest request = new AccountsGetRequest().accessToken(accessToken);
        Response<AccountsGetResponse> response = plaidClient.accountsGet(request).execute();
        return response.body().getAccounts();
    }

    
    LocalDate startLocalDate = LocalDate.parse("2023-08-01"); // To Change
    LocalDate endLocalDate = LocalDate.parse("2023-10-01");

    public List<com.daedal00.app.model.Transaction> fetchTransactionsFromPlaid(String userId) throws IOException {
        String accessToken = getAccessToken(userId);
        TransactionsGetRequest request = new TransactionsGetRequest()
                .accessToken(accessToken)
                .startDate(startLocalDate)  
                .endDate(endLocalDate);   
        Response<TransactionsGetResponse> response = plaidClient.transactionsGet(request).execute();
    
        if (!response.isSuccessful()) {
            throw new IOException("Failed to fetch transactions from Plaid: " + response.errorBody().string());
        }
    
        if (response.body() == null) {
            throw new IOException("No transactions found in the response from Plaid.");
        }
    
        List<com.daedal00.app.model.Transaction> transactions = response.body().getTransactions().stream()
                .map(t -> new com.daedal00.app.model.Transaction(
                    t.getAccountId(),
                    userId,
                    t.getAmount(),
                    t.getCategory().get(0),
                    t.getName(),
                    Date.valueOf(t.getDate())
                ))
                .collect(Collectors.toList());
        transactionRepository.saveAll(transactions);
        return transactions;
    }
    

    public String generateAndExchangeSandboxToken() throws IOException {
        SandboxPublicTokenCreateRequest createRequest = new SandboxPublicTokenCreateRequest()
            .institutionId("ins_109508")  // Example institution ID for "Chase"
            .initialProducts(Arrays.asList(Products.AUTH));
    
        Response<SandboxPublicTokenCreateResponse> createResponse = plaidClient.sandboxPublicTokenCreate(createRequest).execute();
    
        if (!createResponse.isSuccessful()) {
            throw new IOException("Failed to generate sandbox public token: " + createResponse.errorBody().string());
        }
    
        String publicToken = createResponse.body().getPublicToken();
    
        ItemPublicTokenExchangeRequest exchangeRequest = new ItemPublicTokenExchangeRequest().publicToken(publicToken);
        Response<ItemPublicTokenExchangeResponse> exchangeResponse = plaidClient.itemPublicTokenExchange(exchangeRequest).execute();
    
        if (!exchangeResponse.isSuccessful()) {
            throw new IOException("Failed to exchange public token: " + exchangeResponse.errorBody().string());
        }
    
        return exchangeResponse.body().getAccessToken();
    }

    public Double getTotalBalance(String userId) throws IOException {
        List<AccountBase> accounts = fetchAccountsFromPlaid(userId);
        return accounts.stream()
            .mapToDouble(account -> account.getBalances().getCurrent())
            .sum();
    }
    
    public List<Transaction> getExpenses(String userId) {
        List<Transaction> transactions = transactionRepository.findByUserId(userId);
        return transactions.stream()
            .filter(transaction -> transaction.getAmount() < 0)
            .collect(Collectors.toList());
    }

    public Double getTotalExpenses(String userId) {
        List<Transaction> expenses = getExpenses(userId);
        return expenses.stream()
            .mapToDouble(Transaction::getAmount)
            .sum();
    }

    public LinkTokenCreateResponse generateLinkTokenForUser(User user) throws IOException {
        // Create your Plaid client
        HashMap<String, String> apiKeys = new HashMap<>();
        apiKeys.put("clientId", clientid);
        apiKeys.put("secret", secret);
        ApiClient apiClient = new ApiClient(apiKeys);
        apiClient.setPlaidAdapter(ApiClient.Development);
        plaidClient = apiClient.createService(PlaidApi.class);

        String clientUserId = user.getId().toString();
        LinkTokenCreateRequestUser userRequest = new LinkTokenCreateRequestUser().clientUserId(clientUserId);

        // Create a link_token for the given user
        LinkTokenCreateRequest request = new LinkTokenCreateRequest()
            .user(userRequest)
            .clientName("Finance App")
            .products(Arrays.asList(Products.fromValue("auth"), Products.fromValue("transactions")))
            .countryCodes(Arrays.asList(CountryCode.CA))
            .language("en")
            .redirectUri("https://55a2-206-12-138-212.ngrok.io/dashboard")
            .webhook("https://55a2-206-12-138-212.ngrok.io/plaid/webhook");

        Response<LinkTokenCreateResponse> response = plaidClient.linkTokenCreate(request).execute();

        if (!response.isSuccessful()) {
            throw new IOException("Failed to generate link token: " + response.errorBody().string());
        }

        return response.body();
    }

    public void handleWebhookNotification(Map<String, Object> webhookData) {
        String webhookType = (String) webhookData.get("webhook_type");
        String webhookCode = (String) webhookData.get("webhook_code");
        
        // Handle different types of webhook notifications
        switch (webhookType) {
            case "TRANSACTIONS":
                handleTransactionWebhook(webhookCode, webhookData);
                break;
            // Add more cases for other webhook types if needed
            default:
                System.out.println("Unhandled webhook type: " + webhookType);
        }
    }
    
    private void handleTransactionWebhook(String webhookCode, Map<String, Object> webhookData) {
        String userId = (String) webhookData.get("user_id"); // Assuming you have user_id in the webhook data
    
        switch (webhookCode) {
            case "INITIAL_UPDATE":
                // Fetch the latest transactions for the user
                try {
                    fetchTransactionsFromPlaid(userId);
                    System.out.println("Fetched initial transactions for user: " + userId);
                } catch (IOException e) {
                    System.err.println("Error fetching initial transactions for user: " + userId + ". Error: " + e.getMessage());
                }
                break;
    
            case "HISTORICAL_UPDATE":
                // Fetch the latest transactions for the user
                try {
                    fetchTransactionsFromPlaid(userId);
                    System.out.println("Fetched historical transactions for user: " + userId);
                } catch (IOException e) {
                    System.err.println("Error fetching historical transactions for user: " + userId + ". Error: " + e.getMessage());
                }
                break;
    
            default:
                System.out.println("Unhandled transaction webhook code: " + webhookCode + " for user: " + userId);
        }
    }
}