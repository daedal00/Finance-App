package com.daedal00.app.resources;

import com.daedal00.app.service.PlaidService;
import com.plaid.client.request.PlaidApi;
import com.plaid.client.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import retrofit2.Response;

import java.io.IOException;

@RestController
@RequestMapping("/plaid/auth")
public class AuthResource {

    private static final Logger LOG = LoggerFactory.getLogger(AuthResource.class);

    @Autowired
    private PlaidApi plaidClient;

    @Autowired
    private PlaidService plaidService;

    @GetMapping
    public AuthGetResponse getAuth(@RequestParam String userId) throws IOException {
        String accessToken = plaidService.getAccessToken(userId);
        AuthGetRequest request = new AuthGetRequest().accessToken(accessToken);
        Response<AuthGetResponse> response = plaidClient.authGet(request).execute();
        return response.body();
    }
}
