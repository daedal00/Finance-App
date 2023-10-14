package com.daedal00.app.model;

import lombok.Data;

@Data
public class Budget {
    private String id;
    private String userId;
    private String category;
    private Double amount;
}
