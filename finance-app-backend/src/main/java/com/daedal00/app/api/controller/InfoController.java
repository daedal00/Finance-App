package com.daedal00.app.api.controller;

import com.daedal00.app.model.Transaction;
import com.daedal00.app.resources.InfoResource;
import com.daedal00.app.service.PlaidService;
import com.plaid.client.model.AccountBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/plaid/info")
public class InfoController {

    @Autowired
    private PlaidService plaidService;

    @GetMapping("/accounts")
    public List<AccountBase> getAccounts(@RequestParam String userId) throws IOException {
        return plaidService.fetchAccountsFromPlaid(userId);
    }

    @GetMapping
    public InfoResource.InfoResponse getInfo(@RequestParam String userId) throws IOException {
        String accessToken = plaidService.getAccessToken(userId);
        String itemId = plaidService.getItemId(userId);
    
        // Fetch accounts from Plaid
        List<AccountBase> accountBases = plaidService.fetchAccountsFromPlaid(userId);
    
        // Extract account names or other details from the fetched accounts
        List<String> accountNames = accountBases.stream()
            .map(AccountBase::getName)
            .collect(Collectors.toList());
    
        return new InfoResource.InfoResponse(accountNames, accessToken, itemId);
    }

    // Used for sandbox testing
    @GetMapping("/get-access-token")
    public ResponseEntity<String> generateAndExchangeToken(@RequestParam String userId) {
        try {
            String accessToken = plaidService.generateAndExchangeSandboxToken();
            plaidService.setAccessToken(userId, accessToken);
            return ResponseEntity.ok(accessToken);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to generate and exchange token: " + e.getMessage());
        }
    }

    @PostMapping("/exchange-public-token")
    public ResponseEntity<String> exchangePublicToken(@RequestParam String userId, @RequestBody String publicToken) {
        try {
            String accessToken = plaidService.exchangePublicToken(publicToken);
            plaidService.setAccessToken(userId, accessToken);
            return ResponseEntity.ok(accessToken);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to exchange public token: " + e.getMessage());
        }
    }


    @GetMapping("/transactions")
    public ResponseEntity<List<com.daedal00.app.model.Transaction>> getTransactions(@RequestParam String userId) {
        try {
            List<com.daedal00.app.model.Transaction> transactions = plaidService.fetchTransactionsFromPlaid(userId);
            return ResponseEntity.ok(transactions);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/total-balance")
    public ResponseEntity<Double> getTotalBalance(@RequestParam String userId) {
        try {
            Double totalBalance = plaidService.getTotalBalance(userId);
            return ResponseEntity.ok(totalBalance);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/expenses")
    public ResponseEntity<List<Transaction>> getExpenses(@RequestParam String userId) {
        List<Transaction> expenses = plaidService.getExpenses(userId);
        return ResponseEntity.ok(expenses);
    }

    @GetMapping("/total-expenses")
    public ResponseEntity<Double> getTotalExpenses(@RequestParam String userId) {
        Double totalExpenses = plaidService.getTotalExpenses(userId);
        return ResponseEntity.ok(totalExpenses);
    }
}
