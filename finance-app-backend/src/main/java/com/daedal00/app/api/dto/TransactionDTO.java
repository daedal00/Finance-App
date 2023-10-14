package com.daedal00.app.api.dto;

import lombok.Data;

@Data
public class TransactionDTO {

    private String id;
    private String userId;
    private String description;
    private Double amount;
    private String transactionType; 
    private String transactionDate;

    public TransactionDTO() {}

    public TransactionDTO(String id, String userId, String description, Double amount, 
                           String transactionType, String transactionDate) {
        this.id = id;
        this.userId = userId;
        this.description = description;
        this.amount = amount;
        this.transactionType = transactionType;
        this.transactionDate = transactionDate;
    }
}
