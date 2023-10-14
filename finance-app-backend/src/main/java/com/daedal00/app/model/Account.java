package com.daedal00.app.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "accounts")
public class Account {
     @Id
    private String id;
    private String userId;
    private String name;
    private String institutionName;
    private String accountType;
    private Double balance;
    private String plaidAccessToken;

    public Account() {}

    public Account(String userId, String name, String institutionName, String accountType, Double balance) {
        this.userId = userId;
        this.name = name;
        this.institutionName = institutionName;
        this.accountType = accountType;
        this.balance = balance;
    }
}
