package com.daedal00.app.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "transactions")
public class Transaction {
    
    @Id
    private String id;
    private String accountId;
    private String userId;
    private Double amount;
    private String category;
    private String merchantName;
    private Date date;

    public Transaction() {}

    public Transaction(String accountId, String userId, Double amount, String category, String merchantName, Date date) {
        this.accountId = accountId;
        this.userId = userId;
        this.amount = amount;
        this.category = category;
        this.merchantName = merchantName;
        this.date = date;
    }
}
