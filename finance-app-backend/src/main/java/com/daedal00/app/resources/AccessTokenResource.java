// package com.daedal00.app.resources;

// import com.daedal00.app.service.PlaidService;
// import com.plaid.client.request.PlaidApi;
// import com.plaid.client.model.*;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.web.bind.annotation.*;
// import retrofit2.Response;

// import java.io.IOException;
// import java.util.Arrays;
// import java.util.List;

// @RestController
// @RequestMapping("/plaid")
// public class AccessTokenResource {

//     private static final Logger LOG = LoggerFactory.getLogger(AccessTokenResource.class);

//     @Autowired
//     private PlaidApi plaidClient;

//     @Autowired
//     private PlaidService plaidService;

//     private final List<String> plaidProducts = Arrays.asList("accounts", "transfers");


//     @PostMapping("/set_access_token")
//     public InfoResource.InfoResponse getAccessToken(@RequestParam("public_token") String publicToken) throws IOException {
//         ItemPublicTokenExchangeRequest request = new ItemPublicTokenExchangeRequest().publicToken(publicToken);
//         Response<ItemPublicTokenExchangeResponse> response = plaidClient.itemPublicTokenExchange(request).execute();

//         String userId = "defaultUser";  // Replace with actual user ID if you have authentication
//         plaidService.setAccessToken(userId, response.body().getAccessToken());
//         plaidService.setItemId(userId, response.body().getItemId());

//         LOG.info("public token: " + publicToken);
//         LOG.info("access token: " + plaidService.getAccessToken(userId));
//         LOG.info("item ID: " + plaidService.getItemId(userId));

//         if (plaidProducts.contains("transfers")) {
//             AccountsGetRequest accountsGetRequest = new AccountsGetRequest().accessToken(plaidService.getAccessToken(userId));
//             Response<AccountsGetResponse> accountsGetResponse = plaidClient.accountsGet(accountsGetRequest).execute();
//             String accountId = accountsGetResponse.body().getAccounts().get(0).getAccountId();

//             TransferAuthorizationUserInRequest user = new TransferAuthorizationUserInRequest().legalName("FirstName LastName");
//             TransferAuthorizationCreateRequest transferAuthorizationCreateRequest = new TransferAuthorizationCreateRequest()
//                 .accessToken(plaidService.getAccessToken(userId))
//                 .accountId(accountId)
//                 .type(TransferType.CREDIT)
//                 .network(TransferNetwork.ACH)
//                 .amount("1.34")
//                 .achClass(ACHClass.PPD)
//                 .user(user);

//             Response<TransferAuthorizationCreateResponse> transferAuthorizationCreateResponse = plaidClient.transferAuthorizationCreate(transferAuthorizationCreateRequest).execute();
//             String authorizationId = transferAuthorizationCreateResponse.body().getAuthorization().getId();

//             TransferCreateRequest transferCreateRequest = new TransferCreateRequest()
//                 .authorizationId(authorizationId)
//                 .accessToken(plaidService.getAccessToken(userId))
//                 .accountId(accountId)
//                 .description("Payment");

//             Response<TransferCreateResponse> transferCreateResponse = plaidClient.transferCreate(transferCreateRequest).execute();
//             // Assuming you want to store the transfer ID, you can adjust this part as needed
//         }

//         return new InfoResource.InfoResponse(Arrays.asList(), plaidService.getAccessToken(userId), plaidService.getItemId(userId));
//     }
// }
