package com.daedal00.app.api.dto;

import lombok.Data;

@Data
public class AccountDTO {
    private String id;
    private String userId;
    private String name;
    private String institutionName;
    private String accountType;
    private Double balance;

    public AccountDTO() {}

    public AccountDTO(String id, String userId, String name, String institutionName, String accountType, Double balance) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.institutionName = institutionName;
        this.accountType = accountType;
        this.balance = balance;
    }
}
