package com.daedal00.app.api.dto;

import lombok.Data;

@Data
public class WebhookRequestDTO {
    private String webhookType;
    private String itemId;
    private String userId;
}
