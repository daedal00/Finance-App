package com.daedal00.app.api.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.daedal00.app.model.User;
import com.daedal00.app.repository.UserRepository;
import com.daedal00.app.service.PlaidService;
import com.daedal00.app.service.UserService;
import com.plaid.client.model.LinkTokenCreateResponse;
import com.plaid.client.model.ItemPublicTokenExchangeRequest;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/plaid")
@RestController
public class PlaidController {

    @Autowired
    private PlaidService plaidService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/get_link_token")
    public LinkTokenCreateResponse getLinkToken() throws IOException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        User user = userRepository.findByUsername(username);
        return plaidService.generateLinkTokenForUser(user);
    }

    @PostMapping("/set_access_token")
    public ResponseEntity<String> setAccessToken(@RequestBody Map<String, String> payload) {
        String publicToken = payload.get("public_token");
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        User user = userRepository.findByUsername(username);
        try {
            ItemPublicTokenExchangeRequest request = new ItemPublicTokenExchangeRequest().publicToken(publicToken);
            String accessToken = plaidService.exchangePublicToken(request.getPublicToken());
            userService.linkUserWithPlaidData(user.getId(), accessToken);
            return ResponseEntity.ok("User linked successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error linking user: " + e.getMessage());
        }
    }
    
    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(@RequestBody Map<String, Object> webhookData) {
        plaidService.handleWebhookNotification(webhookData);
        return ResponseEntity.ok("Webhook received");
    }

}
