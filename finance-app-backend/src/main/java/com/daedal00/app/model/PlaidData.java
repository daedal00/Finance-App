package com.daedal00.app.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "plaid_data")
public class PlaidData {

    @Id
    private String id;

    private String userId;
    private String accessToken;
    private String itemId;
}
