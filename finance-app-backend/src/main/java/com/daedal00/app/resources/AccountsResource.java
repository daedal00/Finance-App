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
import java.util.List;

@RestController
@RequestMapping("/plaid/accounts")
public class AccountsResource {

    private static final Logger LOG = LoggerFactory.getLogger(AccountsResource.class);

    @Autowired
    private PlaidApi plaidClient;

    @Autowired
    private PlaidService plaidService;

    @GetMapping
    public List<AccountBase> getAccounts(@RequestParam String userId) throws IOException {
        String accessToken = plaidService.getAccessToken(userId);
        AccountsGetRequest request = new AccountsGetRequest().accessToken(accessToken);
        Response<AccountsGetResponse> response = plaidClient.accountsGet(request).execute();
        return response.body().getAccounts();
    }
}
