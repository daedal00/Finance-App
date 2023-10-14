package com.daedal00.app.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "plaidItems")
public class PlaidItem {
     @Id
    private String id;
    private String accessToken;
    private String itemId;
    private String institutionId;

    public PlaidItem() {}

    public PlaidItem(String accessToken, String itemId, String institutionId) {
        this.accessToken = accessToken;
        this.itemId = itemId;
        this.institutionId = institutionId;
    }
}
